package oauth.controllers;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class CData extends Controller {

    @Restrict(value = @Group({"test1"}), handlerKey = "oauth")
    public Result data1Get() {
        ObjectNode on = Json.newObject();
        on.put("data", "ws_data1");
        return ok(on);
    }

    @Restrict(value = @Group({"test2"}), handlerKey = "oauth")
    public Result data2Get() {
        ObjectNode on = Json.newObject();
        on.put("data", "ws_data2");
        return ok(on);
    }

    public Result data3Get() {
        ObjectNode on = Json.newObject();
        on.put("data", "ws_data3");
        return ok(on);
    }

    @Restrict(value = @Group({"test1"}), handlerKey = "oauth")
    public Result data1Post() {
        ObjectNode on = Json.newObject();
        on.put("data", "ws_data1");
        return ok(on);
    }

    @Restrict(value = @Group({"test2"}), handlerKey = "oauth")
    public Result data2Post() {
        ObjectNode on = Json.newObject();
        on.put("data", "ws_data2");
        return ok(on);
    }

}
