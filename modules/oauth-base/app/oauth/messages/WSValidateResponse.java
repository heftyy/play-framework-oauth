package oauth.messages;

import java.util.Set;

public class WSValidateResponse extends BaseMessage {
    private boolean tokenValid;
    private String accessorId;
    private String accessToken;
    private Set<String> allowedScopes;

    public WSValidateResponse() {
    }

    public WSValidateResponse(boolean tokenValid) {
        this.tokenValid = tokenValid;
    }

    public WSValidateResponse(boolean tokenValid, String accessorId, String accessToken, Set<String> allowedScopes) {
        this.tokenValid = tokenValid;
        this.accessorId = accessorId;
        this.accessToken = accessToken;
        this.allowedScopes = allowedScopes;
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

    public Set<String> getAllowedScopes() {
        return allowedScopes;
    }
}
