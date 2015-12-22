package oauth.helper;

import com.google.common.collect.Sets;
import oauth.console.ApiTest;
import oauth.console.ClientTest;
import oauth.models.OAuthApi;
import oauth.models.OAuthClient;
import play.db.jpa.JPA;

public class DatabaseHelper {

    public static void prepareDatabase() {
        JPA.withTransaction(() -> {
            OAuthClient client = ClientTest.getClientWithKey();
            OAuthApi api = ApiTest.getApiWithAllScope();

            client.setApis(Sets.newHashSet(api));
            client.setScopes(api.getScopes());

            JPA.em().persist(client);
        });
    }

}
