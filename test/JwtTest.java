import oauth.client.PlayWSOAuthClient;
import oauth.client.PrivateKeySigningService;
import oauth.client.SigningService;
import oauth.jwt.JsonWebToken;
import oauth.models.OAuthClient;
import oauth.services.GenerateKeyService;
import oauth.services.JwtService;
import org.junit.Test;
import play.Play;
import play.db.jpa.JPA;
import play.test.WithApplication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JwtTest extends WithApplication {
    @Test
    public void generateJwt() throws Exception {
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
    }
}
