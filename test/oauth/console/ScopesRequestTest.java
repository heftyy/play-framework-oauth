package oauth.console;

import com.google.common.collect.Lists;
import oauth.helper.RepositoryHelper;
import oauth.models.OAuthWS;
import oauth.models.OAuthScope;
import oauth.models.OAuthUrlPattern;
import oauth.services.ScopesRequestService;
import org.hibernate.criterion.Restrictions;
import org.junit.Test;
import play.Play;
import play.db.jpa.JPA;
import test.GenericFakeAppTest;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertTrue;
import static play.test.Helpers.running;
import static play.test.Helpers.testServer;

public class ScopesRequestTest extends GenericFakeAppTest {

    @Test
    public void scopesRequestSuccess() {
        running(testServer(9000, fakeApp), () -> {
            RepositoryHelper helper = Play.application().injector().instanceOf(RepositoryHelper.class);

            JPA.withTransaction(() -> {
                OAuthWS ws = new OAuthWS();
                ws.setName("test-api");
                ws.setDomain("localhost:9000");
                ws.setEnabled(true);
                ws.setScopeRequestUrl("/oauth/ws/scopes");

                JPA.em().persist(ws);

                ScopesRequestService requestService = Play.application().injector().instanceOf(ScopesRequestService.class);
                List<OAuthScope> scopes = requestService.getScopes(1L);

                assertTrue(scopes.size() == 1);
                assertTrue(scopes.get(0).getName().equals("test1"));

                Set<OAuthUrlPattern> urls = scopes.get(0).getUrlPatterns();
                assertTrue(urls.size() == 2);

                Iterator<OAuthUrlPattern> iter = urls.iterator();

                String url1 = iter.next().getPattern();
                String url2 = iter.next().getPattern();

                // could be any order so check all possibilities
                assertTrue(url1.equals("/oauth/ws/data1") || url1.equals("/test"));
                assertTrue(url2.equals("/oauth/ws/data1") || url2.equals("/test"));
            });
        });
    }

    @Test
    public void scopesRequestFail() {
        running(testServer(9000, fakeApp), () -> {
            RepositoryHelper helper = Play.application().injector().instanceOf(RepositoryHelper.class);

            JPA.withTransaction(() -> {
                ScopesRequestService requestService = Play.application().injector().instanceOf(ScopesRequestService.class);
                List<OAuthScope> scopes = requestService.getScopes(-1L);

                assertTrue(scopes.size() == 1);
                assertTrue(scopes.get(0).getName().equals("ALL"));

                Set<OAuthUrlPattern> urls = scopes.get(0).getUrlPatterns();
                assertTrue(urls.size() == 1);

                Iterator<OAuthUrlPattern> iter = urls.iterator();

                String url1 = iter.next().getPattern();

                // could be any order so check all possibilities
                assertTrue(url1.equals("/*"));
            });
        });
    }
}
