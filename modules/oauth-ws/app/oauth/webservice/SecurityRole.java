package oauth.webservice;

import be.objectify.deadbolt.core.models.Role;

public class SecurityRole implements Role{

    public String roleName;
    
    public SecurityRole(String roleName) {
    	this.roleName = roleName;
	}

    public String getRoleName(){
        return roleName;
    }

    @Override
    public String getName() {
        return roleName;
    }
}
