package oauth.messages;

public class AccessTokenMessage extends BaseMessage {
    private String accessorId;
    private String accessToken;
    private Integer tokenType;
    private Long expiresAt;

    public AccessTokenMessage() {
    }

    public AccessTokenMessage(String accessorId, String accessToken, Integer tokenType, Long expiresAt) {
        this.accessorId = accessorId;
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.expiresAt = expiresAt;
    }

    public String getAccessorId() {
        return accessorId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public Integer getTokenType() {
        return tokenType;
    }

    public Long getExpiresAt() {
        return expiresAt;
    }
}
