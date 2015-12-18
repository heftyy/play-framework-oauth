import common.json.JsonSetup;
import play.Application;
import play.GlobalSettings;

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
    }

    /**
     * Sync the context lifecycle with Play's.
     */
    @Override
    public void onStop(final Application app) {
        super.onStop(app);
    }
}