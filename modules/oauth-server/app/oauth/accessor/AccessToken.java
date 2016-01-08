package oauth.accessor;

import common.models.JsonSerializable;
import oauth.messages.AccessTokenSuccess;
import oauth.models.OAuthWS;

import java.util.Objects;

public class AccessToken extends JsonSerializable {
    public static long TOKEN_VALID_FOR_MILLISECONDS = 60 * 60 * 1000; // 60 minutes

    private String accessorId;
    private String token;
    private String type;
    private Long expiresAt;
    private OAuthWS ws;

    public AccessToken(String accessorId, String token, Long expiresAt, String type, OAuthWS ws) {
        this.accessorId = accessorId;
        this.token = token;
        this.expiresAt = expiresAt;
        this.type = type;
        this.ws = ws;
    }

    public AccessTokenSuccess getMessage() {
        return new AccessTokenSuccess(accessorId, token, type, TOKEN_VALID_FOR_MILLISECONDS);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccessToken that = (AccessToken) o;
        return Objects.equals(accessorId, that.accessorId) &&
                Objects.equals(token, that.token) &&
                Objects.equals(expiresAt, that.expiresAt) &&
                Objects.equals(type, that.type) &&
                Objects.equals(ws, that.ws);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accessorId, token, expiresAt, type, ws);
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

    public OAuthWS getWs() {
        return ws;
    }

    public void setWs(OAuthWS ws) {
        this.ws = ws;
    }
}
