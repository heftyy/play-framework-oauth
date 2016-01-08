package oauth.jwt;

import common.models.JsonSerializable;

import java.util.Objects;

public class JsonWebTokenClaim extends JsonSerializable {
    private String accessorId;
    private String domain;
    private String scope;
    private String authUrl;
    private Long time;

    public JsonWebTokenClaim() {}

    public JsonWebTokenClaim(String accessorId, String domain, String scope, String authUrl, Long time) {
        this.accessorId = accessorId;
        this.domain = domain;
        this.scope = scope;
        this.authUrl = authUrl;
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JsonWebTokenClaim that = (JsonWebTokenClaim) o;
        return Objects.equals(accessorId, that.accessorId) &&
                Objects.equals(domain, that.domain) &&
                Objects.equals(scope, that.scope) &&
                Objects.equals(authUrl, that.authUrl) &&
                Objects.equals(time, that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accessorId, domain, scope, authUrl, time);
    }

    public String getAccessorId() {
        return accessorId;
    }

    public void setAccessorId(String accessorId) {
        this.accessorId = accessorId;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getAuthUrl() {
        return authUrl;
    }

    public void setAuthUrl(String authUrl) {
        this.authUrl = authUrl;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
