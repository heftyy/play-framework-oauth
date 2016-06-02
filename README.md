# play-framework-oauth [![Build Status](https://travis-ci.org/heftyy/play-framework-oauth.svg?branch=master)](https://travis-ci.org/heftyy/play-framework-oauth)

This is an implementation of oauth2 for server to server authentication.

This application contains multiple modules:
 * oauth-client - used by the client application to request data from the webservice with content
 * oauth-ws - used by the webservice with content, needs to contain a scopes.json file to regulate who can access which information
 * oauth-server - backend authentication server, clients request tokens from it by signing request data with their private keys, webservices validate tokens provided by clients against the authentication server
 * oauth-base - data structures necessary for the 3 modules above
 * commons - utiliity classes

example scopes.json
```json
[
  {"name": "test1", "description": "test level", "urlPatterns": [
    {"pattern": "/oauth/ws/data1", "description": "read webservice data 1", "method": "GET", "returns": "json"},
    {"pattern": "/test", "description": "read test url", "method": "GET", "returns": "text"}
  ]}
]
```

example controller providing data on the webservice
```java
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
```
