package oauth.services;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.google.common.collect.Lists;
import common.repository.Repository;
import oauth.models.OAuthScope;
import org.hibernate.criterion.Restrictions;

import javax.inject.Inject;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ScopesService {
    private final Repository<OAuthScope> scopeRepository;

    @Inject
    public ScopesService(Repository<OAuthScope> scopeRepository) {
        this.scopeRepository = scopeRepository;
    }

    /**
     * Select all the levels client can access on a given webservice. The method is
     * called when oauth tells the webservice the levels this user(accessorId) is allowed to.
     *
     * @param accessorId   String: Client's UUID.
     * @param domain       String: Webservice's domain.
     * @return Set<String> with levels
     */
    public Set<String> getLevelsFor(String accessorId, String domain) {
        List<OAuthScope> scopes = scopeRepository.findWithRestrictions(Lists.newArrayList(
                Restrictions.eq("clients.accessorId", accessorId),
                Restrictions.eq("api.domain", domain)
        ), "level", "level.api", "level.clients");

        return scopes.stream().map(scope -> scope.getLevel().getName()).collect(Collectors.toSet());
    }
}
