package oauth;

import common.repository.Repository;
import oauth.models.OAuthApi;
import oauth.models.OAuthClient;
import oauth.models.OAuthLevel;
import oauth.models.OAuthScope;
import oauth.services.GenerateKeyService;
import oauth.services.JwtService;
import oauth.services.TokenService;
import oauth.webservice.AccessorsService;

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
    public final JwtService jwtService;
    public final TokenService tokenService;
    public final AccessorsService accessorsService;

    @Inject
    public TestServices(Repository<OAuthClient> clientRepository, Repository<OAuthApi> apiRepository, Repository<OAuthScope> scopeRepository, Repository<OAuthLevel> levelRepository, GenerateKeyService generateKeyService, JwtService jwtService, TokenService tokenService, AccessorsService accessorsService) {
        this.clientRepository = clientRepository;
        this.apiRepository = apiRepository;
        this.scopeRepository = scopeRepository;
        this.levelRepository = levelRepository;
        this.generateKeyService = generateKeyService;
        this.jwtService = jwtService;
        this.tokenService = tokenService;
        this.accessorsService = accessorsService;
    }
}
