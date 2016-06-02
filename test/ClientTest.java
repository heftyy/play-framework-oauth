import com.google.common.collect.Sets;
import oauth.models.OAuthClient;
import oauth.models.OAuthWS;
import oauth.services.GenerateKeyService;
import org.bouncycastle.operator.OperatorCreationException;
import org.joda.time.DateTime;
import org.junit.Test;
import play.Play;
import play.db.jpa.JPA;
import play.test.WithApplication;

import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

import static org.junit.Assert.assertNotNull;

public class ClientTest extends WithApplication {

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

    public static OAuthClient getClientWithScopesAndKey() throws Throwable {
        return JPA.withTransaction(() -> {
            OAuthClient client = ClientTest.getClientWithKey();
            OAuthWS ws = ApiTest.getWsWithAppScope();

            client.setWebServices(Sets.newHashSet(ws));
            client.setScopes(ws.getScopes());

            JPA.em().persist(client);

            return client;
        });
    }

    @Test
    public void generateKeysTest() {
        OAuthClient c = getClientWithKey();
        assertNotNull(c);
        assertNotNull(c.getPublicKey());
        assertNotNull(c.getPassword());
    }

    @Test
    public void saveClientTest() {
        OAuthClient client = getClientWithKey();
        assertNotNull(client);

        JPA.withTransaction(() -> {
            JPA.em().persist(client);
            assertNotNull(client.getId());
        });
    }

}
