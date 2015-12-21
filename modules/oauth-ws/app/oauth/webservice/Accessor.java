package oauth.webservice;

import be.objectify.deadbolt.core.models.Permission;
import be.objectify.deadbolt.core.models.Role;
import be.objectify.deadbolt.core.models.Subject;
import common.models.AbstractModel;

import java.util.List;
import java.util.stream.Collectors;

public class Accessor extends AbstractModel implements Subject {
    protected String accessorId;
    protected String accessToken;
    protected List<SecurityRole> allowedLevels;

    public Accessor(String accessorId, String accessToken, List<SecurityRole> allowedLevels) {
        this.accessorId = accessorId;
        this.accessToken = accessToken;
        this.allowedLevels = allowedLevels;
    }

    public List<String> getAllowedLevels() {
        return allowedLevels.stream().map(allowedLevel -> allowedLevel.roleName).collect(Collectors.toList());
    }

    @Override
    public String getIdentifier() {
        return null;
    }

    @Override
    public List<? extends Role> getRoles() {
        return allowedLevels;
    }

    @Override
    public List<? extends Permission> getPermissions() {
        return null;
    }

    public String getAccessorId() {
        return accessorId;
    }

    public void setAccessorId(String accessorId) {
        this.accessorId = accessorId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setAllowedLevels(List<SecurityRole> allowedLevels) {
        this.allowedLevels = allowedLevels;
    }
}
