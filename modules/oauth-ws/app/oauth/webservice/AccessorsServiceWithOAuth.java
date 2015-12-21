package oauth.webservice;

import com.fasterxml.jackson.databind.JsonNode;
import oauth.messages.WebServiceValidateRequest;
import oauth.messages.WebServiceValidateResponse;
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
        WebServiceValidateResponse response = sendValidateRequest(token, requestedScope);
        if(response == null) return null;

        return getAccessor(response);
    }

    private WebServiceValidateResponse sendValidateRequest(String accessToken, String requestedScope) {
        WebServiceValidateRequest request = new WebServiceValidateRequest(
                accessToken,
                requestedScope,
                DOMAIN
        );

        F.Promise<WSResponse> promise = WS.url(VALIDATE_TOKEN_URL).setHeader("Content-Type", "application/json").post(request.getJson());

        JsonNode responseJson = promise.get(10000L).asJson();

        WebServiceValidateResponse response = Json.fromJson(responseJson, WebServiceValidateResponse.class);
        if(response != null) return response;
        return null;
    }
}
