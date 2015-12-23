package oauth.controllers;

import play.api.mvc.AnyContent;

import javax.inject.Inject;

public class Assets {

    private final controllers.Assets assets;

    @Inject
    public Assets(controllers.Assets assets) {
        this.assets = assets;
    }

    public play.api.mvc.Action<AnyContent> at(String path, String file) {
        return assets.at(path, file, false);
    }

    public play.api.mvc.Action<AnyContent> versioned(String path, controllers.Assets.Asset file) {
        return assets.versioned(path, file);
    }
}