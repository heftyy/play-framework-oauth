package security;

import be.objectify.deadbolt.core.models.Subject;
import be.objectify.deadbolt.java.AbstractDeadboltHandler;
import be.objectify.deadbolt.java.DynamicResourceHandler;
import oauth.webservice.AccessorsContainer;
import play.libs.F;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;

import javax.inject.Inject;
import java.security.MessageDigest;
import java.util.Optional;

public class OAuthHandler extends AbstractDeadboltHandler {

    private final AccessorsContainer accessorsContainer;

    @Inject
    public OAuthHandler(AccessorsContainer accessorsContainer) {
        this.accessorsContainer = accessorsContainer;
    }

    public F.Promise<Optional<Result>> beforeAuthCheck(final Http.Context context) {
        // returning null means that everything is OK.  Return a real result if you want a redirect to a login page or
        // somewhere else
        return F.Promise.promise(Optional::empty);
    }

    public F.Promise<Optional<Subject>> getSubject(final Http.Context context) {
        String accessorId = context.request().headers().get("ACCESSOR-ID")[0];
        String userAgent = context.request().headers().get("User-Agent")[0];
        String ip = context.request().remoteAddress();

        MessageDigest md = AccessorsContainer.createMD5Hash(accessorId, ip, userAgent);

        return F.Promise.promise(() -> Optional.ofNullable(accessorsContainer.findAccessor(accessorId, md)));
    }

    public F.Promise<Optional<DynamicResourceHandler>> getDynamicResourceHandler(final Http.Context context) {
        return null;
    }

    @Override
    public F.Promise<Result> onAuthFailure(final Http.Context context,
                                           final String content) {
        // you can return any result from here - forbidden, etc
        if (context.request().headers().get("X-Requested-With") != null && context.request().headers().get("X-Requested-With")[0].equals("XMLHttpRequest")) {
            return F.Promise.promise((F.Function0<Result>) () -> forbidden("redirect_login"));
        }
        return F.Promise.promise((F.Function0<Result>) Results::forbidden);
    }
}