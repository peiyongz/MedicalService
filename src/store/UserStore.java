package store;

import model.Audit;
import model.User;
import java.util.Map;
import java.util.HashMap;

import security.IAMRole;
import security.Ops;
import security.Resource;
import store.AuditStore;

/**
 *  An implementation of the persistent layer for User in lieu of RDBMS
 *
 */
public class UserStore {

    private static Map<String, User> userStore;
    private static Map<String, String> passwordStore;

    static {
        userStore = new HashMap<String, User>();
        passwordStore = new HashMap<String, String>();
    }

    /**
        "INSERT USER ..."
     */
    public static boolean createUser(User user, String password) {

        if (userStore.containsKey(user.getUserId())) {
            return false;
        }

        userStore.put(user.getUserId(), user);
        passwordStore.put(user.getUserId(), password);
        addAudit(user, Ops.CREATE.name());
        return true;
    }

    /**
        "UPDATE USER ... WHERE ..."
     */
    public static boolean updateUser(User user, String password) {

        if (!userStore.containsKey(user.getUserId())) {
            return false;
        }

        userStore.put(user.getUserId(), user);
        passwordStore.put(user.getUserId(), password);
        addAudit(user, Ops.UPDATE.name());
        return true;
    }

    /**
       "SELECT * FROM USER WHERE userId = <user_id>"
     */
    public static User retrieveUser(String userId) {
        //we don't audit retrieval
        return userStore.get(userId);
    }

    /**
     *   To check if a user a patient
     *
     * @param userId
     * @return
     */

    public static boolean isPatient(String userId) {
        return (userStore.containsKey(userId) && userStore.get(userId).getIamRole() == IAMRole.PATIENT);
    }

    public static boolean verifyUser(String userId, String password) {

        if (userId == null || userId.isEmpty() || password == null || password.isEmpty()) {
            return false;
        }

        return (userStore.get(userId) == null) ? false : password.compareToIgnoreCase(passwordStore.get(userId)) == 0;
    }

    private static void addAudit(User user, String ops) {
        AuditStore.add(new Audit(Resource.USER.name(), ops, user.getKey(), user.toString(), user.getUserId()));
    }
}
