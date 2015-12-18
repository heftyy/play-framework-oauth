package oauth.models;

import common.models.AbstractModel;

import java.util.Set;

public class OAuthApi extends AbstractModel {

    private Long id;
	private String name;
	private String domain;
	private String scopeRequestUrl;
	private boolean onGlobally;
	private Set<OAuthClient> clients;
	private Set<OAuthLevel> levels;

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

    public boolean isOnGlobally() {
        return onGlobally;
    }

    public void setOnGlobally(boolean onGlobally) {
        this.onGlobally = onGlobally;
    }

    public Set<OAuthClient> getClients() {
        return clients;
    }

    public void setClients(Set<OAuthClient> clients) {
        this.clients = clients;
    }

    public Set<OAuthLevel> getLevels() {
        return levels;
    }

    public void setLevels(Set<OAuthLevel> levels) {
        this.levels = levels;
    }
}
