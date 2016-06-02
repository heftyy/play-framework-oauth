import oauth.models.OAuthWS;
import org.junit.Test;
import play.db.jpa.JPA;
import play.test.WithApplication;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ApiTest extends WithApplication {

    public static OAuthWS getWsWithAppScope() {
        OAuthWS a = new OAuthWS();
        a.setName("test-api");
        a.setDomain("localhost:9000");
        a.setEnabled(true);
        a.setScopeRequestUrl("/oauth/ws/scopes");

        a.setScopes(ScopesTest.readScopesFile("test/test_scopes.json"));

        return a;
    }

    @Test
    public void saveApiTest() {
        OAuthWS ws = getWsWithAppScope();
        assertNotNull(ws);

        assertTrue(ws.getDomain().equals("localhost:9000"));
        assertTrue(ws.getScopes().size() == 1);
        assertTrue(ws.getScopes().iterator().next().getUrlPatterns().size() == 2);

        JPA.withTransaction(() -> {
            JPA.em().persist(ws);
            assertNotNull(ws.getId());
        });
    }

}
