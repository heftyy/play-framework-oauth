package oauth.services;

import common.repository.Repository;
import oauth.accessor.AccessToken;
import oauth.models.OAuthApi;
import oauth.utils.ScopesStringCompare;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Singleton
public class TokenServiceWithMemory implements TokenService {
    private static String TOKEN_TYPE = "Bearer";

    private Map<String, AccessToken> accessTokens = new HashMap<>();
    private Repository<OAuthApi> apiRepository;
    private ScopesService scopesService;

    @Inject
    public TokenServiceWithMemory(Repository<OAuthApi> apiRepository, ScopesService scopesService) {
        this.apiRepository = apiRepository;
        this.scopesService = scopesService;
    }

    private String getMapKey(String accessorId, String domain) {
        return accessorId + domain;
    }

    @Override
    public AccessToken getAccessToken(String token) {
        for(AccessToken accessToken : accessTokens.values()) {
            if(accessToken.getToken().equals(token)) {
                if(accessToken.getExpiresAt() > System.currentTimeMillis()) {
                    return accessToken;
                } else { // remove outdated tokens
                    accessTokens.remove(getMapKey(accessToken.getAccessorId(), accessToken.getApi().getDomain()));
                }
            }
        }

        return null;
    }

    @Override
    public AccessToken getAccessToken(String accessorId, String domain) {
        AccessToken accessToken = accessTokens.get(getMapKey(accessorId, domain));

        if(accessToken != null &&
                accessToken.getExpiresAt() > System.currentTimeMillis() &&
                accessToken.getApi().getDomain().equals(domain)) {
            return accessToken;
        } else {
            accessTokens.remove(getMapKey(accessorId, domain));
            return null;
        }
    }

    @Override
    public boolean validateScopeRequest(String accessorId, String domain, String scopeRequested) {
        Set<String> scopes = scopesService.getPatternsFor(accessorId, domain);

        for(String scope : scopes) {
            if(ScopesStringCompare.compareStringsRegex(scopeRequested, scope)) return true;
        }

        return false;
    }

    @Override
    public AccessToken createAccessToken(String accessorId, Long time, String scope, String domain) {
        AccessToken accessToken = getAccessToken(accessorId, domain);
        if(accessToken != null) return accessToken;

        OAuthApi api = apiRepository.findByField("domain", domain);
        if(api == null) return null;

        accessToken = new AccessToken(
                accessorId,
                UUID.randomUUID().toString(),
                time + AccessToken.TOKEN_VALID_FOR_MILLISECONDS,
                TOKEN_TYPE,
                api);

        accessTokens.put(getMapKey(accessorId, domain), accessToken);

        return accessToken;
    }

    @Override
    public AccessToken validateAccessToken(String token, String requestedScope, String domain) {
        AccessToken accessToken = getAccessToken(token);

        if(accessToken == null) return null;

        if(accessToken.getExpiresAt() > System.currentTimeMillis() &&
                accessToken.getToken().equals(token) &&
                validateScopeRequest(accessToken.getAccessorId(), domain, requestedScope)) {
            return accessToken;
        }

        return null;
    }
}
