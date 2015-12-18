package oauth.client;

import oauth.jwt.JsonWebToken;
import oauth.messages.AccessTokenMessage;
import oauth.messages.RequestToken;
import oauth.messages.WebServiceAuthorize;
import org.apache.commons.net.util.Base64;
import play.Configuration;
import play.libs.F.Promise;
import play.libs.Json;
import play.libs.ws.WS;
import play.libs.ws.WSResponse;
import play.mvc.Http;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.cert.CertificateException;

public class PlayWSOAuthClient implements OAuthApiClient {

    private final String passwordKey;
    private final String accessorId;
    private final String domain;
    private final String keyFile;
    private final String authScope;

    private static final int max_retries = 2;

    private static final Configuration oAuthConf = play.Configuration.root().getConfig("oauth-client");

    public static String getRequestTokenUrl() {
        String address = oAuthConf.getString("address");

        if (!address.startsWith("http://")) address = "http://" + address;

        address += ":" + oAuthConf.getString("port") + "/" + oAuthConf.getString("request-token");
        return address;
    }

    public PlayWSOAuthClient(String passwordKey, String accessorId, String domain, String keyFile, String authScope) {
        this.passwordKey = passwordKey;
        this.accessorId = accessorId;
        this.domain = domain;
        this.keyFile = keyFile;
        this.authScope = authScope;
    }

    public WSResponse doGet(String scopeUrl) throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, IOException, KeyStoreException, SignatureException, InvalidKeyException {
        return doGet(scopeUrl, 0);
    }

    public WSResponse doPost(String scopeUrl, String args) throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, IOException, KeyStoreException, SignatureException, InvalidKeyException {
        return doPost(scopeUrl, args, 0);
    }

    private WSResponse doGet(String scopeUrl, int current_retries) throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, IOException, KeyStoreException, SignatureException, InvalidKeyException {
        if (!scopeUrl.startsWith("/")) scopeUrl = "/" + scopeUrl;
        String url = this.getServiceAddress(scopeUrl);

        Promise<WSResponse> ret = WS.url(url).setHeader("Content-type", "application/json").setHeader("ACCESSOR-ID", this.accessorId).get();
        WSResponse response = ret.get(1000);

        if (response.getStatus() == Http.Status.OK) {
            return response;
        } else if (response.getStatus() == Http.Status.FORBIDDEN) {
            AccessTokenMessage accessToken = getAccessToken(scopeUrl);
            if (accessToken != null) {
                if (authorizeWithToken(scopeUrl, accessToken)) {
                    if (current_retries < max_retries) {
                        current_retries++;
                        return this.doGet(scopeUrl, current_retries);
                    }
                }
            }
        }
        return null;
    }

    private WSResponse doPost(String scopeUrl, String args, int current_retries) throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, IOException, KeyStoreException, SignatureException, InvalidKeyException {
        if (!scopeUrl.startsWith("/")) scopeUrl = "/" + scopeUrl;
        String url = this.getServiceAddress(scopeUrl);

        Promise<WSResponse> ret = WS.url(url).setHeader("Content-type", "application/json").setHeader("ACCESSOR-ID", this.accessorId).post(args);
        WSResponse response = ret.get(1000);

        if (response.getStatus() == Http.Status.OK) {
            return response;
        } else if (response.getStatus() == Http.Status.FORBIDDEN) {
            AccessTokenMessage accessToken = getAccessToken(scopeUrl);
            if (accessToken != null) {
                if (authorizeWithToken(scopeUrl, accessToken)) {
                    if (current_retries < max_retries) {
                        current_retries++;
                        return this.doPost(scopeUrl, args, current_retries);
                    }
                }
            }
        }
        return null;
    }

    public boolean authorizeWithToken(String scope, AccessTokenMessage accessTokenMessage) throws UnsupportedEncodingException {
        if (accessTokenMessage.getExpiresAt() <= System.currentTimeMillis()) {
            play.Logger.error("Token expired");
            return false;
        }

        WebServiceAuthorize authorize = new WebServiceAuthorize(accessorId, accessTokenMessage.getAccessToken(), scope);

        String url = this.getServiceAddress(this.authScope);

        Promise<WSResponse> ret = WS.url(url).setHeader("Content-type", "application/json").setHeader("ACCESSOR-ID", accessorId).post(authorize.getJson());
        WSResponse response = ret.get(10000);
        return response.getStatus() == Http.Status.OK;
    }

    public AccessTokenMessage getAccessToken(String scope) throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, IOException, KeyStoreException, SignatureException, InvalidKeyException {
        String authUrl = getRequestTokenUrl();
        JsonWebToken jwt = new JsonWebToken("RSA256", "alg", this.accessorId, this.domain, scope, authUrl, System.currentTimeMillis());

        String assertion = signJwt(jwt.getHeader().toString(), jwt.getClaim().toString());

        RequestToken requestToken = new RequestToken(assertion);
        Promise<WSResponse> ret = WS.url(authUrl).setHeader("Content-type", "application/json").setHeader("ACCESSOR-ID", this.accessorId).post(requestToken.getJson());

        WSResponse response = ret.get(10000);
        if (response.getStatus() == Http.Status.OK) {
            String jsonToken = response.getBody();
            return Json.fromJson(Json.parse(jsonToken), AccessTokenMessage.class);
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

    private String signJwt(String header, String claim) throws IOException, KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException, CertificateException, SignatureException, InvalidKeyException {
        StringBuilder token = new StringBuilder();
        token.append(Base64.encodeBase64URLSafeString(header.getBytes("UTF-8")));
        token.append(".");
        token.append(Base64.encodeBase64URLSafeString(claim.getBytes("UTF-8")));
        KeyStore keystore = KeyStore.getInstance("PKCS12");
        keystore.load(new FileInputStream(this.keyFile), this.passwordKey.toCharArray());
        PrivateKey privateKey = (PrivateKey) keystore.getKey("oauth", this.passwordKey.toCharArray());
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(token.toString().getBytes("UTF-8"));
        String signedPayload = Base64.encodeBase64URLSafeString(signature.sign());
        token.append(".");
        token.append(signedPayload);

        return token.toString();
    }

}
