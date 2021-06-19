package service;

import model.User;
import security.Ops;
import security.Resource;
import store.DoctorPatientStore;
import store.UserStore;
import security.Token;
import security.IAMRole;

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
        //User signup does not need auth
        return UserStore.createUser(user, password);
    }

    /**
     *   Get a User
     *
     * @param userId
     * @return
     */
    public static User retrieveUser(Token token, String userId) {

        if (isPermitted(token, userId, Ops.RETRIEVE)) {
            return UserStore.retrieveUser(userId);
        }

        return null;

    }

    /**
     *  Update a User
     *
     * @param user
     * @return
     */
    public static boolean updateUser(Token token, User user, String password) {

        if (isPermitted(token, user.getUserId(), Ops.RETRIEVE)) {
            return UserStore.updateUser(user, password);
        }

        return false;
    }

    /**
     *
     * @param token
     * @param userId
     * @return
     */
    private static boolean isPermitted(Token token, String userId, Ops ops) {

        // patient/doctor is allowed for their own records
        if (userId.compareToIgnoreCase(token.getUserId()) == 0) {
            return true;
        }

        // doctor is allowed on her patient's
        return token.getIamRole().equals(IAMRole.DOCTOR) &&
               DoctorPatientStore.IsDocPatient(token.getUserId(), userId);
    }
}
