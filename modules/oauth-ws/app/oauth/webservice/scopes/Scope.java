package oauth.webservice.scopes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class Scope {
    private String name;
    private String description;
    private List<UrlPattern> urlPatterns = new ArrayList<>();

    public Scope() {
    }

    public Scope(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Scope scope = (Scope) o;
        return Objects.equals(name, scope.name);
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

    public List<UrlPattern> getUrlPatterns() {
        return urlPatterns;
    }

    public void setUrlPatterns(List<UrlPattern> urlPatterns) {
        this.urlPatterns = urlPatterns;
    }
}