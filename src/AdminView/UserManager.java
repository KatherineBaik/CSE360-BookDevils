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
        return userList;
    }

    /** Returns the number of currently registered users. */
    public static int getNumUsers(){
        return userList.size();
    }

    /** Returns the user corresponding to the ASU ID.
     * If the user doesn't exist, returns null. */
    public static User findUser(String ID){
        return userList.get(ID);
    }

    /** Returns true if the user is successfully deleted.
     * If the user does not exist, returns false */
    public static boolean removeUser(String ID){
        return userList.remove(ID) != null;
    }

    //TODO: Edit a user???
}
