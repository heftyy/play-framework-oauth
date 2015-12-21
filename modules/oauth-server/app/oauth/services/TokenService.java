package oauth.services;

import oauth.accessor.AccessToken;

public interface TokenService {
    AccessToken getAccessToken(String token);
    AccessToken getAccessToken(String accessorId, String domain);

    /**
     * Validate the request sent by client, check if the client has access to
     * the requested scope. Compares the scopes using regex.
     *
     * @param accessorId String: Client's UUID.
     * @param domain     String: Webservice's domain.
     * @param scopeRequested String: scopeRequested asked for.
     * @return Boolean: True if request is valid, false if not.
     */
    boolean validateScopeRequest(String accessorId, String domain, String scopeRequested);

    /**
     * Attempts to create a new access token for client. Checks if the
     * data is valid and if the access token doesnt exist already
     *
     * @param accessorId    String: Client's accessorId.
     * @param time          long: Access token's expire time.
     * @param scope         String: scope asked for.
     * @param domain        String: Webservice's domain.
     * @return the access token
     */
    AccessToken createAccessToken(String accessorId, Long time, String scope, String domain);

    AccessToken validateAccessToken(String token, String requestedScope, String domain);
}
