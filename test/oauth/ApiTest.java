package oauth;

import com.google.common.collect.Sets;
import oauth.models.OAuthApi;
import oauth.models.OAuthClient;
import oauth.models.OAuthLevel;
import oauth.models.OAuthScope;
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
import static org.junit.Assert.assertTrue;

public class ApiTest extends GenericFakeAppTest {

    public static OAuthApi getApiWithAllScope() {
        OAuthApi a = new OAuthApi();
        a.setName("test-api");
        a.setDomain("localhost:9000");
        a.setOnGlobally(true);
        a.setScopeRequestUrl("/oauth/ws/scopes");

        OAuthLevel l = new OAuthLevel("ALL", "all scopes", a);
        OAuthScope s = new OAuthScope("*", "GET", "json", "", l);
        l.setScopes(Sets.newHashSet(s));
        a.setLevels(Sets.newHashSet(l));

        return a;
    }

    @Test
    public void saveApiTest() {
        Helpers.running(fakeApp, () -> {
            OAuthApi api = getApiWithAllScope();
            assertNotNull(api);

            assertTrue(api.getDomain().equals("localhost:9000"));
            assertTrue(api.getLevels().size() == 1);
            assertTrue(api.getLevels().iterator().next().getScopes().size() == 1);

            JPA.withTransaction(() -> {
                JPA.em().persist(api);
                assertNotNull(api.getId());
            });
        });
    }

}
