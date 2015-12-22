package oauth.server;

import oauth.accessor.AccessToken;
import oauth.models.OAuthApi;
import oauth.services.TokenService;
import org.junit.Test;
import play.Play;
import play.db.jpa.JPA;
import test.GenericFakeAppTest;

import static org.junit.Assert.assertEquals;
import static play.test.Helpers.running;

public class AccessTokenTest extends GenericFakeAppTest {

    @Test
    public void checkAccessToken() {
        running(fakeApp, () -> {
            JPA.withTransaction(() -> {
                OAuthApi api = new OAuthApi();
                api.setName("test-api");
                api.setDomain("localhost:9000");
                api.setEnabled(true);
                api.setScopeRequestUrl("/oauth/ws/scopes");

                JPA.em().persist(api);

                TokenService tokenService = Play.application().injector().instanceOf(TokenService.class);
                AccessToken accessToken = tokenService.createAccessToken("accessor1", System.currentTimeMillis(), "/oauth/ws/data", "localhost:9000");
                String token = accessToken.getToken();

                assertEquals(accessToken, tokenService.getAccessToken(token));
                // createAccessToken should return the existing token
                assertEquals(accessToken, tokenService.createAccessToken("accessor1", System.currentTimeMillis() + 1000, "/oauth/ws/data", "localhost:9000"));

                // assertTrue(tokenService.validateAccessToken());
            });
        });
    }
}
