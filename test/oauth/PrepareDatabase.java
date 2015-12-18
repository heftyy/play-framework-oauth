package oauth;

import com.google.common.collect.Sets;
import oauth.models.OAuthApi;
import oauth.models.OAuthClient;
import play.db.jpa.JPA;

public class PrepareDatabase {

    public static void prepareDatabase() {
        JPA.withTransaction(() -> {
            OAuthClient client = ClientTest.getClientWithKey();
            OAuthApi api = ApiTest.getApiWithAllScope();

            client.setApis(Sets.newHashSet(api));
            client.setLevels(api.getLevels());

            JPA.em().persist(client);
        });
    }

}
