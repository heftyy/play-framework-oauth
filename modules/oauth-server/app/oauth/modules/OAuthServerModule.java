package oauth.modules;

import com.google.inject.AbstractModule;
import oauth.services.JwtServiceImpl;
import oauth.services.JwtService;

public class OAuthServerModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(JwtService.class).to(JwtServiceImpl.class);
    }
}
