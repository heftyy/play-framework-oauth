import helper.RepositoryHelper;
import helper.WithTestServer;
import oauth.client.OAuthApiClient;
import oauth.client.PlayWSOAuthClient;
import oauth.models.OAuthClient;
import oauth.services.GenerateKeyService;
import org.junit.Test;
import play.db.jpa.JPA;
import play.libs.ws.WSResponse;
import play.mvc.Http;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class OAuthClientTest extends WithTestServer {

    private final String domain = "localhost:9000";

    @Test
    public void testClientGet() {
        RepositoryHelper repositoryHelper = app.injector().instanceOf(RepositoryHelper.class);
        GenerateKeyService keyService = app.injector().instanceOf(GenerateKeyService.class);

        JPA.withTransaction(() -> {
            OAuthClient client = ClientTest.getClientWithScopesAndKey();

            OAuthApiClient apiClient = new PlayWSOAuthClient(
                    client.getPassword(),
                    client.getAccessorId(),
                    domain,
                    keyService.getSecretKeyPath(client.getAccessorId())
            );

            { // OK
                WSResponse response = apiClient.doGet("/oauth/ws/data1");

                assertTrue(response.getStatus() == Http.Status.OK);
                assertTrue(response.getBody().contains("ws_data"));
            }

            { // FAIL
                WSResponse response = apiClient.doGet("/oauth/ws/data2");

                assertFalse(response.getStatus() == Http.Status.OK);
                assertFalse(response.getBody().contains("ws_data"));
            }
        });
    }

    @Test
    public void testClientPost() {
        RepositoryHelper repositoryHelper = app.injector().instanceOf(RepositoryHelper.class);
        GenerateKeyService keyService = app.injector().instanceOf(GenerateKeyService.class);

        JPA.withTransaction(() -> {
            OAuthClient client = ClientTest.getClientWithScopesAndKey();

            OAuthApiClient apiClient = new PlayWSOAuthClient(
                    client.getPassword(),
                    client.getAccessorId(),
                    domain,
                    keyService.getSecretKeyPath(client.getAccessorId())
            );

            { // OK
                WSResponse response = apiClient.doPost("/oauth/ws/data1", "{}");

                assertTrue(response.getStatus() == Http.Status.OK);
                assertTrue(response.getBody().contains("ws_data"));
            }

            { // FAIL
                WSResponse response = apiClient.doPost("/oauth/ws/data2", "{}");

                assertFalse(response.getStatus() == Http.Status.OK);
                assertFalse(response.getBody().contains("ws_data"));
            }
        });
    }

}
