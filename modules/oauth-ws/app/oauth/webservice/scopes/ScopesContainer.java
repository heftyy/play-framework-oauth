package oauth.webservice.scopes;

import oauth.webservice.Accessor;

import java.util.List;

public interface ScopesContainer {
    /**
     * Checks if client is allowed to access a certain scope.
     *
     * @param accessor ValidAccessor: Accessor object containing client's data.
     * @param scope    String: Scope asked for.
     * @return Boolean: True if client is allowed, false if not.
     */
    @Deprecated
    boolean checkIfClientAllowed(Accessor accessor, String scope);

    void addScope(String name, String description);

    /**
     * Add a url pattern to scope. Scope name has to match an existing scope.
     *
     * @param scopeName   String: Name of the scope to add the scope to.
     * @param urlPattern         String: Scope url without the domain (example: /user/add).
     * @param description String: Description of the scope.
     * @param method      String: HTTP method used to access the scope (GET/POST/.. etc).
     * @param returns     String: What to expect after accessing the scope ( xml/json/text/.. etc).
     */
    void addUrlPattern(String scopeName, String urlPattern, String description, String method, String returns);

    /**
     * Removes the scope from a certain scope.
     *
     * @param scopeName String: Scope's name.
     * @param urlPattern       String: url pattern.
     * @return Boolean: True if scope was found and removed, false if not.
     */
    boolean removeUrlPattern(String scopeName, String urlPattern);

    /**
     * Removes the scope and clears the list of the url patterns for it.
     *
     * @param scopeName String: Scope's name.
     * @return Boolean: True if scope was found and removed, false if not.
     */
    boolean removeScope(String scopeName);

    void setScopes(List<Scope> scopes);
    List<Scope> getScopes();

    boolean isReady();
    void setReady(boolean ready);
}
