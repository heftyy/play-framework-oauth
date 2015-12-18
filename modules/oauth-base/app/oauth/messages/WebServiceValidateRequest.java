package oauth.messages;

public class WebServiceValidateRequest extends BaseMessage {
    private String accessorId;
    private String accessToken;
    private String clientRemoteAddress;
    private String requestedScope;
    private String domain;

    public WebServiceValidateRequest() {
    }

    public WebServiceValidateRequest(String accessorId, String accessToken, String clientRemoteAddress, String requestedScope, String domain) {
        this.accessorId = accessorId;
        this.accessToken = accessToken;
        this.clientRemoteAddress = clientRemoteAddress;
        this.requestedScope = requestedScope;
        this.domain = domain;
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

    public String getRequestedScope() {
        return requestedScope;
    }

    public String getDomain() {
        return domain;
    }
}
