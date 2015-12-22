package oauth.models;

import common.models.AbstractModel;
import org.joda.time.DateTime;

import java.util.Set;

public class OAuthScope extends AbstractModel {
	private Long id;
	private String name;
	private String description;
	private DateTime addTime;
	private DateTime modTime;
    private OAuthApi api;
    private Set<OAuthClient> clients;
	private Set<OAuthUrlPattern> urlPatterns;

    public OAuthScope() {
    }

    public OAuthScope(String name, String description, OAuthApi api) {
        this.name = name;
        this.description = description;
        this.api = api;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DateTime getAddTime() {
        return addTime;
    }

    public void setAddTime(DateTime addTime) {
        this.addTime = addTime;
    }

    public DateTime getModTime() {
        return modTime;
    }

    public void setModTime(DateTime modTime) {
        this.modTime = modTime;
    }

    public OAuthApi getApi() {
        return api;
    }

    public void setApi(OAuthApi api) {
        this.api = api;
    }

    public Set<OAuthClient> getClients() {
        return clients;
    }

    public void setClients(Set<OAuthClient> clients) {
        this.clients = clients;
    }

    public Set<OAuthUrlPattern> getUrlPatterns() {
        return urlPatterns;
    }

    public void setUrlPatterns(Set<OAuthUrlPattern> urlPatterns) {
        this.urlPatterns = urlPatterns;
    }
}
