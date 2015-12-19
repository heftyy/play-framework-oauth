package oauth.webservice.scopes;

import oauth.utils.ScopesStringCompare;
import oauth.webservice.ValidAccessor;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class ScopesContainerImpl implements ScopesContainer {
    private List<Level> levels = new ArrayList<>();
    private boolean scopesLoaded = false;

    @Override
    public boolean checkIfClientAllowed(ValidAccessor accessor, String scope) {
        List<String> levelsAllowed = accessor.getAllowedLevels();
        List<String> webserviceLevels = findLevelsForScope(scope);

        for (String levelAllowed : levelsAllowed) {
            if (webserviceLevels.contains(levelAllowed)) return true;
        }

        return false;
    }

    @Override
    public List<String> findLevelsForScope(String requestedScope) {
        List<String> result = new ArrayList<>();
        for (Level level : levels) {
            result.addAll(level.getScopeList().stream().
                    filter(scope -> ScopesStringCompare.compareStringsRegex(requestedScope, scope.getUrl())).
                    map(scope -> level.getName()).
                    collect(Collectors.toList()));
        }
        return result;
    }

    @Override
    public void addLevel(String name, String description) {
        Level level = new Level(name, description);
        if (levels.contains(level)) return;

        levels.add(level);
    }

    @Override
    public void addScope(String levelName, String url, String description, String method, String returns) {
        levels.stream().
                filter(level -> level.getName().equals(levelName)).
                forEach(level -> {
                    Scope scope = new Scope(url, method, returns, description);
                    if (level.getScopeList().contains(scope)) return;

                    level.getScopeList().add(scope);
                }
        );
    }

    @Override
    public boolean removeScope(String levelName, String url) {
        boolean result = false;

        for (Level level : levels) {
            for (Iterator<Scope> iterScope = level.getScopeList().iterator(); iterScope.hasNext(); ) {
                Scope scope = iterScope.next();
                if (scope.getUrl().equals(url)) {
                    iterScope.remove();
                    result = true;
                }
            }
        }

        return result;
    }

    @Override
    public boolean removeLevel(String levelName) {
        boolean result = false;
        for (Iterator<Level> iterLevel = levels.iterator(); iterLevel.hasNext(); ) {
            Level level = iterLevel.next();
            if (level.getName().equals(levelName)) {
                iterLevel.remove();
                result = true;
            }
        }
        return result;
    }

    @Override
    public List<Level> getLevels() {
        return levels;
    }

    @Override
    public boolean isScopesLoaded() {
        return scopesLoaded;
    }

    @Override
    public void setScopesLoaded(boolean scopesLoaded) {
        this.scopesLoaded = scopesLoaded;
    }
}
