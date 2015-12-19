package oauth.webservice;

import oauth.webservice.scopes.ScopesContainer;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;

import javax.inject.Inject;
import java.util.List;

public class PropertiesLoader {

    private final ScopesContainer scopesContainer;

    @Inject
    public PropertiesLoader(ScopesContainer scopesContainer) {
        this.scopesContainer = scopesContainer;
    }

    /**
     * Loads the .xml settings with levels and scopes. Checks the project
     * default directory.
     *
     * @param fileName String: Name of the file with extension (example:
     *                 settings.xml).
     * @return Boolean: True if loaded properly, false if not.
     */
    @SuppressWarnings("unchecked")
    public boolean loadXMLProperties(String fileName) {
        try {
            XMLConfiguration config = new XMLConfiguration(fileName);

            int i = 0;

            List<HierarchicalConfiguration> levelFields = config
                    .configurationsAt("levels.level");

            for (HierarchicalConfiguration sub : levelFields) {

                String levelName = (String) sub.getProperty("name");
                String levelDesc = (String) sub.getProperty("desc");
                scopesContainer.addLevel(levelName, levelDesc);
                // System.out.println(name);
                List<HierarchicalConfiguration> scopeFields = config.configurationsAt("levels.level(" + i + ").scope");

                for (HierarchicalConfiguration scopeSub : scopeFields) {

                    String scopeUrl = null;
                    Object scopeUrlObject = scopeSub.getProperty("url");

                    if (scopeUrlObject instanceof String) {

                        scopeUrl = (String) scopeUrlObject;

                    } else if (scopeUrlObject instanceof List<?>) {

                        StringBuilder strBuilder = new StringBuilder();

                        for (String elem : (List<String>) scopeUrlObject) {
                            strBuilder.append(elem);
                            strBuilder.append(',');
                        }

                        strBuilder.setLength(strBuilder.length() - 1);
                        scopeUrl = strBuilder.toString();
                    }

                    String scopeDesc = (String) scopeSub.getProperty("desc");
                    String scopeMethod = (String) scopeSub.getProperty("method");
                    String scopeReturns = (String) scopeSub.getProperty("returns");

                    scopesContainer.addScope(levelName, scopeUrl, scopeDesc, scopeMethod, scopeReturns);

                    System.out.println(scopeUrl + "," + scopeDesc + "," + scopeMethod + "," + scopeReturns);

                }
                i++;
            }
            scopesContainer.setScopesLoaded(true);
        } catch (Exception e) {
            // dev / prod

            scopesContainer.addLevel("ALL", "no levels declared");
            scopesContainer.addScope("ALL", "/*", "*", "*", "*");
            scopesContainer.setScopesLoaded(false);

            play.Logger.error("Couldn't load the config xml");
            return false;
        }
        return true;
    }
}
