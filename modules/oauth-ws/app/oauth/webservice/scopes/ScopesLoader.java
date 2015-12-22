package oauth.webservice.scopes;

public interface ScopesLoader {
    /**
     * Loads the settings file with scopes and url patterns. Checks the project
     * default directory.
     *
     * @param fileName String: Name of the file with extension (example:
     *                 settings.xml, scopes.json).
     */
    void load(String fileName);
}
