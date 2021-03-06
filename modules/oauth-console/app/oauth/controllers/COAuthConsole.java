package oauth.controllers;

import play.Routes;
import play.api.routing.JavaScriptReverseRoute;
import play.mvc.Controller;
import play.mvc.Result;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import static org.reflections.ReflectionUtils.getAllMethods;
import static org.reflections.ReflectionUtils.withReturnType;

public class COAuthConsole extends Controller {

    public Result index() {
        return ok(oauth.views.html.home.render());
    }

    public Result javascriptRoutes() throws IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {

        // use reflection to get the fields of controllers.routes.javascript and other controller packages
        Set<Object> reverseRoutes = new HashSet<>();
        Class[] routeClasses = {
                oauth.controllers.routes.javascript.class
        };
        for (Class routeClass : routeClasses) {
            for (Field f : routeClass.getFields()) {
                // get its methods
                for (Method m : getAllMethods(f.getType(), withReturnType(JavaScriptReverseRoute.class))) {
                    // for each method, add its result to the reverseRoutes
                    reverseRoutes.add(m.invoke(f.get(null)));
                }
            }
        }
        // return the reverse routes
        response().setContentType("text/javascript");
        return ok(Routes.javascriptRouter("jsRoutes",
                reverseRoutes.toArray(new play.api.routing.JavaScriptReverseRoute[reverseRoutes.size()])));
    }

}
