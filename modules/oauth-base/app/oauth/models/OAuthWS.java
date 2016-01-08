package oauth.models;

import common.models.AbstractModel;

import java.util.Objects;
import java.util.Set;

public class OAuthWS extends AbstractModel {

    private Long id;
	private String name;
	private String domain;
	private String scopeRequestUrl;
	private boolean enabled;
	private Set<OAuthClient> clients;
	private Set<OAuthScope> scopes;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OAuthWS oAuthWS = (OAuthWS) o;
        return Objects.equals(id, oAuthWS.id) &&
                Objects.equals(name, oAuthWS.name) &&
                Objects.equals(domain, oAuthWS.domain);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, domain);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getScopeRequestUrl() {
        return scopeRequestUrl;
    }

    public void setScopeRequestUrl(String scopeRequestUrl) {
        this.scopeRequestUrl = scopeRequestUrl;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<OAuthClient> getClients() {
        return clients;
    }

    public void setClients(Set<OAuthClient> clients) {
        this.clients = clients;
    }

    public Set<OAuthScope> getScopes() {
        return scopes;
    }

    public void setScopes(Set<OAuthScope> scopes) {
        this.scopes = scopes;
    }
}
