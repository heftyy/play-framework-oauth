package oauth.ws;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Charsets;
import com.google.common.collect.Sets;
import com.google.common.io.Files;
import common.json.JsonSetup;
import oauth.models.OAuthScope;
import oauth.modules.OAuthBaseModule;
import oauth.modules.OAuthServerModule;
import oauth.modules.OAuthWebServiceModule;
import oauth.webservice.scopes.ScopesContainer;
import oauth.webservice.scopes.ScopesLoader;
import org.junit.Test;
import play.inject.Injector;
import play.inject.guice.GuiceInjectorBuilder;
import play.libs.Json;
import test.GenericFakeAppTest;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ScopesTest extends GenericFakeAppTest {

    Injector injector = new GuiceInjectorBuilder()
            .bindings(new OAuthBaseModule())
            .bindings(new OAuthWebServiceModule())
            .bindings(new OAuthServerModule())
            .injector();

    public static Set<OAuthScope> readScopesFile(String fileName) {
        String scopes = null;
        try {
            scopes = Files.toString(new File(fileName), Charsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        JsonNode jn = Json.parse(scopes);

        return Sets.newHashSet(Json.fromJson(jn, OAuthScope[].class));
    }

    @Test
    public void loadScopes() {
        JsonSetup.setup();

        ScopesLoader scopesLoader = injector.instanceOf(ScopesLoader.class);
        scopesLoader.load("test/scopes.json");

        ScopesContainer scopesContainer = injector.instanceOf(ScopesContainer.class);

        assertTrue(scopesContainer.isReady());
        assertTrue(scopesContainer.getScopes().size() == 1);
        assertTrue(scopesContainer.getScopes().get(0).getName().equals("test1"));
    }

    @Test
    public void defaultScopes() {
        ScopesLoader scopesLoader = injector.instanceOf(ScopesLoader.class);
        scopesLoader.load("test/not_scopes.json");

        ScopesContainer scopesContainer = injector.instanceOf(ScopesContainer.class);

        assertFalse(scopesContainer.isReady());
        assertTrue(scopesContainer.getScopes().size() == 1);
        assertTrue(scopesContainer.getScopes().get(0).getName().equals("ALL"));
    }

    @Test
    public void serializationTest() {
        JsonSetup.setup();

        Set<OAuthScope> scopes = readScopesFile("test/scopes.json");
        assertTrue(scopes.size() == 1);
        assertTrue(scopes.iterator().next().getUrlPatterns().size() == 2);
    }

}
