package oauth.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import oauth.accessor.AccessToken;
import oauth.jwt.JsonWebToken;
import oauth.messages.AccessTokenFailure;
import oauth.messages.RequestToken;
import oauth.messages.WSValidateRequest;
import oauth.messages.WSValidateResponse;
import oauth.services.JwtService;
import oauth.services.LoggingService;
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
    private final LoggingService loggingService;

    @Inject
    public COAuth(TokenService accessorsService,
                  ScopesService scopesService,
                  JwtService jwtService,
                  LoggingService loggingService) {
        this.accessorsService = accessorsService;
        this.scopesService = scopesService;
        this.jwtService = jwtService;
        this.loggingService = loggingService;
    }

    /**
     * Method for requesting access tokens, checks if the data sent by client is OK.
     *
     * @return String: JSON with access token / 'invalid' / bad request if data is missing in the HTTP request.
     *
     * example JSON received
     * {
     * 	"requestToken":"base64safeStringEncoded"
     * }
     */
    @Transactional
    public Result requestToken() {
        JsonNode json = request().body().asJson();
        if (json == null) return badRequest("Missing parameter");

        RequestToken requestToken = Json.fromJson(json, RequestToken.class);
        if(requestToken == null || requestToken.getAssertion() == null) return badRequest("Missing parameter");

        loggingService.saveLog("requesting token", requestToken);

        /**
         * Validating received data. Checking if data+signature created using secret key (.p12) is OK.
         */
        JsonWebToken jwt;
        try {
            jwt = jwtService.getWebToken(requestToken.getAssertion());
            loggingService.saveLog("received jwt", jwt);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return badRequest(new AccessTokenFailure("invalid_request", "Parsing the assertion failed").getJson());
        }

        boolean jwtValid;
        try {
            jwtValid = jwtService.validateJWT(jwt);
        } catch (UnsupportedEncodingException | InvalidKeySpecException | SignatureException | InvalidKeyException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            return badRequest(new AccessTokenFailure("invalid_grant", "Validating jwt failed.").getJson());
        }

        if (jwtValid) {
            /**
             * Validating scopes.
             */
            if (!accessorsService.validateScopeRequest(jwt.getClaim().getAccessorId(), jwt.getClaim().getDomain(), jwt.getClaim().getScope())) {
                return badRequest(new AccessTokenFailure("invalid_scope").getJson());
            }

            AccessToken token = accessorsService.createAccessToken(
                    jwt.getClaim().getAccessorId(),
                    jwt.getClaim().getTime(),
                    jwt.getClaim().getScope(),
                    jwt.getClaim().getDomain());

            if (token == null) {
                return badRequest(new AccessTokenFailure("invalid_client").getJson());
            } else {
                loggingService.saveLog("token generated for " + jwt.getClaim().getAccessorId(), token.getMessage());
                return ok(token.getMessage().getJson());
            }
        }
        return badRequest(new AccessTokenFailure("invalid_grant").getJson());
    }

    /**
     * Method for web services to validate if the user trying to access content on the WS
     * has been granted an access token and is valid.
     *
     * example JSON sent by the web service
     * {
     * 	"accessToken":"32-hex-long-string",
     * 	"requestedScope":"/url",
     * 	"domain":"localhost:9000"
     * }
     *
     * example JSON to be returned to the web service
     * {
     * 	"tokenValid":"true",
     * 	"accessorId":"32-hex-long-string",
     * 	"accessToken":"32-hex-long-string",
     * 	"allowedScopes":["/url", ...]
     * }
     * or
     * {
     * 	"tokenValid":"false"
     * }
     */
    @Transactional
    public Result validateToken() {
        JsonNode json = request().body().asJson();
        WSValidateRequest validateRequest = Json.fromJson(json, WSValidateRequest.class);

        loggingService.saveLog("webservice requesting validation of token", validateRequest);

        if (validateRequest == null ||
                validateRequest.getAccessToken() == null ||
                validateRequest.getRequestedScope() == null ||
                validateRequest.getDomain() == null) {
            return badRequest("Missing parameter");
        } else {
            AccessToken accessToken = accessorsService.validateAccessToken(
                    validateRequest.getAccessToken(),
                    validateRequest.getRequestedScope(),
                    validateRequest.getDomain()
            );

            WSValidateResponse response;

            if (accessToken != null) {
                loggingService.saveLog("created access token on oauth server", accessToken);

                response = new WSValidateResponse(
                        true,
                        accessToken.getAccessorId(),
                        accessToken.getToken(),
                        scopesService.getScopesFor(
                                accessToken.getAccessorId(),
                                accessToken.getApi().getDomain())
                );
            } else {
                response = new WSValidateResponse(false);
            }

            loggingService.saveLog("webservice access token response", response);
            return ok(response.getJson());
        }
    }
}
