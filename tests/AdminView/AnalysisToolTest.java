package AdminView;

import Data.Book;
import Data.Order;
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
    void get() {
        TransactionLog.clear();
        addTestOrders();

        assertEquals("123", AnalysisTool.getHighestGrossingSeller());
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
    void getHighestGrossingCategory() {
        TransactionLog.clear();
        addTestOrders();

        assertEquals(Book.Category.OTHER, AnalysisTool.getHighestGrossingCategory());
    }

    //---------------
    //Book analysis
    //---------------
    @Test
    void getTotalBooks() {
        BookListingManager.clear();
        addTestBooks();

        assertEquals(5, AnalysisTool.getTotalBooks());

        BookListingManager.clear();
    }

    @Test
    void getTotalBooksAvailable(){
        BookListingManager.clear();
        addTestBooks();

        assertEquals(2, AnalysisTool.getTotalBooks(true));
        assertEquals(3, AnalysisTool.getTotalBooks(false));

        BookListingManager.clear();
    }

    @Test
    void getAvgBookPrice() {
        double total = 10.99 + (5.99 * 3) + 6.99;
        double avg = total / 5;

        BookListingManager.clear();
        addTestBooks();

        assertEquals(avg, AnalysisTool.getAvgBookPrice());

        BookListingManager.clear();
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

    /**
     * 5 books, 2 sold
     */
    private void addTestBooks(){
        Book b1 = new Book("book1", "author", 2000,
                Book.Category.OTHER, Book.Condition.NEW,
                10.99, "123");

        Book b2 = new Book("book2", "author", 2000,
                Book.Category.SCIENCE, Book.Condition.NEW,
                5.99, "124");

        Book b3 = new Book("book3", "author", 2000,
                Book.Category.SCIENCE, Book.Condition.NEW,
                6.99, "124");

        Book b4 = new Book("book4", "author2", 2000,
                Book.Category.SCIENCE, Book.Condition.NEW,
                5.99, "125");

        Book b5 = new Book("book5", "author2", 2000,
                Book.Category.MATH, Book.Condition.NEW,
                5.99, "126");

        BookListingManager.createListing(b1);
        BookListingManager.createListing(b2);
        BookListingManager.createListing(b3);
        BookListingManager.createListing(b4);
        BookListingManager.createListing(b5);

        BookListingManager.markSold(b2.getId());
        BookListingManager.markSold(b5.getId());
    }
}