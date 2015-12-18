package security;

import be.objectify.deadbolt.java.DeadboltHandler;
import be.objectify.deadbolt.java.cache.HandlerCache;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class MyHandlerCache implements HandlerCache {
    private final DeadboltHandler defaultHandler = new MyDeadboltHandler();
    private final Map<String, DeadboltHandler> handlers = new HashMap<>();

    private final Provider<OAuthHandler> oAuthHandlerProvider;

    @Inject
    public MyHandlerCache(Provider<OAuthHandler> oAuthHandlerProvider) {
        this.oAuthHandlerProvider = oAuthHandlerProvider;
        handlers.put(HandlerKeys.DEFAULT.key, defaultHandler);
        handlers.put(HandlerKeys.OAUTH.key, oAuthHandlerProvider.get());
    }

    @Override
    public DeadboltHandler apply(final String key) {
        return handlers.get(key);
    }

    @Override
    public DeadboltHandler get() {
        return defaultHandler;
    }
}