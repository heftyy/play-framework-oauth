package oauth.models.compositeid;

import java.io.Serializable;
import java.util.Objects;

import oauth.models.OAuthClient;

public class OAuthLevelID implements Serializable {
	private static final long serialVersionUID = 156487L;
	
	private String level;
	private Long apiId;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		OAuthLevelID that = (OAuthLevelID) o;
		return Objects.equals(level, that.level) &&
				Objects.equals(apiId, that.apiId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(level, apiId);
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public Long getApiId() {
		return apiId;
	}

	public void setApiId(Long apiId) {
		this.apiId = apiId;
	}

}
