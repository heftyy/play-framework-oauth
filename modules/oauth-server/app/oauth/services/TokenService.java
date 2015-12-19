package oauth.services;

import com.google.common.collect.Lists;
import common.repository.Repository;
import oauth.accessor.AccessToken;
import oauth.models.OAuthScope;
import oauth.utils.ScopesStringCompare;
import org.hibernate.criterion.Restrictions;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;

@Singleton
public class TokenService {

    private static int TOKEN_VALID_FOR_MILLISECONDS = 15 * 60 * 1000; // in milliseconds ( 15 minutes )

    private Map<String, AccessToken> accessTokens = new HashMap<>();
    private Repository<OAuthScope> scopeRepository;

    @Inject
    public TokenService(Repository<OAuthScope> scopeRepository) {
        this.scopeRepository = scopeRepository;
    }

    /**
     * Check if access token exists in memory.
     *
     * @param accessorId    String: Client's UUID.
     * @param remoteAddress String: Client's IP.
     * @return Boolean: True if found, false if not.
     */
    private boolean checkAccessTokenExists(String accessorId,
                                           String remoteAddress) {
        AccessToken accessToken = accessTokens.get(accessorId);

        return accessToken != null &&
                accessToken.getTokenExpiresAt() > System.currentTimeMillis() &&
                accessToken.getRemoteAddress().equals(remoteAddress);
    }

    /**
     * Validate the request sent by client, check if the client has access to
     * the requested scope. Compares the scopes using regex.
     *
     * @param accessorId String: Client's UUID.
     * @param domain     String: Webservice's domain.
     * @param scopeRequested String: scopeRequested asked for.
     * @return Boolean: True if request is valid, false if not.
     */
    public boolean validateScopeRequest(String accessorId, String domain, String scopeRequested) {

        List<OAuthScope> scopes = scopeRepository.findWithRestrictions(Lists.newArrayList(
                Restrictions.eq("clients.accessorId", accessorId),
                Restrictions.eq("api.domain", domain)
        ), "level", "level.api", "level.clients");

        try {
            for (OAuthScope scopeAllowed : scopes) {
                if (ScopesStringCompare.compareStringsRegex(scopeRequested, scopeAllowed.getScopeUrl())) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            play.Logger.info("Failed when validating the request");
            return false;
        }
    }

    /**
     * Attempts to create a new access token for client in the memory. Checks if the
     * data is valid and if the access token doesnt exist already. Also inserts all
     * the data into the database for statistics generation.
     *
     * @param accessorId    String: Client's UUID.
     * @param time          long: Access token's expire time.
     * @param scope         String: scope asked for.
     * @param remoteAddress String: Client's IP.
     * @param domain        String: Webservice's domain.
     * @return String: JSON.toString() that is going to be sent back to the
     * client.
     */
    public AccessToken createNewAccessor(String accessorId, Long time, String scope,
                                                String remoteAddress, String domain) {
        if (accessorId == null || scope == null || remoteAddress == null ||
                checkAccessTokenExists(accessorId, remoteAddress)) return null;

        UUID accessToken = UUID.randomUUID();
        AccessToken newAccessToken = new AccessToken(accessorId,
                accessToken.toString(), scope, remoteAddress, time
                + TOKEN_VALID_FOR_MILLISECONDS, 0);

        accessTokens.put(accessorId, newAccessToken);

        return newAccessToken;
    }

    /**
     * Validate the request sent by the webservice after client tried to
     * authorize at the webservice. IP is not compared because it may vary,
     * client can send a request to oauth and it saves client's outside IP and
     * webservice may get a local IP for a example.
     *
     * @param accessorId String: Client's accessorId.
     * @param token String: Access token.
     * @param scope String: scope.
     * @param remoteAddress String: Client's IP.
     * @return ValidAccessor: If the method finds a valid access token returns it otherwise null.
     */
    public AccessToken validateAccessToken(String accessorId, String token, String scope, String remoteAddress) {
        AccessToken accessToken = accessTokens.get(accessorId);

        if(accessToken != null &&
                accessToken.getTokenExpiresAt() > System.currentTimeMillis() &&
                accessToken.getAccessToken().equals(token) &&
//                accessToken.getRemoteAddress().equals(remoteAddress) &&
                accessToken.getScope().equals(scope))
            return accessToken;

        return null;
    }
}
