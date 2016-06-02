package oauth.services;

import common.repository.Repository;
import oauth.accessor.AccessToken;
import oauth.models.OAuthWS;
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
    private Repository<OAuthWS> wsRepository;
    private ScopesService scopesService;

    @Inject
    public TokenServiceWithMemory(Repository<OAuthWS> wsRepository, ScopesService scopesService) {
        this.wsRepository = wsRepository;
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
                    accessTokens.remove(getMapKey(accessToken.getAccessorId(), accessToken.getWs().getDomain()));
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
                accessToken.getWs().getDomain().equals(domain)) {
            return accessToken;
        } else {
            accessTokens.remove(getMapKey(accessorId, domain));
            return null;
        }
    }

    @Override
    public boolean validateScopeRequest(String accessorId, String domain, String scopeRequested) {
        Set<String> scopes = scopesService.getPatternsFor(accessorId, domain);

        if(scopes == null || scopes.size() == 0) {
            play.Logger.error("Didn't find any scopes available for accessor %s on %s", accessorId, domain);
        }

        for(String scope : scopes) {
            if(ScopesStringCompare.compareStringsRegex(scopeRequested, scope)) return true;
        }

        return false;
    }

    @Override
    public AccessToken createAccessToken(String accessorId, Long time, String scope, String domain) {
        AccessToken accessToken = getAccessToken(accessorId, domain);
        if(accessToken != null) return accessToken;

        OAuthWS ws = wsRepository.findByField("domain", domain);
        if(ws == null) return null;

        accessToken = new AccessToken(
                accessorId,
                UUID.randomUUID().toString(),
                time + AccessToken.TOKEN_VALID_FOR_MILLISECONDS,
                TOKEN_TYPE,
                ws);

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
