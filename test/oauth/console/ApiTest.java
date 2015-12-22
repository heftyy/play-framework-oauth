package oauth.console;

import oauth.models.OAuthApi;
import oauth.ws.ScopesTest;
import org.junit.Test;
import play.db.jpa.JPA;
import test.GenericFakeAppTest;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static play.test.Helpers.running;

public class ApiTest extends GenericFakeAppTest {

    public static OAuthApi getApiWithAllScope() {
        OAuthApi a = new OAuthApi();
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
            OAuthApi api = getApiWithAllScope();
            assertNotNull(api);

            assertTrue(api.getDomain().equals("localhost:9000"));
            assertTrue(api.getScopes().size() == 1);
            assertTrue(api.getScopes().iterator().next().getUrlPatterns().size() == 2);

            JPA.withTransaction(() -> {
                JPA.em().persist(api);
                assertNotNull(api.getId());
            });
        });
    }

}
