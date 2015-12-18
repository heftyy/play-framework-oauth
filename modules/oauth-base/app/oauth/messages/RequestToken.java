package oauth.messages;

public class RequestToken extends BaseMessage {
    private String assertion;

    public RequestToken() {
    }

    public RequestToken(String assertion) {
        this.assertion = assertion;
    }

    public String getAssertion() {
        return assertion;
    }
}
