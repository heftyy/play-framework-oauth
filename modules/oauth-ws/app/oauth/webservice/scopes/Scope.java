package oauth.webservice.scopes;

import java.util.Objects;

class Scope {
    private String url;
    private String method;
    private String returns;
    private String description;

    public Scope() {
    }

    public Scope(String url, String method, String returns, String description) {
        this.url = url;
        this.method = method;
        this.returns = returns;
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Scope scope = (Scope) o;
        return Objects.equals(url, scope.url) &&
                Objects.equals(method, scope.method);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, method);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getReturns() {
        return returns;
    }

    public void setReturns(String returns) {
        this.returns = returns;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}