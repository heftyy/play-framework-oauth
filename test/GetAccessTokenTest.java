import oauth.client.PlayWSOAuthClient;
import oauth.messages.AccessTokenSuccess;
import oauth.models.OAuthClient;
import oauth.services.GenerateKeyService;
import org.junit.Test;
import play.db.jpa.JPA;
import helper.WithTestServer;

import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

import static org.junit.Assert.assertNotNull;

public class GetAccessTokenTest extends WithTestServer {

    private final String domain = "localhost:9000";

    public static AccessTokenSuccess getAccessToken(OAuthClient client,
                                                    String domain,
                                                    String scopeUrl,
                                                    String keyPath) throws Throwable {
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
    public void getAccessTokenTest() {
        GenerateKeyService keyService = app.injector().instanceOf(GenerateKeyService.class);

        JPA.withTransaction(() -> {
            OAuthClient client = ClientTest.getClientWithScopesAndKey();

            AccessTokenSuccess token = getAccessToken(client,
                                                      domain,
                                                      "/test",
                                                      keyService.getSecretKeyPath(client.getAccessorId()));
            assertNotNull(token);
            assertNotNull(token.getAccessToken());
            assertNotNull(token.getTokenType());
            assertNotNull(token.getExpiresIn());
        });
    }
}
