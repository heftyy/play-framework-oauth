package oauth.messages;

public class AccessTokenFailure extends BaseMessage {
    private String error;
    private String description;

    public AccessTokenFailure() {
    }

    public AccessTokenFailure(String error) {
        this.error = error;
    }

    public AccessTokenFailure(String error, String description) {
        this.error = error;
        this.description = description;
    }

    public String getError() {
        return error;
    }

    public String getDescription() {
        return description;
    }
}
