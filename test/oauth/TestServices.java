package oauth;

import common.repository.Repository;
import oauth.models.OAuthApi;
import oauth.models.OAuthClient;
import oauth.models.OAuthLevel;
import oauth.models.OAuthScope;
import oauth.services.GenerateKeyService;

import javax.inject.Inject;

/**
 * Helper class that holds all the injected services used by tests.
 */
public class TestServices {
    public final Repository<OAuthClient> clientRepository;
    public final Repository<OAuthApi> apiRepository;
    public final Repository<OAuthScope> scopeRepository;
    public final Repository<OAuthLevel> levelRepository;
    public final GenerateKeyService generateKeyService;

    @Inject
    public TestServices(Repository<OAuthClient> clientRepository, Repository<OAuthApi> apiRepository, Repository<OAuthScope> scopeRepository, Repository<OAuthLevel> levelRepository, GenerateKeyService generateKeyService) {
        this.clientRepository = clientRepository;
        this.apiRepository = apiRepository;
        this.scopeRepository = scopeRepository;
        this.levelRepository = levelRepository;
        this.generateKeyService = generateKeyService;
    }
}
