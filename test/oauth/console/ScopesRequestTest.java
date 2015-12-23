package oauth.console;

import com.google.common.collect.Lists;
import oauth.helper.DatabaseHelper;
import oauth.helper.RepositoryHelper;
import oauth.models.OAuthApi;
import oauth.models.OAuthScope;
import oauth.models.OAuthUrlPattern;
import oauth.services.ScopesRequestService;
import org.hibernate.criterion.Restrictions;
import org.junit.Test;
import play.Play;
import play.db.jpa.JPA;
import test.GenericFakeAppTest;

import java.util.List;

import static org.junit.Assert.assertTrue;
import static play.test.Helpers.running;
import static play.test.Helpers.testServer;

public class ScopesRequestTest extends GenericFakeAppTest {

    @Test
    public void scopesRequestSuccess() {
        running(testServer(9000, fakeApp), () -> {
            RepositoryHelper helper = Play.application().injector().instanceOf(RepositoryHelper.class);

            JPA.withTransaction(() -> {
                OAuthApi a = new OAuthApi();
                a.setName("test-api");
                a.setDomain("localhost:9000");
                a.setEnabled(true);
                a.setScopeRequestUrl("/oauth/ws/scopes");

                JPA.em().persist(a);

                ScopesRequestService requestService = Play.application().injector().instanceOf(ScopesRequestService.class);
                requestService.getScopesJson(1L);

                List<OAuthScope> scope = helper.scopeRepository.findWithRestrictions(Lists.newArrayList(
                        Restrictions.eq("api.id", 1L)
                ));
                assertTrue(scope.size() == 1);
                assertTrue(scope.get(0).getName().equals("test1"));

                List<OAuthUrlPattern> urls = helper.urlPatternRepository.findWithRestrictions(Lists.newArrayList(
                        Restrictions.eq("scope.api.id", 1L)
                ), "scope");
                assertTrue(urls.size() == 2);

                String url1 = urls.get(0).getPattern();
                String url2 = urls.get(1).getPattern();

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
                requestService.getScopesJson(2L);

                List<OAuthScope> scope = helper.scopeRepository.findWithRestrictions(Lists.newArrayList(
                        Restrictions.eq("api.id", 2L)
                ));
                assertTrue(scope.size() == 1);
                assertTrue(scope.get(0).getName().equals("ALL"));

                List<OAuthUrlPattern> urls = helper.urlPatternRepository.findWithRestrictions(Lists.newArrayList(
                        Restrictions.eq("scope.api.id", 2L)
                ), "scope");
                assertTrue(urls.size() == 1);

                String url1 = urls.get(0).getPattern();

                // could be any order so check all possibilities
                assertTrue(url1.equals("/*"));
            });
        });
    }
}
