package oauth.lifecycle;

import oauth.webservice.scopes.ScopesLoader;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class OnWsStart {

    @Inject
    public OnWsStart(ScopesLoader scopesLoader) {
        scopesLoader.load("scopes.json");
    }
}
