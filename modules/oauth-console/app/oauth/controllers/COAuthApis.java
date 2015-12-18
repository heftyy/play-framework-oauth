package oauth.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import common.repository.Repository;
import oauth.models.OAuthApi;
import org.hibernate.criterion.Restrictions;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.List;

public class COAuthApis extends Controller {

    private final Repository<OAuthApi> apiRepository;

    @Inject
    public COAuthApis(Repository<OAuthApi> apiRepository) {
        this.apiRepository = apiRepository;
    }

    @Transactional
    public Result updateApis(String json) {
        return ok(apiRepository.updateFromJson(Json.parse(json)));
    }

    @Transactional
    public Result removeApis(String json) {
        return ok(apiRepository.removeFromJson(Json.parse(json)));
    }

    @Transactional
    public Result apiList() {
        return ok(oauth.views.html.apilist.render());
    }

    @Transactional
    public Result apis(String json) {
        JsonNode jn = Json.parse(json);
        int clientId = jn.findPath("clientId").asInt(0);
        List<OAuthApi> l;
        if (clientId == 0) {
            l = apiRepository.findAll();
        } else {
            l = apiRepository.findWithRestrictions(
                    Lists.newArrayList(Restrictions.eq("client.id", clientId)),
                    "clients");
        }
        return ok(Json.toJson(l));
    }
}
