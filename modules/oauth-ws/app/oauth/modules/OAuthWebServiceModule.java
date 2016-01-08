package oauth.modules;

import be.objectify.deadbolt.java.cache.HandlerCache;
import com.google.inject.AbstractModule;
import oauth.lifecycle.OnWsStart;
import oauth.webservice.AccessorsService;
import oauth.webservice.AccessorsServiceWithOAuth;
import oauth.webservice.scopes.ScopesContainer;
import oauth.webservice.scopes.ScopesContainerImpl;
import oauth.webservice.scopes.ScopesLoader;
import oauth.webservice.scopes.ScopesLoaderJson;
import security.MyHandlerCache;

public class OAuthWebServiceModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(OnWsStart.class).asEagerSingleton();

        bind(ScopesContainer.class).to(ScopesContainerImpl.class);
        bind(HandlerCache.class).to(MyHandlerCache.class);
        bind(AccessorsService.class).to(AccessorsServiceWithOAuth.class);
        bind(ScopesLoader.class).to(ScopesLoaderJson.class);
    }
}
