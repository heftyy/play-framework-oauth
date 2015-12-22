package oauth.controllers;

import com.google.common.collect.Lists;
import common.repository.Repository;
import oauth.models.OAuthScope;
import oauth.services.ScopesRequestService;
import org.hibernate.criterion.Restrictions;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.List;

public class COauthScopes extends Controller {

    private final ScopesRequestService scopesService;
    private final Repository<OAuthScope> scopeRepository;

    @Inject
    public COauthScopes(ScopesRequestService scopesService, Repository<OAuthScope> scopeRepository) {
        this.scopesService = scopesService;
        this.scopeRepository = scopeRepository;
    }

    /**
     * Download scopes from the webservice.
     *
     * @param apiId Long: API's (Webservice's) id in the database.
     */
    @Transactional
    public Result downloadScopes(Long apiId) {
        return ok(scopesService.getScopesJson(apiId));
    }

    @Transactional
    public Result scopesForApi(Long apiId) {
        List<OAuthScope> scopes = scopeRepository.findWithRestrictions(
                Lists.newArrayList(
                        Restrictions.eq("api.id", apiId)
                ), "urlPatterns");
        return ok(Json.toJson(scopes));
    }

    @Transactional
    public Result scopesForApiAndClient(Long apiId, Long clientId) {
        List<OAuthScope> scopes = scopeRepository.findWithRestrictions(
                Lists.newArrayList(
                        Restrictions.eq("api.id", apiId),
                        Restrictions.eq("clients.id", clientId)),
                "clients", "urlPatterns");
        return ok(Json.toJson(scopes));
    }

    @Transactional
    public Result updateScopes(String json) {
        if(json == null) return badRequest("Missing json");
        return ok(scopeRepository.updateFromJson(Json.parse(json)));
    }
}
