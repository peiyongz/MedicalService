package security;

import store.IAMStore;
import store.UserStore;

public class IAMService {

    public static Token login(String userId, String password) {

        if (!UserStore.verifyUser(userId, password)) {
            return null;
        }

        return IAMStore.getToken(userId);
    }
}
