package oauth.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import common.repository.Repository;
import oauth.models.OAuthClient;
import oauth.models.OAuthScope;
import oauth.services.ScopesRequestService;
import org.hibernate.criterion.Restrictions;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.List;

public class COauthScope extends Controller {

    private final ScopesRequestService scopesService;
    private final Repository<OAuthScope> scopeRepository;
    private final Repository<OAuthClient> clientRepository;

    @Inject
    public COauthScope(ScopesRequestService scopesService, Repository<OAuthScope> scopeRepository, Repository<OAuthClient> clientRepository) {
        this.scopesService = scopesService;
        this.scopeRepository = scopeRepository;
        this.clientRepository = clientRepository;
    }

    /**
     * Download scopes from the webservice.
     *
     * @param wsId Long: API's (Webservice's) id in the database.
     */
    @Transactional
    public Result downloadScopes(Long wsId) {
        return ok(scopesService.getScopesJson(wsId));
    }

    @Transactional
    public Result scopesForWS(Long wsId) {
        List<OAuthScope> scopes = scopeRepository.findWithRestrictions(
                Lists.newArrayList(
                        Restrictions.eq("ws.id", wsId)
                ), "urlPatterns");
        return ok(Json.toJson(scopes));
    }

    @Transactional
    public Result scopesForWSAndClient(Long wsId, Long clientId) {
        List<OAuthScope> scopes = scopeRepository.findWithRestrictions(
                Lists.newArrayList(
                        Restrictions.eq("ws.id", wsId),
                        Restrictions.eq("clients.id", clientId)),
                "clients", "urlPatterns");
        return ok(Json.toJson(scopes));
    }

    @Transactional
    @BodyParser.Of(BodyParser.Json.class)
    public Result updateScopes(String json) {
        if(json == null) return badRequest("Missing json");
        Http.RequestBody body = request().body();
        JsonNode jn = body.asJson();

        Long clientId = jn.get("clientId").asLong();
        Long scopeId = jn.get("scopeId").asLong();

        OAuthScope scope = scopeRepository.findByField("id", scopeId, "clients");
        OAuthClient client = clientRepository.findById(clientId);

        if(scope.getClients() == null) scope.setClients(Sets.newHashSet());
        scope.getClients().add(client);

        return ok(scope.getJson());
    }
}
