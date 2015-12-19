package oauth.controllers;

import oauth.webservice.AccessorsContainerWithMD5;
import oauth.webservice.AccessorsContainer;
import oauth.webservice.scopes.ScopesContainer;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import java.security.MessageDigest;

public class WSControllerAction extends Action<WSController> {

    private final ScopesContainer scopesContainer;
    private final AccessorsContainer accessorsContainer;

    @Inject
    public WSControllerAction(AccessorsContainer accessorsContainer, ScopesContainer scopesContainer) {
        this.accessorsContainer = accessorsContainer;
        this.scopesContainer = scopesContainer;
    }

    /**
     * This method is called every time a request comes to the webservice. If scopes have been
     * declared in .xml properties file then the method assumes deadbolt is preset as
     * well and doesnt do anything. If scopes were not declared then this method validates the request.
     *
     * @param ctx Http.Context
     */
    public play.libs.F.Promise<Result> call(Http.Context ctx) throws Throwable {       //FIXME was Result

        //If you specify scopes then you need to specify restriction

        if (scopesContainer.isScopesLoaded()) {
            return delegate.call(ctx);

        } else {

            String accessorId = null, userAgent = null, ip = null;
            MessageDigest md = null;

            try {

                accessorId = ctx.request().headers().get("ACCESSOR-ID")[0];
                userAgent = ctx.request().headers().get("USER-AGENT")[0];
                ip = ctx.request().remoteAddress();

                if (accessorId != null && userAgent != null && ip != null) {
                    md = AccessorsContainerWithMD5.createMD5Hash(accessorId, ip, userAgent);
                }


            } catch (NullPointerException e) {
                System.err.println("Not a valid request, missing some headers");
            }

            if (accessorsContainer.findAccessor(accessorId, md.digest()) == null) {
                System.err.println("Didn't authorize the request");
            }

            return delegate.call(ctx);

        }

    }

}
