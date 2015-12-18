package oauth.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import oauth.messages.WebServiceAuthorize;
import oauth.webservice.AccessorsContainer;
import oauth.webservice.scopes.ScopesContainer;
import play.Configuration;
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

    @Inject
    public COAuthWS(AccessorsContainer accessorsContainer, ScopesContainer scopesContainer) {
        this.accessorsContainer = accessorsContainer;
        this.scopesContainer = scopesContainer;
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
    public Result authorize() {

        try {
            JsonNode json = request().body().asJson();

            WebServiceAuthorize authorize = Json.fromJson(json, WebServiceAuthorize.class);

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

            if (accessorsContainer.validateAccessor(
                    authorize.getAccessorId(),
                    authorize.getAccessToken(),
                    authorize.getScope(),
                    request().remoteAddress(),
                    DOMAIN,
                    userAgent
            )) return ok(VALID);
            else return ok(INVALID);

        } catch (Exception e) {
            return badRequest("not a json, go away");
        }
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
