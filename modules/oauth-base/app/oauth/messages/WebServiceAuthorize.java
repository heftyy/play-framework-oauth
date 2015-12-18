package oauth.messages;

public class WebServiceAuthorize extends BaseMessage {
    private String accessorId;
    private String accessToken;
    private String scope;

    public WebServiceAuthorize() {
    }

    public WebServiceAuthorize(String accessorId, String accessToken, String scope) {
        this.accessorId = accessorId;
        this.accessToken = accessToken;
        this.scope = scope;
    }

    public String getAccessorId() {
        return accessorId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getScope() {
        return scope;
    }
}
