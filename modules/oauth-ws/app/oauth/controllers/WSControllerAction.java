package oauth.controllers;

import oauth.webservice.Accessor;
import oauth.webservice.AccessorsServiceWithMD5;
import oauth.webservice.AccessorsService;
import oauth.webservice.scopes.ScopesContainer;
import play.libs.F;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import java.security.MessageDigest;
import java.util.Optional;

public class WSControllerAction extends Action<WSController> {

    private final ScopesContainer scopesContainer;
    private final AccessorsService accessorsService;

    @Inject
    public WSControllerAction(AccessorsService accessorsService, ScopesContainer scopesContainer) {
        this.accessorsService = accessorsService;
        this.scopesContainer = scopesContainer;
    }

    /**
     * This method is called every time a request comes to the webservice. If scopes have been
     * declared in .xml properties file then the method assumes deadbolt is preset as
     * well and doesnt do anything. If scopes were not declared then this method validates the request.
     *
     * @param context Http.Context
     */
    public play.libs.F.Promise<Result> call(Http.Context context) throws Throwable {
        if (scopesContainer.isScopesLoaded()) {
            return delegate.call(context);
        } else {
            String[] authorizations = context.request().headers().get("Authorization");

            if(authorizations == null || authorizations.length == 0) return delegate.call(context);

            String authorization = authorizations[0];
            if(authorization.contains(" ")) {
                String type = authorization.split(" ")[0];
                String token = authorization.split(" ")[1];

                Accessor accessor = accessorsService.validateToken(token, "url");
            }
            return delegate.call(context);

        }
    }
}
