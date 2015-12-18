package oauth.controllers;

import com.google.common.collect.Lists;
import common.repository.Repository;
import oauth.models.OAuthLevel;
import oauth.services.ScopesRequestService;
import org.hibernate.criterion.Restrictions;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.List;

public class COAuthLevels extends Controller {

    private final ScopesRequestService scopesService;
    private final Repository<OAuthLevel> levelsRepository;

    @Inject
    public COAuthLevels(ScopesRequestService scopesService, Repository<OAuthLevel> levelRepository) {
        this.scopesService = scopesService;
        this.levelsRepository = levelRepository;
    }

    /**
     * Download scopes from the webservice.
     *
     * @param apiId int: Api's (Webservice's) id in the database.
     */
    @Transactional
    public Result downloadScopes(Long apiId) {
        return ok(scopesService.askForScopes(apiId));
    }

    @Transactional
    public Result levelsForApi(Long apiId) {
        List<OAuthLevel> levels = levelsRepository.findWithRestrictions(
                Lists.newArrayList(
                        Restrictions.eq("compositeId.apiId", apiId)),
                "level");
        return ok(Json.toJson(levels));
    }

    @Transactional
    public Result levelsForApiAndClient(Long apiId, Long clientId) {
        List<OAuthLevel> levels = levelsRepository.findWithRestrictions(
                Lists.newArrayList(
                        Restrictions.eq("compositeId.apiId", apiId),
                        Restrictions.eq("clients.id", clientId)),
                "clients", "level");
        return ok(Json.toJson(levels));
    }

    @Transactional
    public Result updateLevels(String json) {
        if(json == null) return badRequest("Missing json");
        return ok(levelsRepository.updateFromJson(Json.parse(json)));
    }
}
