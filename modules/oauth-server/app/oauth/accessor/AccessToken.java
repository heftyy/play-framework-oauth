package oauth.accessor;

import common.models.AbstractModel;
import oauth.messages.AccessTokenMessage;

public class AccessToken extends AbstractModel {
    protected String accessorId;
    protected String accessToken;
    protected String scope;
    protected String remoteAddress;
    protected Long tokenExpiresAt;
    protected Integer type;

    public AccessToken(String accessorId, String accessToken, String scope, String remoteAddress, Long tokenExpiresAt, Integer type) {
        this.accessorId = accessorId;
        this.accessToken = accessToken;
        this.scope = scope;
        this.remoteAddress = remoteAddress;
        this.tokenExpiresAt = tokenExpiresAt;
        this.type = type;
    }

    public AccessTokenMessage getMessage() {
        return new AccessTokenMessage(accessorId, accessToken, type, tokenExpiresAt);
    }

    public String getAccessorId() {
        return accessorId;
    }

    public void setAccessorId(String accessorId) {
        this.accessorId = accessorId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getRemoteAddress() {
        return remoteAddress;
    }

    public void setRemoteAddress(String remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    public Long getTokenExpiresAt() {
        return tokenExpiresAt;
    }

    public void setTokenExpiresAt(Long tokenExpiresAt) {
        this.tokenExpiresAt = tokenExpiresAt;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
