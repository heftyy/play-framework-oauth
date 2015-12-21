package oauth.accessor;

import common.models.AbstractModel;
import oauth.messages.AccessTokenMessage;
import oauth.models.OAuthApi;

public class AccessToken extends AbstractModel {
    private String accessorId;
    private String token;
    private Long expiresAt;
    private String type;
    private OAuthApi api;

    public AccessToken(String accessorId, String token, Long expiresAt, String type, OAuthApi api) {
        this.accessorId = accessorId;
        this.token = token;
        this.expiresAt = expiresAt;
        this.type = type;
        this.api = api;
    }

    public AccessTokenMessage getMessage() {
        return new AccessTokenMessage(accessorId, token, type, expiresAt);
    }

    public String getAccessorId() {
        return accessorId;
    }

    public void setAccessorId(String accessorId) {
        this.accessorId = accessorId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Long expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public OAuthApi getApi() {
        return api;
    }

    public void setApi(OAuthApi api) {
        this.api = api;
    }
}
