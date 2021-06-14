package security;

import java.util.Map;

public class Token {

    private String userId;
    private IAMRole iamRole;
    private Map<Resource, Permission> resourcePermMap;

    //key:    resource, such as User, Visit, etc
    //value:  permission
    //private Map<String, String>

    public Token(String userId, IAMRole iamRole, Map<Resource, Permission> permMap) {
       this.userId = userId;
       this.iamRole = iamRole;
       this.resourcePermMap = permMap;
    }

    public boolean authorize(Resource resource, Ops ops) {

       final Permission permission = resourcePermMap.get(resource);
       return permission == null? false : permission.authorize(ops);
    }

    public String getUserId() {
        return this.userId;
    }

    public IAMRole getIamRole() {
        return this.iamRole;
    }
}
