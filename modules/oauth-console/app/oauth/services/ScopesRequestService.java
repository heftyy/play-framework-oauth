package oauth.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import common.repository.Repository;
import oauth.models.OAuthApi;
import oauth.models.OAuthScope;
import oauth.models.OAuthUrlPattern;
import play.db.jpa.JPA;
import play.libs.F;
import play.libs.Json;
import play.libs.ws.WS;
import play.libs.ws.WSResponse;
import play.mvc.Http;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

public class ScopesRequestService {

    private final Repository<OAuthScope> scopeRepository;
    private final Repository<OAuthApi> apiRepository;

    @Inject
    public ScopesRequestService(Repository<OAuthScope> scopeRepository, Repository<OAuthApi> apiRepository) {
        this.scopeRepository = scopeRepository;
        this.apiRepository = apiRepository;
    }

    /**
     * Downloads scopes from the webservice, if the address for requesting
     * scopes was not specified, the method creates one scope 'ALL' and sets a
     * scope '/*' for it.
     *
     * @param apiId int: Api(webservice) id in the database
     */
    public JsonNode getScopesJson(Long apiId) {
        removeOldScopes(apiId);

        List<OAuthScope> scopes;

        String url = getScopesRequestUrl(apiId);

        if(url == null || url.length() == 0) scopes = Lists.newArrayList(insertDefaultScope(apiId));
        else {
            scopes = getScopesFromWebService(url);
            if(scopes == null || scopes.size() == 0) scopes = Lists.newArrayList(insertDefaultScope(apiId));
        }

        OAuthApi api = apiRepository.findById(apiId);

        scopes.stream().forEach(scope -> {
            scope.setApi(api);
            JPA.em().persist(scope);
        });

        return Json.toJson(scopes);
    }

    public OAuthScope insertDefaultScope(Long apiId) {
        OAuthScope defaultScope = scopeRepository.findByFields(ImmutableMap.of(
                "api.id", apiId,
                "name", "ALL"));

        if (defaultScope != null) {
            return defaultScope;
        } else {
            defaultScope = new OAuthScope("ALL", "default scope - all urls", apiRepository.findById(apiId));
            OAuthUrlPattern scope = new OAuthUrlPattern("/*", "*", "*", "*", defaultScope);
            defaultScope.setUrlPatterns(Sets.newHashSet(scope));

            JPA.em().persist(defaultScope);

            return defaultScope;
        }
    }

    public List<OAuthScope> getScopesFromWebService(String url) {
        F.Promise<WSResponse> promise = WS.url(url).get();
        WSResponse response = promise.get(1000);
        if (response.getStatus() != Http.Status.OK) {
            return null;
        }
        JsonNode jn = response.asJson();

        return Arrays.asList(Json.fromJson(jn, OAuthScope[].class));
    }

    private void removeOldScopes(Long apiId) {
        JPA.em().createQuery("DELETE FROM OAuthUrlPattern WHERE scope.id IN (SELECT id FROM OAuthScope WHERE api.id = ?1)").
                setParameter(1, apiId).
                executeUpdate();

        JPA.em().createQuery("DELETE FROM OAuthScope WHERE api.id = ?1").
                setParameter(1, apiId).
                executeUpdate();
    }

    private String getScopesRequestUrl(Long apiId) {
        OAuthApi api = apiRepository.findById(apiId);
        if(api == null) return null;

        String url = api.getDomain() + api.getScopeRequestUrl();
        if (!url.startsWith("http://")) {
            url = "http://" + url;
        }

        return url;
    }
}