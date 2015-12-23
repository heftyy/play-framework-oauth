package oauth.server;

import oauth.accessor.AccessToken;
import oauth.helper.DatabaseHelper;
import oauth.helper.RepositoryHelper;
import oauth.models.OAuthApi;
import oauth.services.TokenService;
import org.junit.Test;
import play.Play;
import play.db.jpa.JPA;
import test.GenericFakeAppTest;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static play.test.Helpers.running;

public class AccessTokenTest extends GenericFakeAppTest {

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

                assertEquals(accessToken, new AccessToken("accessor1", accessToken.getToken(), accessToken.getExpiresAt(), accessToken.getType(), accessToken.getApi()));

                // assertTrue(tokenService.validateAccessToken());
            });
        });
    }
}
