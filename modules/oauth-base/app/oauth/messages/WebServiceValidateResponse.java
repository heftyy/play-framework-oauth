package oauth.messages;

import java.util.Set;

public class WebServiceValidateResponse extends BaseMessage {
    private boolean tokenValid;
    private String accessorId;
    private String accessToken;
    private Set<String> allowedLevels;

    public WebServiceValidateResponse() {
    }

    public WebServiceValidateResponse(boolean tokenValid) {
        this.tokenValid = tokenValid;
    }

    public WebServiceValidateResponse(boolean tokenValid, String accessorId, String accessToken, Set<String> allowedLevels) {
        this.tokenValid = tokenValid;
        this.accessorId = accessorId;
        this.accessToken = accessToken;
        this.allowedLevels = allowedLevels;
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

    public Set<String> getAllowedLevels() {
        return allowedLevels;
    }
}
