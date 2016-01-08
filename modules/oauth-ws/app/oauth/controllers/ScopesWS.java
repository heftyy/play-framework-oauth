package oauth.controllers;

import oauth.webservice.scopes.ScopesContainer;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;

public class ScopesWS extends Controller {
    private static final String VALID = "valid";
    private static final String INVALID = "invalid";

    private final ScopesContainer scopesContainer;

    @Inject
    public ScopesWS(ScopesContainer scopesContainer) {
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

    /*
    @Deprecated
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
        } catch (NullPointerException e) {
            return badRequest("Bad request\r\n");
        }

        Accessor accessor = accessorsService.validateToken(
                authorize.getAccessToken(),
                authorize.getScope()
        );

        if(accessor != null) {
            loggingService.saveLog("created accessor on web service", accessor);
            return ok(VALID);
        } else {
            loggingService.saveLog("creating accessor on web service failed", Json.newObject());
            return badRequest(INVALID);
        }
    }
    */

    /**
     * Method called by oauth to download scopes from the webservice.
     *
     * @return String: String with all the scopes.
     */
    public Result scopes() {
        return ok(Json.toJson(scopesContainer.getScopes()));
    }
}
