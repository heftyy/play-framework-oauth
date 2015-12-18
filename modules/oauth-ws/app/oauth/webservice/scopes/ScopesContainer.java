package oauth.webservice.scopes;

import oauth.webservice.ValidAccessor;

import java.util.List;

public interface ScopesContainer {
    /**
     * Checks if client is allowed to access a certain scope.
     *
     * @param accessor ValidAccessor: Accessor object containing client's data.
     * @param scope    String: Scope asked for.
     * @return Boolean: True if client is allowed, false if not.
     */
    boolean checkIfClientAllowed(ValidAccessor accessor, String scope);

    /**
     * Searches through all the levels and scopes looking for the correct scope.
     * Adds found levels to a list.
     *
     * @param requestedScope String: Scope asked for.
     * @return List<String>: A list with all the level names which contained the scope asked for.
     */
    List<String> findLevelsForScope(String requestedScope);

    void addLevel(String name, String description);

    /**
     * Add a scope to a level. Level name has to match existing level.
     *
     * @param levelName   String: Name of the level to add the scope to.
     * @param url         String: Scope url without the domain (example: /user/add).
     * @param description String: Description of the scope.
     * @param method      String: HTTP method used to access the scope (GET/POST/.. etc).
     * @param returns     String: What to expect after accessing the scope ( xml/json/text/.. etc).
     */
    void addScope(String levelName, String url, String description, String method, String returns);

    /**
     * Removes the scope from a certain level.
     *
     * @param levelName String: Level's name.
     * @param url       String: Scope's url.
     * @return Boolean: True if scope was found and removed, false if not.
     */
    boolean removeScope(String levelName, String url) ;

    /**
     * Removes the level and clears the list of the scopes for it.
     *
     * @param levelName String: Level's name.
     * @return Boolean: True if level was found and removed, false if not.
     */
    boolean removeLevel(String levelName);

    List<Level> getLevels();
    boolean isScopesLoaded();
    void setScopesLoaded(boolean scopesLoaded);

}
