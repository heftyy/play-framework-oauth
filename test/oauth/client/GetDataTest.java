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
import static play.test.Helpers.running;
import static play.test.Helpers.testServer;

public class GetDataTest extends GenericFakeAppTest {

    @Test
    public void authorizeTest() {
        running(testServer(9000, fakeApp), () -> {
            DatabaseHelper.prepareDatabase();

            String domain = "localhost:9000";

            RepositoryHelper repositoryHelper = Play.application().injector().instanceOf(RepositoryHelper.class);
            GenerateKeyService keyService = Play.application().injector().instanceOf(GenerateKeyService.class);

            JPA.withTransaction(() -> {
                OAuthClient client = repositoryHelper.clientRepository.findByField("id", 1L, "apis", "scopes", "scopes.urlPatterns");
                AccessTokenSuccess token = GetAccessTokenTest.getAccessToken(client, domain, "/test", keyService.getSecretKeyPath(client.getAccessorId()));

                assertNotNull(token);

                { // should work
                    F.Promise<WSResponse> ret = WS.url("http://localhost:9000/oauth/ws/data1").
                            setHeader("Content-type", "application/json").
                            setHeader("Authorization", PlayWSOAuthClient.getAuthorizationHeader(token.getAccessToken())).
                            get();

                    WSResponse response = ret.get(100000);

                    assertTrue(response.getStatus() == Http.Status.OK);
                    assertTrue(response.getBody().contains("ws_data1"));
                }

                { // should fail
                    F.Promise<WSResponse> ret = WS.url("http://localhost:9000/oauth/ws/data2").
                            setHeader("Content-type", "application/json").
                            setHeader("Authorization", PlayWSOAuthClient.getAuthorizationHeader(token.getAccessToken())).
                            get();

                    WSResponse response = ret.get(100000);

                    assertFalse(response.getStatus() == Http.Status.OK);
                    assertFalse(response.getBody().contains("ws_data2"));
                }
            });
        });
    }
}
