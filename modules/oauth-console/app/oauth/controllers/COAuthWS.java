package oauth.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import common.repository.Repository;
import oauth.models.OAuthWS;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.List;

public class COAuthWS extends Controller {

    private final Repository<OAuthWS> wsRepository;

    @Inject
    public COAuthWS(Repository<OAuthWS> wsRepository) {
        this.wsRepository = wsRepository;
    }

    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
    public Result update() {
        Http.RequestBody body = request().body();
        return ok(wsRepository.updateWithJson(body.asJson()));
    }

    @Transactional
    public Result delete(Long id) {
        OAuthWS ws = wsRepository.delete(id);
        if(ws != null) return ok(ws.getJson());
        else return badRequest("Already deleted");
    }

    @Transactional
    public Result getList(String json) {
        if (json == null) {
            return ok(Json.toJson(wsRepository.findAll()));
        } else {
            JsonNode jn = Json.parse(json);
            List<Criterion> restrictions = Lists.newArrayList();

            if(jn.has("clientId")) {
                Long clientId = jn.get("clientId").asLong(0);
                if(clientId != 0) restrictions.add(Restrictions.eq("client.id", clientId));
            }

            if(jn.has("wsId")) {
                Long wsId = jn.get("wsId").asLong(0);
                if(wsId == 0) restrictions.add(Restrictions.eq("id", wsId));
            }

            return ok(Json.toJson(
                    wsRepository.findWithRestrictions(restrictions, "clients")
            ));
        }
    }
}
