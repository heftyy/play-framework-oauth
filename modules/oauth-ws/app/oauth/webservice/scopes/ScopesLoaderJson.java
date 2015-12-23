package oauth.webservice.scopes;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import oauth.models.OAuthScope;
import oauth.models.OAuthUrlPattern;
import play.libs.Json;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ScopesLoaderJson implements ScopesLoader {
    private final ScopesContainer scopesContainer;

    @Inject
    public ScopesLoaderJson(ScopesContainer scopesContainer) {
        this.scopesContainer = scopesContainer;
    }

    public void load(String fileName) {
        try {
            String content = Files.toString(new File(fileName), Charsets.UTF_8);
            List<Scope> scopes = Arrays.asList(Json.fromJson(Json.parse(content), Scope[].class));

            scopesContainer.setScopes(scopes);
            scopesContainer.setReady(true);
        } catch (IOException e) {
            play.Logger.error("Couldn't load the scopes config file.", e.getMessage());

            scopesContainer.addScope("ALL", "no scopes declared");
            scopesContainer.addUrlPattern("ALL", "/*", "*", "*", "*");
            scopesContainer.setReady(false);
        }
    }
}
