package oauth.messages;

public class AccessTokenMessage extends BaseMessage {
    private String accessToken;
    private Integer tokenType;
    private Long expiresAt;

    public AccessTokenMessage() {
    }

    public AccessTokenMessage(String accessToken, Integer tokenType, Long expiresAt) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.expiresAt = expiresAt;
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
