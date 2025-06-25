package LoginPage;

import Data.User;
import Data.UserStore;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/** Stateless façade: keeps an in-memory cache but always syncs to users.txt. */
public final class AuthenticationService {

    private static Map<String,User> CACHE;

    /* lazy-load users.txt exactly once */
    private static Map<String,User> users() {
        if (CACHE == null) {
            try { CACHE = UserStore.load(); }
            catch (IOException ex) {
                ex.printStackTrace();
                CACHE = new HashMap<>();
            }
            /* seed demo accounts if file was empty */
            if (CACHE.isEmpty()) {
                CACHE.put("1111111111", new User("1111111111","password123",User.Role.BUYER));
                CACHE.put("2222222222", new User("2222222222","admin123",   User.Role.ADMIN));
                CACHE.put("3333333333", new User("3333333333","seller123",  User.Role.SELLER));
                CACHE.put("4444444444", new User("4444444444","buyer123",   User.Role.BUYER));
                persist();   // write seed to disk
            }
        }
        return CACHE;
    }

    /* ---------- public API ---------- */

    /** Returns the matching User or null. */
    public static User authenticate(String asuId, String pw, User.Role role) {
        User u = users().get(asuId);
        if (u == null)                    return null;
        if (!u.getPassword().equals(pw))  return null;
        if (u.isSuspended())              return null;
        if (u.getRole() != role)          return null;
        return u;
    }

    /** Attempts to register a brand-new account. */
    public static boolean register(String asuId, String pw, User.Role role) {
        if (users().containsKey(asuId)) return false;
        User u = new User(asuId, pw, role);
        users().put(asuId, u);
        persist();
        return true;
    }

    public static boolean userExists(String asuId) { return users().containsKey(asuId); }

    /* ---------- helper ---------- */
    private static void persist() {
        try { UserStore.save(users().values()); }
        catch (IOException ex) { ex.printStackTrace(); }
    }

    private AuthenticationService() {}

    //sign-up from UI
    
}
