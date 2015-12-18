package oauth.models;

import common.models.AbstractModel;
import oauth.models.compositeid.OAuthLevelID;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class OAuthLevel extends AbstractModel {

	/**
	 * Level model containing all the data about a level and a list of scopes
	 * for this level.
	 */
	private Long id;
	private String name;
	private String description;
	private DateTime addTime;
	private DateTime modTime;
	private boolean userAllowed;
	private boolean updated;
    private OAuthApi api;
    private Set<OAuthClient> clients;
	private Set<OAuthScope> scopes;

	public OAuthLevel() {
		userAllowed = false;
	}

	public OAuthLevel(String name, String description, OAuthApi api) {
        this.api = api;
		this.name = name;
		this.description = description;
		this.userAllowed = false;
		this.addTime = DateTime.now();
		this.modTime = DateTime.now();
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

    public boolean isUserAllowed() {
        return userAllowed;
    }

    public void setUserAllowed(boolean userAllowed) {
        this.userAllowed = userAllowed;
    }

    public boolean isUpdated() {
        return updated;
    }

    public void setUpdated(boolean updated) {
        this.updated = updated;
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

    public Set<OAuthScope> getScopes() {
        return scopes;
    }

    public void setScopes(Set<OAuthScope> scopes) {
        this.scopes = scopes;
    }
}
