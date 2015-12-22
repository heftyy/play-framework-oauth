package oauth.messages;

public class AccessTokenSuccess extends BaseMessage {
    private String accessorId;
    private String accessToken;
    private String tokenType;
    private Long expiresIn;

    public AccessTokenSuccess() {
    }

    public AccessTokenSuccess(String accessorId, String accessToken, String tokenType, Long expiresIn) {
        this.accessorId = accessorId;
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
    }

    public String getAccessorId() {
        return accessorId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }
}
