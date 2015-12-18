package oauth.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import oauth.accessor.AccessToken;
import oauth.jwt.JsonWebToken;
import oauth.messages.RequestToken;
import oauth.messages.AccessTokenMessage;
import oauth.messages.WebServiceValidateRequest;
import oauth.messages.WebServiceValidateResponse;
import oauth.services.JwtService;
import oauth.services.ScopesService;
import oauth.services.TokenService;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;

@BodyParser.Of(BodyParser.Json.class)
public class COAuth extends Controller {

    public static final String INVALID = "invalid";
    public static final String VALID = "valid";

    private final TokenService accessorsService;
    private final ScopesService scopesService;
    private final JwtService jwtService;

    @Inject
    public COAuth(TokenService accessorsService, ScopesService scopesService, JwtService jwtService) {
        this.accessorsService = accessorsService;
        this.scopesService = scopesService;
        this.jwtService = jwtService;
    }

    /*
     * example JSON received
	 * {
	 * 	"request_token":"base64safeStringEncoded"
	 * }
	 */

    /**
     * Method for requesting access tokens, checks if the data sent by client is OK.
     *
     * @return String: JSON with access token / 'invalid' / bad request if data is missing in the HTTP request.
     */
    @Transactional
    public Result requestToken() {
        JsonNode json = request().body().asJson();
        if (json == null) return badRequest("Missing parameter");

        RequestToken requestToken = Json.fromJson(json, RequestToken.class);
        if(requestToken == null || requestToken.getAssertion() == null) return badRequest("Missing parameter");

        /**
         * Validating received data. Checking if data+signature created using secret key (.p12) is OK.
         */

        JsonWebToken jwt = null;
        try {
            jwt = jwtService.getWebToken(requestToken.getAssertion());
        } catch (UnsupportedEncodingException e) {
            return internalServerError(e.getMessage());
        }

        boolean valid = false;
        try {
            valid = jwtService.validateJWT(jwt);
        } catch (UnsupportedEncodingException | InvalidKeySpecException | SignatureException | InvalidKeyException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            return internalServerError(e.getMessage());
        }

        if (valid) {
            /**
             * Validating scopes.
             */
            if (!accessorsService.validateRequest(jwt.getClaim().getAccessorId(), jwt.getClaim().getDomain(), jwt.getClaim().getScope())) {
                return forbidden(INVALID);
            }

            AccessTokenMessage result = accessorsService.createNewAccessor(
                    jwt.getClaim().getAccessorId(),
                    jwt.getClaim().getTime(),
                    jwt.getClaim().getScope(),
                    request().remoteAddress(),
                    jwt.getClaim().getDomain());

            if (result == null) {
                return forbidden(INVALID);

            } else {
                return ok(result.getJson());
            }
        }
        return forbidden(INVALID);
    }

	/*
     * example JSON sent by the web service
	 * {
	 * 	"accessor_id":"32-hex-long-string",
	 * 	"access_token":"32-hex-long-string",
	 * 	"remote_address_of_user":"xxx.xxx.xxx.xxx",
	 * 	"scope_asked_for":"/url",
	 * 	"domain":"localhost:9000"
	 * }
	 */
	/*
	 * example JSON to be returned to the web service
	 * {
	 * 	"token_valid":"true",
	 * 	"accessor_id":"32-hex-long-string",
	 * 	"access_token":"32-hex-long-string",
	 * 	"remote_address_of_user":"xxx.xxx.xxx.xxx",
	 * 	"scope_allowed_to":["/url", ...]
	 * 	"expires_at":"long"
	 * }
	 * or 
	 * {
	 * 	"token_valid":"false"
	 * }
	 */

    /**
     * Method for webservices to validate if the user trying to access content on the WS
     * has been granted an access token and is valid.
     *
     * @return String: 'valid' / 'invalid' / bad request if data is missing in the HTTP request.
     */
    @Transactional
    public Result validateToken() {
        JsonNode json = request().body().asJson();
        WebServiceValidateRequest webServiceValidateRequest = Json.fromJson(json, WebServiceValidateRequest.class);

        if (webServiceValidateRequest == null ||
                webServiceValidateRequest.getAccessorId() == null ||
                webServiceValidateRequest.getAccessToken() == null ||
                webServiceValidateRequest.getClientRemoteAddress() == null ||
                webServiceValidateRequest.getRequestedScope() == null) {
            return badRequest("Missing parameter");
        } else {
            AccessToken accessor = accessorsService.validateAccessToken(
                    webServiceValidateRequest.getAccessorId(),
                    webServiceValidateRequest.getAccessToken(),
                    webServiceValidateRequest.getClientRemoteAddress()
            );


            if (accessor != null) {
                WebServiceValidateResponse response = new WebServiceValidateResponse(
                        true,
                        accessor.getAccessorId(),
                        accessor.getAccessToken(),
                        accessor.getRemoteAddress(),
                        scopesService.getLevelsFor(
                                webServiceValidateRequest.getAccessorId(),
                                webServiceValidateRequest.getDomain()),
                        accessor.getTokenExpiresAt()
                );

                return ok(response.getJson());
            } else {
                WebServiceValidateResponse response = new WebServiceValidateResponse(false);
                return ok(response.getJson());
            }
        }
    }
}
