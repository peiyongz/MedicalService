package service;

import model.User;
import security.Ops;
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
     *
     *    Create user
     *
     * @param user
     * @param password
     * @return
     */
    public static boolean createUser(User user, String password) {
        //User creation does not need authorization
        return UserStore.createUser(user, password);
    }

    /**
     *
     *    Retrieve user
     *
     * @param requesterToken
     * @param userId
     * @return
     */
    public static User retrieveUser(Token requesterToken, String userId) {

        if (authorize(requesterToken, userId, Ops.RETRIEVE)) {
            return UserStore.retrieveUser(userId);
        }

        return null;
    }

    /**
     *
     *    Update user
     *
     * @param token
     * @param user
     * @param password
     * @return
     */
    public static boolean updateUser(Token token, User user, String password) {

        if (authorize(token, user.getUserId(), Ops.RETRIEVE)) {
            return UserStore.updateUser(user, password);
        }

        return false;
    }

    /**
     *
     *    Authorization
     *
     * @param token
     * @param userId
     * @param ops
     * @return
     */
    private static boolean authorize(Token token, String userId, Ops ops) {

        // patient/doctor is allowed for their own records
        if (userId.compareToIgnoreCase(token.getUserId()) == 0) {
            return true;
        }

        // doctor is allowed on her patient's
        return token.getIamRole().equals(IAMRole.DOCTOR) &&
               DoctorPatientStore.IsDocPatient(token.getUserId(), userId);
    }
}
