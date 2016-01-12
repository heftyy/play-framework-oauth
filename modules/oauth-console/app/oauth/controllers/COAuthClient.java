package oauth.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import common.repository.Repository;
import oauth.models.OAuthClient;
import oauth.services.GenerateKeyService;
import org.bouncycastle.operator.OperatorCreationException;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.List;

public class COAuthClient extends Controller {

    private final Repository<OAuthClient> clientRepository;
    private final GenerateKeyService generateKeyService;

    @Inject
    public COAuthClient(Repository<OAuthClient> clientRepository, GenerateKeyService generateKeyService) {
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
    @BodyParser.Of(BodyParser.Json.class)
    public Result update() {
        Http.RequestBody body = request().body();
        List<OAuthClient> clients = clientRepository.parseJson(body.asJson());
        for(OAuthClient client : clients) {
            // generate keys for a new client
            if(client.getId() == null) {
                try {
                    generateKeyService.generateKey(client);
                } catch (CertificateException | NoSuchAlgorithmException | KeyStoreException | OperatorCreationException | InvalidKeyException | IOException | SignatureException | NoSuchProviderException e) {
                    return internalServerError("Generating the private key failed");
                }
                JPA.em().persist(client);
            } else {
                JPA.em().merge(client);
            }
        }

        return ok(Json.toJson(clients));
    }

    @Transactional
    public Result delete(Long id) {
        OAuthClient client = clientRepository.delete(id);
        if(client != null) return ok(client.getJson());
        else return badRequest("Already deleted");
    }

    @Transactional
    public Result getList(String json) {
        if(json == null) return ok(Json.toJson((clientRepository.findAll())));
        JsonNode jn = Json.parse(json);

        List<Criterion> restrictions = Lists.newArrayList();

        if(jn.has("clientId")) {
            Long clientId = jn.get("clientId").asLong(0);
            if(clientId != 0) restrictions.add(Restrictions.eq("id", clientId));
        }

        return ok(Json.toJson(clientRepository.findWithRestrictions(restrictions)));
    }
}
