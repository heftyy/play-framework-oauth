package oauth.client;

import oauth.accessor.AccessToken;
import oauth.helper.DatabaseHelper;
import oauth.helper.RepositoryHelper;
import oauth.messages.AccessTokenSuccess;
import oauth.models.OAuthApi;
import oauth.models.OAuthClient;
import oauth.services.GenerateKeyService;
import org.junit.Test;
import play.Play;
import play.db.jpa.JPA;
import test.GenericFakeAppTest;

import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static play.test.Helpers.running;
import static play.test.Helpers.testServer;

public class GetAccessTokenTest extends GenericFakeAppTest {

    public static AccessTokenSuccess getAccessToken(OAuthClient client, String domain, String scopeUrl, String keyPath) throws Throwable {
        PlayWSOAuthClient clientApi = new PlayWSOAuthClient(
                client.getPassword(),
                client.getAccessorId(),
                domain,
                keyPath);

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
            DatabaseHelper.prepareDatabase();

            RepositoryHelper repositoryHelper = Play.application().injector().instanceOf(RepositoryHelper.class);

            AccessToken accessToken = null;
            try {
                accessToken = JPA.withTransaction(() -> {
                    OAuthApi api = repositoryHelper.apiRepository.findByField("domain", "localhost:9000");

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

            assertEquals(accessToken.getAccessorId(), accessToken.getMessage().getAccessorId());
            assertEquals(accessToken.getToken(), accessToken.getMessage().getAccessToken());
        });
    }

    @Test
    public void getAccessTokenTest() {
        running(testServer(9000, fakeApp), () -> {
            DatabaseHelper.prepareDatabase();

            String domain = "localhost:9000";

            RepositoryHelper repositoryHelper = Play.application().injector().instanceOf(RepositoryHelper.class);
            GenerateKeyService keyService = Play.application().injector().instanceOf(GenerateKeyService.class);

            JPA.withTransaction(() -> {
                OAuthClient client = repositoryHelper.clientRepository.findByField("id", 1L, "apis", "scopes", "scopes.urlPatterns");
                AccessTokenSuccess token = getAccessToken(client, domain, "/test", keyService.getSecretKeyPath(client.getAccessorId()));
                assertNotNull(token);
                assertNotNull(token.getAccessToken());
                assertNotNull(token.getTokenType());
                assertNotNull(token.getExpiresIn());
            });
        });
    }
}
