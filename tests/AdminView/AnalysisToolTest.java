package AdminView;

import Data.Book;
import Data.Order;
import Data.User;
import Data.UserStore;

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

            assertEquals(5, AnalysisTool.getTotalUsers());

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
            assertEquals(2, AnalysisTool.getTotalUsers(User.Role.SELLER));
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
        TransactionLog.clear();
        addTestOrders();

        assertEquals(4, AnalysisTool.getTotalOrders());
    }

    @Test
    void getTotalRevenue() {
        TransactionLog.clear();
        addTestOrders();

        double total = (10.99 * 3) + (5.99 * 5);

        assertEquals(total, AnalysisTool.getTotalRevenue());
    }

    @Test
    void getAverageRevenue() {
        TransactionLog.clear();
        addTestOrders();

        double total = (10.99 * 3) + (5.99 * 5);
        double avg = total / 4;

        assertEquals(avg, AnalysisTool.getAverageRevenue());
    }

    @Test
    void getBestSellingBook() {
        TransactionLog.clear();
        addTestOrders();

        Book b2 = new Book("book2", "author", 2000,
                Book.Category.SCIENCE, Book.Condition.NEW,
                5.99, "124");

        assertEquals(b2, AnalysisTool.getBestSellingBook());
    }

    @Test
    void getBestSellingCategory() {
        TransactionLog.clear();
        addTestOrders();

        assertEquals(Book.Category.SCIENCE, AnalysisTool.getBestSellingCategory());
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
    void getTotalBooksAvailable(){
        //TODO
        fail("Not implemented yet");
    }

    @Test
    void getAvgBookPrice() {
        //TODO
        fail("Not implemented yet");
    }

    //----------------
    //HELPER FUNCTIONS
    //----------------

    /** 5 users,
     *  2 buyers,
     *  2 sellers,
     *  1 admin
     */
    private void saveTestUsers() throws IOException {
        List<User> users = new ArrayList<>();

        users.add(new User("buyer1", "word", User.Role.BUYER));
        users.add(new User("buyer2", "word", User.Role.BUYER));
        users.getLast().setSuspended(true);

        users.add(new User("seller1", "word", User.Role.SELLER));

        users.add(new User("seller2", "word", User.Role.SELLER));
        users.getLast().setSuspended(true);

        users.add(new User("admin1", "word", User.Role.ADMIN));


        UserStore.save(users);
    }

    /**
     * 4 orders
     */
    private void addTestOrders(){
        //create book list
        List<Book> books = new ArrayList<>();

        Book b1 = new Book("book", "author", 2000,
                Book.Category.OTHER, Book.Condition.NEW,
                10.99, "123");

        Book b2 = new Book("book2", "author", 2000,
                Book.Category.SCIENCE, Book.Condition.NEW,
                5.99, "124");

        books.add(b1);
        books.add(b2);

        //create users
        User u1 = new User("buyer1", "word", User.Role.BUYER);
        User u2 = new User("buyer2", "word", User.Role.BUYER);
        User u3 = new User("buyer3", "word", User.Role.BUYER);

        //create orders and add to transactionLog
        TransactionLog.add(new Order(u1, books)); //10.99 + 5.99
        TransactionLog.add(new Order(u2, books)); //10.99 + 5.99

        books.add(b2);
        TransactionLog.add(new Order(u3, books)); //10.99 + 5.99 + 5.99

        books.removeFirst();
        books.removeLast();
        TransactionLog.add(new Order(u3, books)); //5.99
    }
}