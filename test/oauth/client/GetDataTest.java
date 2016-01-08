package oauth.client;

import oauth.helper.DatabaseHelper;
import oauth.helper.RepositoryHelper;
import oauth.messages.AccessTokenSuccess;
import oauth.models.OAuthClient;
import oauth.services.GenerateKeyService;
import org.junit.Test;
import play.Play;
import play.db.jpa.JPA;
import play.libs.F;
import play.libs.ws.WS;
import play.libs.ws.WSResponse;
import play.mvc.Http;
import test.GenericFakeAppTest;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.running;
import static play.test.Helpers.testServer;

public class GetDataTest extends GenericFakeAppTest {

    @Test
    public void clientGetTest() {
        running(testServer(9000, fakeApp), () -> {
            DatabaseHelper.prepareDatabase();

            String domain = "localhost:9000";

            RepositoryHelper repositoryHelper = Play.application().injector().instanceOf(RepositoryHelper.class);
            GenerateKeyService keyService = Play.application().injector().instanceOf(GenerateKeyService.class);

            JPA.withTransaction(() -> {
                OAuthClient client = repositoryHelper.clientRepository.findByField("id", 1L, "webServices", "scopes", "scopes.urlPatterns");

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
        });
    }

    @Test
    public void clientPostTest() {
        running(testServer(9000, fakeApp), () -> {
            DatabaseHelper.prepareDatabase();

            String domain = "localhost:9000";

            RepositoryHelper repositoryHelper = Play.application().injector().instanceOf(RepositoryHelper.class);
            GenerateKeyService keyService = Play.application().injector().instanceOf(GenerateKeyService.class);

            JPA.withTransaction(() -> {
                OAuthClient client = repositoryHelper.clientRepository.findByField("id", 1L, "webServices", "scopes", "scopes.urlPatterns");

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
        });
    }

}
