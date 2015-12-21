import common.json.JsonSetup;
import oauth.webservice.scopes.ScopesXmlLoader;
import play.Application;
import play.GlobalSettings;
import play.Play;

/**
 * Application wide behaviour. We establish a Spring application context for the dependency injection system and
 * configure Spring Data.
 */
public class Global extends GlobalSettings {

    /**
     * Sync the context lifecycle with Play's.
     */
    @Override
    public void onStart(final Application app) {
        super.onStart(app);
        JsonSetup.setup();
        ScopesXmlLoader scopesXmlLoader = Play.application().injector().instanceOf(ScopesXmlLoader.class);
        scopesXmlLoader.loadXMLProperties("scopes.xml");
    }

    /**
     * Sync the context lifecycle with Play's.
     */
    @Override
    public void onStop(final Application app) {
        super.onStop(app);
    }
}