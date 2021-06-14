package service;

import model.User;
import store.UserStore;

/**
 *   User Service
 *
 *   Web Service Endpoint Handler
 */
public class UserService {

    /**
     *   Create a user
     *
     * @param user
     * @return
     */
    public static boolean createUser(User user, String password) {
        return UserStore.createUser(user, password);
    }

    /**
     *   Get a User for UI to present
     *
     * @param userId
     * @return
     */
    public static User retrieveUser(String userId) {
        return UserStore.retrieveUser(userId);
    }

    /**
     *  Update a User
     *
     * @param user
     * @return
     */
    public static boolean updateUser(User user, String password) {
        return UserStore.updateUser(user, password);
    }

    //public static
}
