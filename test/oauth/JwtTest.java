package oauth;

import com.fasterxml.jackson.databind.JsonNode;
import oauth.client.PlayWSOAuthClient;
import oauth.messages.AccessTokenMessage;
import oauth.models.OAuthClient;
import org.junit.Test;
import play.Play;
import play.db.jpa.JPA;
import play.libs.F;
import play.libs.ws.WS;
import play.libs.ws.WSResponse;
import play.mvc.Http;
import test.GenericFakeAppTest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.cert.CertificateException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static play.test.Helpers.running;

public class JwtTest extends GenericFakeAppTest {

    public AccessTokenMessage getAccessToken(OAuthClient client, String domain, String scopeUrl, String keyPath) throws Throwable {
        PlayWSOAuthClient clientApi = new PlayWSOAuthClient(
                client.getPassword(),
                client.getAccessorId(),
                domain,
                keyPath,
                "/oauth/ws/auth");

        try {
            return clientApi.getAccessToken(scopeUrl);

        } catch (CertificateException | UnrecoverableKeyException | NoSuchAlgorithmException | KeyStoreException | IOException | InvalidKeyException | SignatureException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean authorizeWithToken(OAuthClient client, String domain, String scopeUrl, AccessTokenMessage token) {
        PlayWSOAuthClient clientApi = new PlayWSOAuthClient(
                client.getPassword(),
                client.getAccessorId(),
                domain,
                null, // not needed
                "/oauth/ws/auth");

        try {
            return clientApi.authorizeWithToken(scopeUrl, token);
        } catch (UnsupportedEncodingException e) {
            return false;
        }
    }

    @Test
    public void getAccessTokenTest() {
        running(fakeApp, () -> {
            PrepareDatabase.prepareDatabase();

            String domain = "localhost:9000";

            TestServices testServices = Play.application().injector().instanceOf(TestServices.class);

            JPA.withTransaction(() -> {
                OAuthClient client = testServices.clientRepository.findByField("id", 1L, "apis", "levels", "levels.scopes");
                AccessTokenMessage token = getAccessToken(client, domain, "/oauth/ws/data", testServices.generateKeyService.getSecretKeyPath(client.getAccessorId()));
                assertNotNull(token);
                assertNotNull(token.getAccessToken());
                assertNotNull(token.getTokenType());
                assertNotNull(token.getExpiresAt());
            });
        });
    }

    @Test
    public void authorizeTest() {
        running(fakeApp, () -> {
            PrepareDatabase.prepareDatabase();

            String domain = "localhost:9000";

            TestServices testServices = Play.application().injector().instanceOf(TestServices.class);

            JPA.withTransaction(() -> {
                OAuthClient client = testServices.clientRepository.findByField("id", 1L, "apis", "levels", "levels.scopes");
                AccessTokenMessage token = getAccessToken(client, domain, "/oauth/ws/data", testServices.generateKeyService.getSecretKeyPath(client.getAccessorId()));
                assertTrue(authorizeWithToken(client, domain, "/oauth/ws/data", token));

                F.Promise<WSResponse> ret = WS.url("http://localhost:9000/oauth/ws/data").setHeader("Content-type", "application/json").setHeader("ACCESSOR-ID", client.getAccessorId()).get();
                WSResponse response = ret.get(1000);

                assertTrue(response.getStatus() == Http.Status.OK);
                assertTrue(response.getBody().contains("ws_data"));
            });
        });
    }
}
