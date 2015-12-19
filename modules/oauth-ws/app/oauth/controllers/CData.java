package oauth.controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class CData extends Controller {

    @Restrict(value = @Group({"test"}), handlerKey = "oauth")
    public Result data() {
        ObjectNode on = Json.newObject();
        on.put("data", "ws_data");
        return ok(on);
    }

}
