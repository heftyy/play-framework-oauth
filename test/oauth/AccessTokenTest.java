package oauth;

import oauth.accessor.AccessToken;
import oauth.client.PlayWSOAuthClient;
import oauth.messages.AccessTokenMessage;
import oauth.models.OAuthApi;
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
import java.security.*;
import java.security.cert.CertificateException;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static play.test.Helpers.running;
import static play.test.Helpers.testServer;

public class AccessTokenTest extends GenericFakeAppTest {
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

    @Test
    public void serializeAccessToken() {
        running(fakeApp, () -> {
            PrepareDatabase.prepareDatabase();

            TestServices testServices = Play.application().injector().instanceOf(TestServices.class);

            AccessToken accessToken = null;
            try {
                accessToken = JPA.withTransaction(() -> {
                    OAuthApi api = testServices.apiRepository.findByField("domain", "localhost:9000");

                    return new AccessToken(
                            "123131312",
                            UUID.randomUUID().toString(),
                            System.currentTimeMillis() + 150000,
                            "Bearer",
                            api);
                });
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }

            assertNotNull(accessToken);
            assertNotNull(accessToken.getJson());
        });
    }

    @Test
    public void getAccessTokenTest() {
        running(testServer(9000, fakeApp), () -> {
            PrepareDatabase.prepareDatabase();

            String domain = "localhost:9000";

            TestServices testServices = Play.application().injector().instanceOf(TestServices.class);

            JPA.withTransaction(() -> {
                OAuthClient client = testServices.clientRepository.findByField("id", 1L, "apis", "levels", "levels.scopes");
                AccessTokenMessage token = getAccessToken(client, domain, "/test1", testServices.generateKeyService.getSecretKeyPath(client.getAccessorId()));
                assertNotNull(token);
                assertNotNull(token.getAccessToken());
                assertNotNull(token.getTokenType());
                assertNotNull(token.getExpiresAt());
            });
        });
    }

    @Test
    public void authorizeTest() {
        running(testServer(9000, fakeApp), () -> {
            PrepareDatabase.prepareDatabase();

            String domain = "localhost:9000";

            TestServices testServices = Play.application().injector().instanceOf(TestServices.class);

            JPA.withTransaction(() -> {
                OAuthClient client = testServices.clientRepository.findByField("id", 1L, "apis", "levels", "levels.scopes");
                AccessTokenMessage token = getAccessToken(client, domain, "/test", testServices.generateKeyService.getSecretKeyPath(client.getAccessorId()));

                F.Promise<WSResponse> ret = WS.url("http://localhost:9000/oauth/ws/data").
                        setHeader("Content-type", "application/json").
                        setHeader("Authorization", PlayWSOAuthClient.getAuthorizationHeader(token.getAccessToken())).
                        get();

                WSResponse response = ret.get(100000);

                assertTrue(response.getStatus() == Http.Status.OK);
                assertTrue(response.getBody().contains("ws_data"));
            });
        });
    }
}
