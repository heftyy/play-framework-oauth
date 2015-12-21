package oauth.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableMap;
import common.repository.Repository;
import oauth.models.OAuthApi;
import oauth.models.OAuthLevel;
import oauth.models.OAuthScope;
import play.db.jpa.JPA;
import play.libs.F;
import play.libs.Json;
import play.libs.ws.WS;
import play.libs.ws.WSResponse;
import play.mvc.Http;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ScopesRequestService {

    private final Repository<OAuthLevel> levelRepository;
    private final Repository<OAuthApi> apiRepository;

    @Inject
    public ScopesRequestService(Repository<OAuthLevel> levelRepository, Repository<OAuthApi> apiRepository) {
        this.levelRepository = levelRepository;
        this.apiRepository = apiRepository;
    }

    /**
     * Downloads scopes from the webservice, if the address for requesting
     * scopes was not specified, the method creates one level 'ALL' and sets a
     * scope '/*' for it.
     *
     * @param apiId int: Api(webservice) id in the database
     */
    public JsonNode askForScopes(Long apiId) {
        OAuthApi api = apiRepository.findById(apiId);
        String url = api.getDomain() + api.getScopeRequestUrl();
        if (url.length() == 0) {
            insertDefaultLevel(apiId);
            return Json.newObject();
        }
        if (!url.startsWith("http://")) {
            url = "http://" + url;
        }
        try {
            F.Promise<WSResponse> promise = WS.url(url).get();
            WSResponse response = promise.get(1000);
            if (response.getStatus() != Http.Status.OK) {
                OAuthLevel l = insertDefaultLevel(apiId);
                return l.getJson();
            }
            JsonNode jsonObject = response.asJson();
            return levelRepository.updateFromJson(jsonObject);
        } catch (Exception e) {
            play.Logger.error("Connection refused to: " + url);
            insertDefaultLevel(apiId);
        }
        return Json.newObject();
    }

    public OAuthLevel insertDefaultLevel(Long apiId) {
        OAuthLevel l = levelRepository.findByFields(ImmutableMap.of(
                "compositeId.apiId", apiId,
                "compositeId.level", "ALL"));

        if (l != null) {
            return l;
        } else {
            JPA.em().createQuery("DELETE FROM OAuthLevel WHERE api_id=?1").setParameter(1, apiId).executeUpdate();
            l = new OAuthLevel("ALL", "all rights", apiRepository.findById(apiId));
            OAuthScope s = new OAuthScope("/*", "*", "*", "*", l);
            l.getScopes().add(s);
            return l;
        }
    }

    /**
     * Parses the JSON received from the webservice, called by askForScopes
     * method. Adds the levels and scopes to the database and removes the ones
     * that still exist in the database but weren't received in JSON.
     *
     * @param jsonObject JsonNode: JSON from webservice.
     * @param apiId      int: Api(webservice) id in the database
     */
    public void saveScopes(JsonNode jsonObject, Long apiId) {
        levelRepository.updateFromJson(jsonObject);
        /*
        for (Iterator<String> iterLevels = jsonObject.fieldNames(); iterLevels.hasNext(); ) {
            String key = iterLevels.next();
            JsonNode jsonLevel = jsonObject.get(key);
            String levelName = key;
            String levelDesc = jsonLevel.findPath("desc").asText();
            OAuthLevel l = levelRepository.findByFields(ImmutableMap.of(
                    "compositeId.apiId", apiId,
                    "compositeId.level", "ALL"));

            if (l == null) {
                l = new OAuthLevel(levelName, levelDesc, apiId);
            }
            System.out.println("creating new level " + levelName + " " + levelDesc + " " + apiId);
            JsonNode jsonScopes = jsonLevel.get("scopes");
            List<OAuthScope> scopes = new ArrayList<OAuthScope>();
            for (Iterator<String> iterScopes = jsonScopes.fieldNames(); iterScopes.hasNext(); ) {
                key = iterScopes.next();
                JsonNode jsonScope = jsonScopes.get(key);
                String url = key;
                String description = jsonScope.findPath("desc").asText();
                String method = jsonScope.findPath("method").asText();
                String returns = jsonScope.findPath("returns").asText();
                System.out.println("creating new scope " + url + " " + method + " " + returns + " " + levelName + " " + apiId);
                OAuthScope s = new OAuthScope(url, method, returns, "arg1, arg2", levelName, apiId);
                scopes.add(s);
            }
            JPA.em().createQuery("DELETE FROM OAuthScope WHERE apiId=?1 AND levelName=?2").setParameter(1, apiId).setParameter(2, levelName).executeUpdate();
            //l.setScopes(scopes);
            l.save();
            for (OAuthScope scope : scopes) {
                JPA.em().persist(scope);
            }
            JPA.em().flush();
        }
        */
    }
}