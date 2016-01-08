package oauth.helper;

import com.google.common.collect.Sets;
import oauth.console.ApiTest;
import oauth.console.ClientTest;
import oauth.models.OAuthWS;
import oauth.models.OAuthClient;
import play.db.jpa.JPA;

public class DatabaseHelper {

    public static void prepareDatabase() {
        JPA.withTransaction(() -> {
            OAuthClient client = ClientTest.getClientWithKey();
            OAuthWS ws = ApiTest.getApiWithAllScope();

            client.setWebServices(Sets.newHashSet(ws));
            client.setScopes(ws.getScopes());

            JPA.em().persist(client);
        });
    }

}
