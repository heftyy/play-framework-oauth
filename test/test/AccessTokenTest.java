package test;

import helper.RepositoryHelper;
import helper.WithTestServer;
import oauth.accessor.AccessToken;
import oauth.models.OAuthWS;
import oauth.services.TokenService;
import org.junit.Test;
import play.Play;
import play.db.jpa.JPA;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class AccessTokenTest extends WithTestServer {

    @Test
    public void serializeAccessToken() {
        RepositoryHelper repositoryHelper = Play.application().injector().instanceOf(RepositoryHelper.class);

        AccessToken accessToken = null;
        try {
            accessToken = JPA.withTransaction(() -> {
                OAuthWS ws = repositoryHelper.apiRepository.findByField("domain", "localhost:9000");

                return new AccessToken(
                        "123131312",
                        UUID.randomUUID().toString(),
                        System.currentTimeMillis() + 150000,
                        "Bearer",
                        ws);
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        assertNotNull(accessToken);
        assertNotNull(accessToken.getJson());

        assertEquals(accessToken.getAccessorId(), accessToken.getMessage().getAccessorId());
        assertEquals(accessToken.getToken(), accessToken.getMessage().getAccessToken());
    }

    @Test
    public void checkAccessToken() {
        JPA.withTransaction(() -> {
            OAuthWS ws = new OAuthWS();
            ws.setName("test-api");
            ws.setDomain("localhost:9000");
            ws.setEnabled(true);
            ws.setScopeRequestUrl("/oauth/ws/scopes");

            JPA.em().persist(ws);

            TokenService tokenService = Play.application().injector().instanceOf(TokenService.class);
            AccessToken accessToken = tokenService.createAccessToken("accessor1", System.currentTimeMillis(), "/oauth/ws/data", "localhost:9000");
            String token = accessToken.getToken();

            assertEquals(accessToken, tokenService.getAccessToken(token));
            // createAccessToken should return the existing token
            assertEquals(accessToken, tokenService.createAccessToken("accessor1", System.currentTimeMillis() + 1000, "/oauth/ws/data", "localhost:9000"));

            assertEquals(accessToken, new AccessToken("accessor1", accessToken.getToken(), accessToken.getExpiresAt(), accessToken.getType(), accessToken.getWs()));

            // assertTrue(tokenService.validateAccessToken());
        });
    }
}
