package security;

import be.objectify.deadbolt.core.models.Subject;
import be.objectify.deadbolt.java.AbstractDeadboltHandler;
import be.objectify.deadbolt.java.DynamicResourceHandler;
import oauth.webservice.AccessorsService;
import oauth.webservice.Accessor;
import play.libs.F;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;

import javax.inject.Inject;
import java.util.Optional;

public class OAuthHandler extends AbstractDeadboltHandler {

    private final AccessorsService accessorsService;

    @Inject
    public OAuthHandler(AccessorsService accessorsService) {
        this.accessorsService = accessorsService;
    }

    public F.Promise<Optional<Result>> beforeAuthCheck(final Http.Context context) {
        // returning null means that everything is OK.  Return a real result if you want a redirect to a login page or
        // somewhere else
        return F.Promise.promise(Optional::empty);
    }

    public F.Promise<Optional<Subject>> getSubject(final Http.Context context) {
        String[] authorizations = context.request().headers().get("Authorization");
        if(authorizations == null || authorizations.length == 0) return F.Promise.promise(Optional::empty);

        String authorization = authorizations[0];
        if(authorization.contains(" ")) {
            String type = authorization.split(" ")[0];
            if(!type.equals("Bearer")) return F.Promise.promise(Optional::empty);
            String token = authorization.split(" ")[1];

            Accessor accessor = accessorsService.validateToken(token, context.request().path());
            return F.Promise.promise(() -> Optional.ofNullable(accessor));
        }

        return F.Promise.promise(Optional::empty);
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