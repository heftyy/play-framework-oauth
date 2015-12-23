package oauth.lifecycle;

import common.json.JsonSetup;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class OnStart {

    @Inject
    public OnStart() {
        JsonSetup.setup();
    }
}
