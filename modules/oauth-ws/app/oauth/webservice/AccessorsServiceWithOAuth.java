package oauth.webservice;

import com.fasterxml.jackson.databind.JsonNode;
import oauth.messages.WSValidateRequest;
import oauth.messages.WSValidateResponse;
import play.Configuration;
import play.libs.F;
import play.libs.Json;
import play.libs.ws.WS;
import play.libs.ws.WSResponse;

public class AccessorsServiceWithOAuth implements AccessorsService {
    private static Configuration CONFIG = Configuration.root().getConfig("oauth-ws");
    private static String VALIDATE_TOKEN_URL = CONFIG.getString("validate-token");
    private static String DOMAIN = CONFIG.getString("domain");

    @Override
    public Accessor validateToken(String token, String requestedScope) {
        WSValidateResponse response = sendValidateRequest(token, requestedScope);
        if(response == null) return null;

        return getAccessor(response);
    }

    private WSValidateResponse sendValidateRequest(String accessToken, String requestedScope) {
        WSValidateRequest request = new WSValidateRequest(
                accessToken,
                requestedScope,
                DOMAIN
        );

        F.Promise<WSResponse> promise = WS.url(VALIDATE_TOKEN_URL).setHeader("Content-Type", "application/json").post(request.getJson());

        JsonNode responseJson = promise.get(10000L).asJson();

        WSValidateResponse response = Json.fromJson(responseJson, WSValidateResponse.class);
        if(response != null) return response;
        return null;
    }
}
