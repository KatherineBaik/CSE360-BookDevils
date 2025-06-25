package AdminView;

import Data.User;

import java.io.IOException;
import java.util.Map;

public class UserManager {
    private static Map<String, User> userList;

    /** Loads all user data stored in file.
     * NOTE: This should be used at the start of the application. */
    public static void loadData() throws IOException{
        userList = Data.UserStore.load();
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
        if(userList.remove(ID) == null){
            return false;
        }
        else{
            return true;
        }
    }

    //TODO: Edit a user???
}
