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
    protected List<SecurityRole> allowedScopes;

    public Accessor(String accessorId, String accessToken, List<SecurityRole> allowedScopes) {
        this.accessorId = accessorId;
        this.accessToken = accessToken;
        this.allowedScopes = allowedScopes;
    }

    public List<String> getAllowedScopes() {
        return allowedScopes.stream().map(scope -> scope.roleName).collect(Collectors.toList());
    }

    @Override
    public String getIdentifier() {
        return null;
    }

    @Override
    public List<? extends Role> getRoles() {
        return allowedScopes;
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

    public void setAllowedScopes(List<SecurityRole> allowedScopes) {
        this.allowedScopes = allowedScopes;
    }
}
