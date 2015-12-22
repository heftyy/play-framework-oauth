package oauth.helper;

import common.repository.Repository;
import oauth.models.OAuthApi;
import oauth.models.OAuthClient;
import oauth.models.OAuthScope;
import oauth.models.OAuthUrlPattern;

import javax.inject.Inject;

/**
 * Helper class that holds all the injected repositories.
 */
public class RepositoryHelper {
    public final Repository<OAuthClient> clientRepository;
    public final Repository<OAuthApi> apiRepository;
    public final Repository<OAuthUrlPattern> urlPatternRepository;
    public final Repository<OAuthScope> scopeRepository;

    @Inject
    public RepositoryHelper(Repository<OAuthClient> clientRepository, Repository<OAuthApi> apiRepository, Repository<OAuthUrlPattern> urlPatternRepository, Repository<OAuthScope> scopeRepository) {
        this.clientRepository = clientRepository;
        this.apiRepository = apiRepository;
        this.urlPatternRepository = urlPatternRepository;
        this.scopeRepository = scopeRepository;
    }
}
