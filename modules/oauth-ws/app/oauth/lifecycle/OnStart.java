package oauth.lifecycle;

import oauth.webservice.scopes.ScopesLoader;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class OnStart {

    @Inject
    public OnStart(ScopesLoader scopesLoader) {
        scopesLoader.load("scopes.json");
    }
}
