package security;

import be.objectify.deadbolt.core.models.Subject;
import be.objectify.deadbolt.java.AbstractDeadboltHandler;
import be.objectify.deadbolt.java.DynamicResourceHandler;
import oauth.webservice.AccessorsContainerWithMD5;
import oauth.webservice.AccessorsContainer;
import oauth.webservice.ValidAccessor;
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
        String[] accessorIdHeader = context.request().headers().get("ACCESSOR-ID");
        if(accessorIdHeader == null || accessorIdHeader.length == 0) return F.Promise.promise(Optional::empty);

        String accessorId = accessorIdHeader[0];
        String userAgent = context.request().headers().get("User-Agent")[0];
        String ip = context.request().remoteAddress();

        MessageDigest md = AccessorsContainerWithMD5.createMD5Hash(accessorId, ip, userAgent);

        ValidAccessor accessor = accessorsContainer.findAccessor(accessorId, md.digest());

        return F.Promise.promise(() -> Optional.ofNullable(accessor));
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