package oauth.modules;

import com.google.inject.AbstractModule;
import oauth.services.GenerateKeyService;
import oauth.services.GeneratePkcsKeyService;

public class OAuthConsoleModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(GenerateKeyService.class).to(GeneratePkcsKeyService.class);
    }
}
