package oauth;

import oauth.models.OAuthClient;
import oauth.services.GenerateKeyService;
import org.bouncycastle.operator.OperatorCreationException;
import org.joda.time.DateTime;
import org.junit.Test;
import play.Play;
import play.db.jpa.JPA;
import play.test.Helpers;
import test.GenericFakeAppTest;

import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

import static org.junit.Assert.assertNotNull;

public class ClientTest extends GenericFakeAppTest {

    public static OAuthClient getClientWithKey() {
        OAuthClient c = new OAuthClient();
        c.setCreationTime(DateTime.now());
        c.setName("test-client");

        GenerateKeyService generateKeyService = Play.application().injector().instanceOf(GenerateKeyService.class);
        try {
            generateKeyService.generateKey(c);
        } catch (CertificateException | NoSuchAlgorithmException | OperatorCreationException | KeyStoreException | SignatureException | NoSuchProviderException | InvalidKeyException | IOException e) {
            e.printStackTrace();
        }

        return c;
    }

    @Test
    public void generateKeysTest() {
        Helpers.running(fakeApp, () -> {
            OAuthClient c = getClientWithKey();
            assertNotNull(c);
            assertNotNull(c.getPublicKey());
            assertNotNull(c.getPassword());
        });
    }

    @Test
    public void saveClientTest() {
        Helpers.running(fakeApp, () -> {
            OAuthClient client = getClientWithKey();
            assertNotNull(client);

            JPA.withTransaction(() -> {
                JPA.em().persist(client);
                assertNotNull(client.getId());
            });
        });
    }

}
