import common.json.JsonSetup;
import oauth.services.ScopesService;
import oauth.utils.ScopesStringCompare;
import oauth.webservice.PropertiesLoader;
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
        PropertiesLoader propertiesLoader = Play.application().injector().instanceOf(PropertiesLoader.class);
        propertiesLoader.loadXMLProperties("scopes.xml");
    }

    /**
     * Sync the context lifecycle with Play's.
     */
    @Override
    public void onStop(final Application app) {
        super.onStop(app);
    }
}