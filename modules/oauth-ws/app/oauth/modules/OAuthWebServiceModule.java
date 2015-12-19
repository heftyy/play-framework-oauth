package oauth.modules;

import be.objectify.deadbolt.java.cache.HandlerCache;
import com.google.inject.AbstractModule;
import oauth.webservice.AccessorsContainer;
import oauth.webservice.AccessorsContainerWithMD5;
import oauth.webservice.scopes.ScopesContainer;
import oauth.webservice.scopes.ScopesContainerImpl;
import security.MyHandlerCache;

public class OAuthWebServiceModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ScopesContainer.class).to(ScopesContainerImpl.class);
        bind(HandlerCache.class).to(MyHandlerCache.class);
        bind(AccessorsContainer.class).to(AccessorsContainerWithMD5.class);
    }
}
