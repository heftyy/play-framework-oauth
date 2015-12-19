package oauth.controllers;

import common.repository.Repository;
import oauth.models.OAuthLog;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;

public class CLog extends Controller {
    private final Repository<OAuthLog> logRepository;

    @Inject
    public CLog(Repository<OAuthLog> logRepository) {
        this.logRepository = logRepository;
    }

    @Transactional
    public Result get() {
        return ok(Json.toJson(logRepository.findAll()));
    }
}
