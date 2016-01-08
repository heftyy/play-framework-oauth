package oauth.console;

import oauth.models.OAuthWS;
import oauth.ws.ScopesTest;
import org.junit.Test;
import play.db.jpa.JPA;
import test.GenericFakeAppTest;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static play.test.Helpers.running;

public class ApiTest extends GenericFakeAppTest {

    public static OAuthWS getApiWithAllScope() {
        OAuthWS a = new OAuthWS();
        a.setName("test-api");
        a.setDomain("localhost:9000");
        a.setEnabled(true);
        a.setScopeRequestUrl("/oauth/ws/scopes");

        a.setScopes(ScopesTest.readScopesFile("test/scopes.json"));

        return a;
    }

    @Test
    public void saveApiTest() {
        running(fakeApp, () -> {
            OAuthWS ws = getApiWithAllScope();
            assertNotNull(ws);

            assertTrue(ws.getDomain().equals("localhost:9000"));
            assertTrue(ws.getScopes().size() == 1);
            assertTrue(ws.getScopes().iterator().next().getUrlPatterns().size() == 2);

            JPA.withTransaction(() -> {
                JPA.em().persist(ws);
                assertNotNull(ws.getId());
            });
        });
    }

}
