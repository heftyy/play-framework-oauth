package oauth.webservice.scopes;

import java.util.Objects;

public class UrlPattern {
    private String pattern;
    private String method;
    private String returns;
    private String description;

    public UrlPattern() {
    }

    public UrlPattern(String pattern, String method, String returns, String description) {
        this.pattern = pattern;
        this.method = method;
        this.returns = returns;
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UrlPattern urlPattern = (UrlPattern) o;
        return Objects.equals(pattern, urlPattern.pattern) &&
                Objects.equals(method, urlPattern.method);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pattern, method);
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
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