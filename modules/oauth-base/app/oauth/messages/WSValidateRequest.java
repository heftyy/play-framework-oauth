package oauth.messages;

public class WSValidateRequest extends BaseMessage {
    private String accessToken;
    private String requestedScope;
    private String domain;

    public WSValidateRequest() {
    }

    public WSValidateRequest(String accessToken, String requestedScope, String domain) {
        this.accessToken = accessToken;
        this.requestedScope = requestedScope;
        this.domain = domain;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRequestedScope() {
        return requestedScope;
    }

    public String getDomain() {
        return domain;
    }
}
