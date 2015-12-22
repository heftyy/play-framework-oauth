package oauth.models;

import common.models.AbstractModel;

public class OAuthUrlPattern extends AbstractModel {
    private Long id;
	private String pattern;
	private String method;
	private String returns;
	private String arguments;
	private OAuthScope scope;

	OAuthUrlPattern() { }

    public OAuthUrlPattern(String pattern, String method, String returns, String arguments, OAuthScope scope) {
        this.pattern = pattern;
        this.method = method;
        this.returns = returns;
        this.arguments = arguments;
        this.scope = scope;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getReturns() {
        return returns;
    }

    public void setReturns(String returns) {
        this.returns = returns;
    }

    public String getArguments() {
        return arguments;
    }

    public void setArguments(String arguments) {
        this.arguments = arguments;
    }

    public OAuthScope getScope() {
        return scope;
    }

    public void setScope(OAuthScope scope) {
        this.scope = scope;
    }
}
