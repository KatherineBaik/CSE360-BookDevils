package AdminView;

import Data.User;
import Data.UserStore;

import java.io.IOException;
import java.util.Map;

public class UserManager {
    private static Map<String, User> userList;

    /** Loads all user data stored in file.
     * NOTE: Must be used before using this class. */
    public static void loadData() throws IOException{
        if(userList != null) userList.clear();
        userList = UserStore.load();
    }

    /** Saves all user data to file.
     * NOTE: Should be used at before application closes, or when updating data in the list. */
    public static void saveData() throws IOException{
        UserStore.save(userList.values());
    }

    //----------------------

    /** Get the underlying map used by this class. */
    public static Map<String, User> getUserList(){
        try {
            loadData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userList;
    }

    /** Returns the number of currently registered users. */
    public static int getNumUsers(){
        return userList.size();
    }

    /** Finds and suspends/unsuspends a user. Returns false if user doesn't exist. */
    public static boolean setSuspended(String userID, boolean suspend) throws IOException{
        User user = userList.get(userID);
        if(user != null){
            user.setSuspended(suspend);
            saveData();
            return true;
        }
        else{
            return false;
        }
    }

    /** Returns true if the user is successfully deleted.
     * If the user does not exist, returns false */
    public static boolean removeUser(String ID) throws IOException{
        boolean success = userList.remove(ID) != null;
        saveData();
        return success;
    }
}
