package oauth.modules;

import com.google.inject.AbstractModule;
import oauth.services.JwtServiceImpl;
import oauth.services.JwtService;
import oauth.services.TokenService;
import oauth.services.TokenServiceWithMemory;

public class OAuthServerModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(JwtService.class).to(JwtServiceImpl.class);
        bind(TokenService.class).to(TokenServiceWithMemory.class);
    }
}
