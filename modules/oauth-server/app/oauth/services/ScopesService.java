package oauth.services;

import com.google.common.collect.Lists;
import common.repository.Repository;
import oauth.models.OAuthUrlPattern;
import org.hibernate.criterion.Restrictions;

import javax.inject.Inject;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ScopesService {
    private final Repository<OAuthUrlPattern> scopeRepository;

    @Inject
    public ScopesService(Repository<OAuthUrlPattern> scopeRepository) {
        this.scopeRepository = scopeRepository;
    }

    /**
     * Select all the scopes client can access on a given webservice. The method is
     * called when oauth tells the webservice the scopes this user(accessorId) is allowed to.
     *
     * @param accessorId   String: Client's UUID.
     * @param domain       String: Webservice's domain.
     * @return Set<String> with scopes
     */
    public Set<String> getScopesFor(String accessorId, String domain) {
        List<OAuthUrlPattern> scopes = scopeRepository.findWithRestrictions(Lists.newArrayList(
                Restrictions.eq("clients.accessorId", accessorId),
                Restrictions.eq("ws.domain", domain)
        ), "scope", "scope.ws", "scope.clients");

        return scopes.stream().map(scope -> scope.getScope().getName()).collect(Collectors.toSet());
    }

    public Set<String> getPatternsFor(String accessorId, String domain) {
        List<OAuthUrlPattern> scopes = scopeRepository.findWithRestrictions(Lists.newArrayList(
                Restrictions.eq("clients.accessorId", accessorId),
                Restrictions.eq("ws.domain", domain)
        ), "scope", "scope.ws", "scope.clients");

        return scopes.stream().map(OAuthUrlPattern::getPattern).collect(Collectors.toSet());
    }
}
