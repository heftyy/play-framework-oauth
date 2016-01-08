package oauth.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import common.repository.Repository;
import oauth.models.OAuthWS;
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
import java.util.Set;

public class ScopesRequestService {

    private final Repository<OAuthScope> scopeRepository;
    private final Repository<OAuthWS> wsRepository;

    @Inject
    public ScopesRequestService(Repository<OAuthScope> scopeRepository, Repository<OAuthWS> wsRepository) {
        this.scopeRepository = scopeRepository;
        this.wsRepository = wsRepository;
    }

    public Set<OAuthScope> getScopesSet(Long wsId) {
        return Sets.newHashSet(getScopes(wsId));
    }

    public List<OAuthScope> getScopes(Long wsId) {
        List<OAuthScope> scopes;

        String url = getScopesRequestUrl(wsId);

        if(url == null || url.length() == 0) {
            scopes = Lists.newArrayList(getDefaultScope(wsId));
        }
        else {
            scopes = getScopesFromWebService(url);
            if(scopes == null || scopes.size() == 0) scopes = Lists.newArrayList(getDefaultScope(wsId));
        }

        return scopes;
    }

    /**
     * Downloads scopes from the webservice, if the address for requesting
     * scopes was not specified, the method creates one scope 'ALL' and sets a
     * scope '/*' for it.
     *
     * @param wsId int: Api(webservice) id in the database
     */
    public JsonNode getScopesJson(Long wsId) {
        removeOldScopes(wsId);

        List<OAuthScope> scopes = getScopes(wsId);

        OAuthWS ws = wsRepository.findById(wsId);

        scopes.stream().forEach(scope -> {
            scope.setWs(ws);
        });

        return Json.toJson(scopes);
    }

    public OAuthScope getDefaultScope(Long wsId) {
        OAuthScope defaultScope = scopeRepository.findByFields(ImmutableMap.of(
                "ws.id", wsId,
                "name", "ALL"));

        if (defaultScope != null) {
            return defaultScope;
        } else {
            defaultScope = new OAuthScope("ALL", "default scope - all urls", wsRepository.findById(wsId));
            OAuthUrlPattern scope = new OAuthUrlPattern("/*", "*", "*", "*", defaultScope);
            defaultScope.setUrlPatterns(Sets.newHashSet(scope));

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

    private void removeOldScopes(Long wsId) {
        JPA.em().createQuery("DELETE FROM OAuthUrlPattern WHERE scope.id IN (SELECT id FROM OAuthScope WHERE ws.id = ?1)").
                setParameter(1, wsId).
                executeUpdate();

        JPA.em().createQuery("DELETE FROM OAuthScope WHERE ws.id = ?1").
                setParameter(1, wsId).
                executeUpdate();
    }

    private String getScopesRequestUrl(Long wsId) {
        OAuthWS ws = wsRepository.findById(wsId);
        if(ws == null) return null;

        String url = ws.getDomain() + ws.getScopeRequestUrl();
        if (!url.startsWith("http://")) {
            url = "http://" + url;
        }

        return url;
    }
}