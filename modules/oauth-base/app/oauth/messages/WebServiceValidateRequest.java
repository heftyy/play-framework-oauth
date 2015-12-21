package oauth.messages;

public class WebServiceValidateRequest extends BaseMessage {
    private String accessToken;
    private String requestedScope;
    private String domain;

    public WebServiceValidateRequest() {
    }

    public WebServiceValidateRequest(String accessToken, String requestedScope, String domain) {
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
