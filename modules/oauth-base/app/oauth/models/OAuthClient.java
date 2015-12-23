package oauth.models;

import common.models.AbstractModel;
import org.joda.time.DateTime;

import java.util.Set;

public class OAuthClient extends AbstractModel {
    private Long id;
	private String name;
	private String accessorId;
	private String publicKey;
	private String password;
	private DateTime creationTime;
	private Set<OAuthApi> apis;
	private Set<OAuthScope> scopes;

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

    public String getAccessorId() {
        return accessorId;
    }

    public void setAccessorId(String accessorId) {
        this.accessorId = accessorId;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public DateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(DateTime creationTime) {
        this.creationTime = creationTime;
    }

    public Set<OAuthApi> getApis() {
        return apis;
    }

    public void setApis(Set<OAuthApi> apis) {
        this.apis = apis;
    }

    public Set<OAuthScope> getScopes() {
        return scopes;
    }

    public void setScopes(Set<OAuthScope> scopes) {
        this.scopes = scopes;
    }
}
