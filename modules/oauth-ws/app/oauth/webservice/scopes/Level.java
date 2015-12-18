package oauth.webservice.scopes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class Level {
    private String name;
    private String description;
    private List<Scope> scopeList = new ArrayList<>();

    public Level() {
    }

    public Level(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Level level = (Level) o;
        return Objects.equals(name, level.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Scope> getScopeList() {
        return scopeList;
    }

    public void setScopeList(List<Scope> scopeList) {
        this.scopeList = scopeList;
    }
}