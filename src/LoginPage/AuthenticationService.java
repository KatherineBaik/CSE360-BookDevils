package LoginPage;

import java.util.ArrayList;
import java.util.List;

public class AuthenticationService {

    static List<UserInfo> users = new ArrayList<>();

    static class UserInfo {
        String asuId;
        String password;
        String role;

        UserInfo(String asuId, String password, String role) {
            this.asuId = asuId;
            this.password = password;
            this.role = role;
        }
    }

    static void initializeUsers() {
        users.add(new UserInfo("1111111111", "password123", "Buyer"));
        users.add(new UserInfo("2222222222", "admin123", "Admin"));
        users.add(new UserInfo("3333333333", "seller123", "Seller"));
        users.add(new UserInfo("4444444444", "buyer123", "Buyer"));
    }

    static boolean authenticate(String asuId, String password, String role) {
        if (users.isEmpty()) {
            initializeUsers();
        }

        if (asuId == null || password == null || role == null) {
            return false;
        }

        for (UserInfo user : users) {
            if (user.asuId.equals(asuId) && 
                user.password.equals(password) && 
                user.role.equals(role)) {
                return true;
            }
        }

        return false;
    }

    static void addUser(String asuId, String password, String role) {
        users.add(new UserInfo(asuId, password, role));
    }

    static boolean userExists(String asuId) {
        for (UserInfo user : users) {
            if (user.asuId.equals(asuId)) {
                return true;
            }
        }
        return false;
    }
}
