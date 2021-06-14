package store;

import model.User;
import java.util.Map;
import java.util.HashMap;

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
        return true;
    }

    /**
       "SELECT * FROM USER WHERE userId = <user_id>"
     */
    public static User retrieveUser(String userId) {
        return userStore.get(userId);
    }

    /**
     *   To verify if a user w/ given password exists
     *
     * @param userId
     * @param password
     * @return
     */
    public static boolean verifyUser(String userId, String password) {

        if (userId == null || userId.isEmpty() || password == null || password.isEmpty()) {
            return false;
        }

        return (userStore.get(userId) == null) ? false : password.compareToIgnoreCase(passwordStore.get(userId)) == 0;
    }
}
