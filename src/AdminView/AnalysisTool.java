package AdminView;

import Data.User;

import java.util.Collection;

public class AnalysisTool {

    //---------------
    //User analysis
    //---------------

    /** Returns the total number of all currently registered users. */
    public static int getTotalUsers(){
        return UserManager.getNumUsers();
    }

    /** Returns the total number of users matching role. */
    public static int getTotalUsers(User.Role role){
        Collection<User>list = UserManager.getUserList().values();
        int total = 0;

        for(User user: list){
            if(user.getRole() == role){
                total++;
            }
        }

        return total;
    }

    /** Returns the number of suspended users. */
    public static int getTotalSuspendedUsers(){
        Collection<User>list = UserManager.getUserList().values();
        int total = 0;

        for(User user: list){
            if(user.isSuspended()){
                total++;
            }
        }

        return total;
    }
    //---------------
    //Sales analysis
    //---------------



    //---------------
    //Book analysis
    //---------------

}
