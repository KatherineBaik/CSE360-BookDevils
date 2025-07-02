package AdminView;

import Data.User;
import Data.UserStore;
import SellerView.BookListingManager;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AnalysisToolTest {
    //---------------
    //User analysis
    //---------------

    //NOTE: Delete users.txt before running test file

    @Test
    void getTotalUsers() {
        try{
            saveTestUsers();
            UserManager.loadData();

            assertEquals(4, AnalysisTool.getTotalUsers());

        }
        catch (Exception ex){
            ex.printStackTrace();
            fail();
        }
    }

    @Test
    void GetTotalUsersByRole() {
        try{
            saveTestUsers();
            UserManager.loadData();

            assertEquals(2, AnalysisTool.getTotalUsers(User.Role.BUYER));
            assertEquals(1, AnalysisTool.getTotalUsers(User.Role.SELLER));
            assertEquals(1, AnalysisTool.getTotalUsers(User.Role.ADMIN));
        }
        catch (Exception ex){
            ex.printStackTrace();
            fail();
        }
    }

    @Test
    void getTotalSuspendedUsers() {
        try{
            saveTestUsers();
            UserManager.loadData();

            assertEquals(2, AnalysisTool.getTotalSuspendedUsers());
        }
        catch (Exception ex){
            ex.printStackTrace();
            fail();
        }
    }

    //---------------
    //Sales analysis
    //---------------
    @Test
    void getTotalOrders() {
        //TODO
        fail("Not implemented yet");
    }

    @Test
    void getTotalRevenue() {
        //TODO
        fail("Not implemented yet");
    }

    @Test
    void getAverageRevenue() {
        //TODO
        fail("Not implemented yet");
    }

    @Test
    void getBestSellingBook() {
        //TODO
        fail("Not implemented yet");
    }

    @Test
    void getBestSellingCategory() {
        //TODO
        fail("Not implemented yet");
    }

    //---------------
    //Book analysis
    //---------------
    @Test
    void getTotalBooks() {
        //TODO
        fail("Not implemented yet");
    }

    @Test
    void getAvgBookPrice() {
        //TODO
        fail("Not implemented yet");
    }

    //HELPER FUNCTIONS

    /** 4 users,
     *  2 buyers,
     *  1 seller,
     *  1 admin
     */
    private void saveTestUsers() throws IOException {
        List<User> users = new ArrayList<>();

        users.add(new User("buyer1", "word", User.Role.BUYER));
        users.add(new User("buyer2", "word", User.Role.BUYER));
        users.getLast().setSuspended(true);

        users.add(new User("seller1", "word", User.Role.SELLER));
        users.getLast().setSuspended(true);

        users.add(new User("admin1", "word", User.Role.ADMIN));


        UserStore.save(users);
    }

}