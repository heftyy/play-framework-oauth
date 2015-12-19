package oauth.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import oauth.messages.WebServiceAuthorize;
import oauth.services.LoggingService;
import oauth.webservice.AccessorsContainer;
import oauth.webservice.ValidAccessor;
import oauth.webservice.scopes.ScopesContainer;
import play.Configuration;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;

public class COAuthWS extends Controller {

    private static Configuration oauth = play.Configuration.root().getConfig("oauth-ws");
    public static String DOMAIN = oauth.getString("domain");
    public static String VALIDATE = oauth.getString("validate");

    private static final String VALID = "valid";
    private static final String INVALID = "invalid";

    private final ScopesContainer scopesContainer;
    private final AccessorsContainer accessorsContainer;
    private final LoggingService loggingService;

    @Inject
    public COAuthWS(AccessorsContainer accessorsContainer,
                    ScopesContainer scopesContainer,
                    LoggingService loggingService) {
        this.accessorsContainer = accessorsContainer;
        this.scopesContainer = scopesContainer;
        this.loggingService = loggingService;
    }

    /**
     * Authorization method, a request is sent here by user after obtaining an
     * access token from the oauth authorization server. Webservice determines
     * if its a valid access token and authorizes the client so calling this
     * method is no longer necessary.
     *
     * @return String: 'valid' / 'invalid' or bad request if the JSON received
     * is incomplete.
     */
    @Transactional
    public Result authorize() {
        JsonNode json = request().body().asJson();

        WebServiceAuthorize authorize = Json.fromJson(json, WebServiceAuthorize.class);

        loggingService.saveLog("client authorizing on web service", authorize);

        if (authorize.getAccessorId() == null || authorize.getAccessToken() == null || authorize.getScope() == null) {
            return badRequest("Json is incomplete.");
        }

        String userAgent = null, ip = null;
        try {
            userAgent = request().headers().get("User-Agent")[0];
//                  ip = request().remoteAddress();
//					System.out.println(userAgent);
//					System.out.println(ip);
        } catch (NullPointerException e) {
            return badRequest("Bad request\r\n");
        }

        ValidAccessor accessor = accessorsContainer.validateAccessor(
                authorize.getAccessorId(),
                authorize.getAccessToken(),
                authorize.getScope(),
                request().remoteAddress(),
                DOMAIN,
                userAgent
        );

        loggingService.saveLog("created accessor on web service", accessor);

        if(accessor == null) return badRequest(INVALID);
        else return ok(VALID);
    }

    /**
     * Method called by oauth to download scopes from the webservice.
     *
     * @return String: String with all the scopes.
     */
    public Result scopes() {
        return ok(Json.toJson(scopesContainer.getLevels()));
    }
}
