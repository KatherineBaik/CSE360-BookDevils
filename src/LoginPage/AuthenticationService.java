package LoginPage;

import java.util.HashMap;
import java.util.Map;

public class AuthenticationService {

    static Map<String, UserInfo> users = new HashMap<>();

    static class UserInfo {
        String password;
        String role;

        UserInfo(String password, String role) {
            this.password = password;
            this.role = role;
        }
    }

    static void initializeUsers() {
        users.put("1234567890", new UserInfo("password123", "Buyer"));
        users.put("0987654321", new UserInfo("admin123", "Admin"));
        users.put("1122334455", new UserInfo("seller123", "Seller"));
        users.put("2233445566", new UserInfo("buyer123", "Buyer"));
    }

    static boolean authenticate(String asuId, String password, String role) {
        if (users.isEmpty()) {
            initializeUsers();
        }

        if (asuId == null || password == null || role == null) {
            return false;
        }

        UserInfo user = users.get(asuId);

        if (user == null) {
            return false;
        }

        return user.password.equals(password) && user.role.equals(role);
    }

    static void addUser(String asuId, String password, String role) {
        users.put(asuId, new UserInfo(password, role));
    }

    static boolean userExists(String asuId) {
        return users.containsKey(asuId);
    }
}
