package oauth.models;

import common.models.AbstractModel;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OAuthScope extends AbstractModel {
    private Long id;
	private String scopeUrl;
	private String method;
	private String returns;
	private String arguments;
	private String levelName;
	private Long apiId;
	private OAuthLevel level;

	/**
	 * Create a single scope, the data will be added using setters.
	 */
	OAuthScope() { }

	/**
	 * POSSIBLY NOT NEEDED ANYMORE. Create a single scope.
	 */
	public OAuthScope(String url, String method, String returns, String arguments, String level, Long apiId) {
		this.scopeUrl = url;
		this.method = method;
		this.returns = returns;
		this.arguments = arguments;
		this.apiId = apiId;
		this.levelName = level;
	}
	
	public OAuthScope(String url, String method, String returns, String arguments, OAuthLevel l) {
		this.scopeUrl = url;
		this.method = method;
		this.returns = returns;
		this.arguments = arguments;
		this.level = l;
	}
	
	public OAuthScope(String url, String method, String returns, String arguments) {
		this.scopeUrl = url;
		this.method = method;
		this.returns = returns;
		this.arguments = arguments;
	}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getScopeUrl() {
        return scopeUrl;
    }

    public void setScopeUrl(String scopeUrl) {
        this.scopeUrl = scopeUrl;
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

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public Long getApiId() {
        return apiId;
    }

    public void setApiId(Long apiId) {
        this.apiId = apiId;
    }

    public OAuthLevel getLevel() {
        return level;
    }

    public void setLevel(OAuthLevel level) {
        this.level = level;
    }
}
