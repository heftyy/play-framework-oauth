package oauth;

import oauth.client.PlayWSOAuthClient;
import oauth.client.PrivateKeySigningService;
import oauth.client.SigningService;
import oauth.console.ClientTest;
import oauth.helper.RepositoryHelper;
import oauth.jwt.JsonWebToken;
import oauth.models.OAuthClient;
import oauth.services.GenerateKeyService;
import oauth.services.JwtService;
import org.junit.Test;
import play.Play;
import play.db.jpa.JPA;
import test.GenericFakeAppTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static play.test.Helpers.running;

public class JwtTest extends GenericFakeAppTest {
    @Test
    public void generateJwt() throws Exception {
        running(fakeApp, () -> {
            JPA.withTransaction(() -> {
                OAuthClient client = ClientTest.getClientWithKey();
                JPA.em().persist(client);

                JsonWebToken jwt = new JsonWebToken(
                        "RSA256",
                        "alg",
                        client.getAccessorId(),
                        "localhost:9000",
                        "/oauth/ws/data1",
                        PlayWSOAuthClient.getRequestTokenUrl(),
                        System.currentTimeMillis()
                );

                GenerateKeyService keyService = Play.application().injector().instanceOf(GenerateKeyService.class);
                JwtService jwtService = Play.application().injector().instanceOf(JwtService.class);

                SigningService signingService = new PrivateKeySigningService(
                        keyService.getSecretKeyPath(client.getAccessorId()),
                        client.getPassword()
                );

                try {
                    String assertion = signingService.signJwt(jwt);
                    JsonWebToken unHashedJwt = jwtService.getWebToken(assertion);
                    assertEquals(jwt, unHashedJwt);
                    assertTrue(jwtService.validateJWT(unHashedJwt));
                } catch(Exception e) {
                    e.printStackTrace();
                }
            });
        });
    }
}
