package oauth.messages;

import java.util.Set;

public class WebServiceValidateResponse extends BaseMessage {
    private boolean tokenValid;
    private String accessorId;
    private String accessToken;
    private String clientRemoteAddress;
    private Set<String> allowedScopes;
    private Long expiresAt;

    public WebServiceValidateResponse() {
    }

    public WebServiceValidateResponse(boolean tokenValid, String accessorId, String accessToken, String clientRemoteAddress, Set<String> allowedScopes, Long expiresAt) {
        this.tokenValid = tokenValid;
        this.accessorId = accessorId;
        this.accessToken = accessToken;
        this.clientRemoteAddress = clientRemoteAddress;
        this.allowedScopes = allowedScopes;
        this.expiresAt = expiresAt;
    }

    public WebServiceValidateResponse(boolean tokenValid) {
        this.tokenValid = tokenValid;
    }

    public boolean isTokenValid() {
        return tokenValid;
    }

    public String getAccessorId() {
        return accessorId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getClientRemoteAddress() {
        return clientRemoteAddress;
    }

    public Set<String> getAllowedScopes() {
        return allowedScopes;
    }

    public Long getExpiresAt() {
        return expiresAt;
    }
}
