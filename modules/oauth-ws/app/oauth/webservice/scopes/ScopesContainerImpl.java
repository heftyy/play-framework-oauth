package oauth.webservice.scopes;

import oauth.utils.ScopesStringCompare;
import oauth.webservice.Accessor;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class ScopesContainerImpl implements ScopesContainer {
    private List<Scope> scopes = new ArrayList<>();
    private boolean ready = false;

    @Override
    public boolean checkIfClientAllowed(Accessor accessor, String scope) {
        List<String> scopesAllowed = accessor.getAllowedScopes();
        List<String> wsScopes = findScopesForUrlPattern(scope);

        for (String scopeAllowed : scopesAllowed) {
            if (wsScopes.contains(scopeAllowed)) return true;
        }

        return false;
    }

    @Override
    public void addScope(String name, String description) {
        Scope scope = new Scope(name, description);
        if (scopes.contains(scope)) return;

        scopes.add(scope);
    }

    @Override
    public void addUrlPattern(String scopeName, String urlPattern, String description, String method, String returns) {
        scopes.stream().
                filter(scope -> scope.getName().equals(scopeName)).
                forEach(scope -> {
                    UrlPattern pattern = new UrlPattern(urlPattern, method, returns, description);
                    if (scope.getUrlPatterns().contains(pattern)) return;

                    scope.getUrlPatterns().add(pattern);
                }
        );
    }

    @Override
    public boolean removeUrlPattern(String scopeName, String urlPattern) {
        boolean result = false;

        for (Scope scope : scopes) {
            for (Iterator<UrlPattern> iterScope = scope.getUrlPatterns().iterator(); iterScope.hasNext(); ) {
                UrlPattern pattern = iterScope.next();
                if (pattern.getPattern().equals(urlPattern)) {
                    iterScope.remove();
                    result = true;
                }
            }
        }

        return result;
    }

    @Override
    public boolean removeScope(String scopeName) {
        boolean result = false;
        for (Iterator<Scope> iterScope = scopes.iterator(); iterScope.hasNext(); ) {
            Scope scope = iterScope.next();
            if (scope.getName().equals(scopeName)) {
                iterScope.remove();
                result = true;
            }
        }
        return result;
    }

    @Override
    public void setScopes(List<Scope> scopes) {
        this.scopes = scopes;
    }

    @Override
    public List<Scope> getScopes() {
        return scopes;
    }

    @Override
    public boolean isReady() {
        return ready;
    }

    @Override
    public void setReady(boolean ready) {
        this.ready = ready;
    }

    private List<String> findScopesForUrlPattern(String requestedScope) {
        List<String> result = new ArrayList<>();
        for (Scope scope : scopes) {
            result.addAll(scope.getUrlPatterns().stream().
                    filter(urlPattern -> ScopesStringCompare.compareStringsRegex(requestedScope, urlPattern.getPattern())).
                    map(urlPattern -> scope.getName()).
                    collect(Collectors.toList()));
        }
        return result;
    }
}
