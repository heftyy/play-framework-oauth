package oauth.webservice.scopes;

import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;

import javax.inject.Inject;
import java.util.List;

public class ScopesLoaderXml implements ScopesLoader {

    private final ScopesContainer scopesContainer;

    @Inject
    public ScopesLoaderXml(ScopesContainer scopesContainer) {
        this.scopesContainer = scopesContainer;
    }

    @SuppressWarnings("unchecked")
    public void load(String fileName) {
        try {
            XMLConfiguration config = new XMLConfiguration(fileName);

            int i = 0;

            List<HierarchicalConfiguration> scopeFields = config
                    .configurationsAt("scopes.scope");

            for (HierarchicalConfiguration sub : scopeFields) {

                String scopeName = (String) sub.getProperty("name");
                String scopeDesc = (String) sub.getProperty("desc");
                scopesContainer.addScope(scopeName, scopeDesc);
                // System.out.println(name);
                List<HierarchicalConfiguration> urlFields = config.configurationsAt("scopes.scope(" + i + ").urlPattern");

                for (HierarchicalConfiguration scopeSub : urlFields) {

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

                    String urlDesc = (String) scopeSub.getProperty("desc");
                    String urlMethod = (String) scopeSub.getProperty("method");
                    String urlReturns = (String) scopeSub.getProperty("returns");

                    scopesContainer.addUrlPattern(scopeName, scopeUrl, urlDesc, urlMethod, urlReturns);

                    System.out.println(scopeUrl + "," + urlDesc + "," + urlMethod + "," + urlReturns);

                }
                i++;
            }
            scopesContainer.setReady(true);
        } catch (Exception e) {
            // dev / prod

            scopesContainer.addScope("ALL", "no scopes declared");
            scopesContainer.addUrlPattern("ALL", "/*", "*", "*", "*");
            scopesContainer.setReady(false);

            play.Logger.error("Couldn't load the config xml");
        }
    }
}
