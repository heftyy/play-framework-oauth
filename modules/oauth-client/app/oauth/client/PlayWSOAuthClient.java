package oauth.client;

import oauth.jwt.JsonWebToken;
import oauth.messages.AccessTokenSuccess;
import oauth.messages.RequestToken;
import play.Configuration;
import play.libs.F.Promise;
import play.libs.Json;
import play.libs.ws.WS;
import play.libs.ws.WSResponse;
import play.mvc.Http;

import java.io.UnsupportedEncodingException;

public class PlayWSOAuthClient implements OAuthApiClient {
    private final String accessorId;
    private final String domain;
    private final SigningService signingService;
    private String token;

    private static final int max_retries = 1;

    private static final Configuration CONFIG = play.Configuration.root().getConfig("oauth-client");

    public static String getRequestTokenUrl() {
        return CONFIG.getString("request-token");
    }

    public static String getAuthorizationHeader(String token) {
        return "Bearer " + token;
    }

    public PlayWSOAuthClient(String password, String accessorId, String domain, String keyFile) {
        this.accessorId = accessorId;
        this.domain = domain;
        this.signingService = new PrivateKeySigningService(keyFile, password);
    }

    public WSResponse doGet(String scopeUrl) throws Exception {
        return doGet(scopeUrl, 0);
    }

    public WSResponse doPost(String scopeUrl, String args) throws Exception {
        return doPost(scopeUrl, args, 0);
    }

    private WSResponse doGet(String scopeUrl, int current_retries) throws Exception {
        if(this.token == null) token = getAccessToken(scopeUrl).getAccessToken();

        String url = this.getServiceAddress(scopeUrl);

        Promise<WSResponse> ret = WS.url(url).
                setHeader("Content-type", "application/json").
                setHeader("Authorization", getAuthorizationHeader(token)).
                get();

        WSResponse response = ret.get(10000L);

        if (response.getStatus() == Http.Status.OK) {
            return response;
        } else if (response.getStatus() == Http.Status.FORBIDDEN) {
            AccessTokenSuccess tokenMessage = getAccessToken(scopeUrl);
            if (tokenMessage != null && current_retries < max_retries) {
                this.token = tokenMessage.getAccessToken();
                return this.doGet(scopeUrl, ++current_retries);
            }
        }

        return response;
    }

    private WSResponse doPost(String scopeUrl, String args, int current_retries) throws Exception {
        if(this.token == null) token = getAccessToken(scopeUrl).getAccessToken();

        String url = this.getServiceAddress(scopeUrl);

        Promise<WSResponse> ret = WS.url(url).
                setHeader("Content-type", "application/json").
                setHeader("Authorization", getAuthorizationHeader(token)).
                post(args);

        WSResponse response = ret.get(10000L);

        if (response.getStatus() == Http.Status.OK) {
            return response;
        } else if (response.getStatus() == Http.Status.FORBIDDEN) {
            AccessTokenSuccess tokenMessage = getAccessToken(scopeUrl);
            if (tokenMessage != null && current_retries < max_retries) {
                this.token = tokenMessage.getAccessToken();
                return this.doPost(scopeUrl, args, ++current_retries);
            }
        }

        return response;
    }

    public AccessTokenSuccess getAccessToken(String requestedScope) throws Exception {
        String authUrl = getRequestTokenUrl();
        JsonWebToken jwt = new JsonWebToken("RSA256", "alg", this.accessorId, this.domain, requestedScope, authUrl, System.currentTimeMillis());

        String assertion = signingService.signJwt(jwt);

        RequestToken requestToken = new RequestToken(assertion);
        Promise<WSResponse> ret = WS.url(authUrl).setHeader("Content-type", "application/json").post(requestToken.getJson());

        WSResponse response = ret.get(10000);
        if (response.getStatus() == Http.Status.OK) {
            String jsonToken = response.getBody();
            return Json.fromJson(Json.parse(jsonToken), AccessTokenSuccess.class);
        }
        return null;
    }

    private String getServiceAddress(Object... args) throws UnsupportedEncodingException {
        String url = this.domain;
        if (!this.domain.startsWith("http://")) url = "http://" + this.domain;
        for (Object c : args) {
            if (!c.toString().startsWith("/") && !url.endsWith("/")) {
                url += "/" + c.toString();
            } else {
                url += c.toString();
            }
        }
        url = url.replace(" ", "+");
        return url;
    }

}
