package oauth;

import oauth.client.PlayWSOAuthClient;
import oauth.client.PrivateKeySigningService;
import oauth.client.SigningService;
import oauth.jwt.JsonWebToken;
import oauth.models.OAuthClient;
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

                TestServices testServices = Play.application().injector().instanceOf(TestServices.class);

                JsonWebToken jwt = new JsonWebToken(
                        "RSA256",
                        "alg",
                        client.getAccessorId(),
                        "localhost:9000",
                        "/oauth/ws/data",
                        PlayWSOAuthClient.getRequestTokenUrl(),
                        System.currentTimeMillis()
                );

                SigningService signingService = new PrivateKeySigningService(
                        testServices.generateKeyService.getSecretKeyPath(client.getAccessorId()),
                        client.getPassword()
                );

                try {
                    String assertion = signingService.signJwt(jwt);
                    JsonWebToken unHashedJwt = testServices.jwtService.getWebToken(assertion);
                    assertEquals(jwt, unHashedJwt);
                    assertTrue(testServices.jwtService.validateJWT(unHashedJwt));
                } catch(Exception e) {
                    e.printStackTrace();
                }
            });
        });
    }
}
