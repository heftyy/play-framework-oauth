package oauth.controllers;

import common.repository.Repository;
import oauth.models.OAuthClient;
import oauth.services.GenerateKeyService;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.io.File;

public class COAuthClients extends Controller {

    private final Repository<OAuthClient> clientRepository;
    private final GenerateKeyService generateKeyService;

    @Inject
    public COAuthClients(Repository<OAuthClient> clientRepository, GenerateKeyService generateKeyService) {
        this.clientRepository = clientRepository;
        this.generateKeyService = generateKeyService;
    }

    public Result downloadKey(String accessorId) {
        String path = generateKeyService.getSecretKeyPath(accessorId);
        File file = new File(path);
        response().setHeader("Content-Disposition", "attachment; filename=\"" + path + "\"");
        return ok(file);
    }

    @Transactional
    public Result updateClients(String json) {
        return ok(clientRepository.updateFromJson(Json.toJson(json)));
    }

    @Transactional
    public Result removeClients(String json) {
        return ok(clientRepository.removeFromJson(Json.toJson(json)));
    }

    @Transactional
    public Result clientList() {
        return ok(oauth.views.html.clientlist.render());
    }

    @Transactional
    public Result clients(String json) {
        return ok(Json.toJson((clientRepository.findAll())));
    }
}
