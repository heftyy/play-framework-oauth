package oauth.lifecycle;

import common.json.JsonSetup;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class OnBaseStart {

    @Inject
    public OnBaseStart() {
        JsonSetup.setup();
    }
}
