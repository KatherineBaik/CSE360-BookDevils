package AdminView;

import Data.Book;
import Data.Order;

import Data.User;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TransactionLogTest {
    @Test
    void saveAndloadData() {
        try{
            //SETUP
            TransactionLog.clear();
            List<Order> orderList = new ArrayList<>();

            User user1 = new User("1", "word", User.Role.BUYER);
            User user2 = new User("2", "pass", User.Role.BUYER);
            List<Book> books = createTestBookList();

            orderList.add(new Order(user1, books));
            orderList.add(new Order(user2, books));

            for(Order o : orderList){
                TransactionLog.add(o);
            }

            TransactionLog.saveData(); //THROWS ERROR here
            TransactionLog.loadData();

            //TEST
            assertEquals(orderList.size(), TransactionLog.size()); //make sure size is the same

            assertEquals(orderList, TransactionLog.getOrderList());
        }
        catch (Exception ex){
            ex.printStackTrace();
            fail();
        }
    }

    @Test
    void size() {
        //SETUP
        TransactionLog.clear();
        List<Order> orderList = new ArrayList<>();

        User user1 = new User("1", "word", User.Role.BUYER);
        User user2 = new User("2", "pass", User.Role.BUYER);
        List<Book> books = createTestBookList();

        orderList.add(new Order(user1, books));
        orderList.add(new Order(user2, books));

        for(Order o : orderList){
            TransactionLog.add(o);
        }

        //TEST
        assertEquals(orderList.size(), TransactionLog.size());
    }

    @Test
    void add() {
        //SETUP
        TransactionLog.clear();
        List<Order> orderList = new ArrayList<>();

        User user1 = new User("1", "word", User.Role.BUYER);
        User user2 = new User("2", "pass", User.Role.BUYER);
        List<Book> books = createTestBookList();

        orderList.add(new Order(user1, books));
        orderList.add(new Order(user2, books));

        for(Order o : orderList){
            TransactionLog.add(o);
        }

        //TEST
        assertEquals(orderList, TransactionLog.getOrderList());
    }

    @Test
    void byBuyer() {
        //SETUP
        TransactionLog.clear();
        List<Order> orderList = new ArrayList<>();

        User user1 = new User("1", "word", User.Role.BUYER);
        User user2 = new User("2", "pass", User.Role.BUYER);
        List<Book> books = createTestBookList();

        orderList.add(new Order(user1, books));
        orderList.add(new Order(user1, books));
        orderList.add(new Order(user2, books));

        for(Order o : orderList){
            TransactionLog.add(o);
        }

        //TEST
        orderList.remove(2); //remove the last one, which has a different user
        assertEquals(orderList, TransactionLog.byBuyer(user1.getAsuId()));
    }

    @Test
    void sortByDate() {
        //SETUP
        TransactionLog.clear();
        List<Order> orderList = new ArrayList<>();

        User user1 = new User("1", "word", User.Role.BUYER);
        User user2 = new User("2", "pass", User.Role.BUYER);
        List<Book> books = createTestBookList();

        Order order1 = new Order(user1, books); //first
        Order order2 = new Order(user1, books); //second
        Order order3 = new Order(user2, books); //third

        //add them out of order
        orderList.add(order2);
        orderList.add(order1);
        orderList.add(order3);

        for(Order o : orderList){
            TransactionLog.add(o);
        }

        //TEST
        List<Order> expected = new ArrayList<>();
        expected.add(order1);
        expected.add(order2);
        expected.add(order3);

        //makes sure the unsorted lists are equal before the sort
        assertEquals(orderList, TransactionLog.getOrderList());

        TransactionLog.sortByDate();

        //make sure it matches the expected, not the unsorted
        assertNotEquals(orderList, TransactionLog.getOrderList());
        assertEquals(expected, TransactionLog.getOrderList());

        //make sure ascending order, not descending order
        assertNotEquals(expected.reversed(), TransactionLog.getOrderList());
    }

    @Test
    void sortByValue() {
        //SETUP
        TransactionLog.clear();
        List<Order> orderList = new ArrayList<>();

        User user1 = new User("1", "word", User.Role.BUYER);
        User user2 = new User("2", "pass", User.Role.BUYER);
        List<Book> books = createTestBookList();

        //two books
        Order order1 = new Order(user1, books);

        books.removeLast(); //one book
        Order order2 = new Order(user1, books);

        books.removeLast(); //zero books
        Order order3 = new Order(user2, books);

        //add them out of order
        orderList.add(order2);
        orderList.add(order1);
        orderList.add(order3);

        for(Order o : orderList){
            TransactionLog.add(o);
        }

        //TEST
        List<Order> expected = new ArrayList<>();
        expected.add(order1);
        expected.add(order2);
        expected.add(order3);

        //makes sure the unsorted lists are equal before the sort
        assertEquals(orderList, TransactionLog.getOrderList());

        TransactionLog.sortByValue();

        //make sure it matches the expected, not the unsorted
        assertNotEquals(orderList, TransactionLog.getOrderList());
        assertEquals(expected, TransactionLog.getOrderList());

        //make sure descending, not ascending order
        assertNotEquals(expected.reversed(), TransactionLog.getOrderList());
    }

    private List<Book> createTestBookList(){
        List<Book> books = new ArrayList<>();

        Book b1 = new Book("t", "a",
                2000, Book.Category.OTHER, Book.Condition.NEW,
                10.99, "123");

        Book b2 = new Book("title", "author",
                2000, Book.Category.OTHER, Book.Condition.NEW,
                10.99, "111");

        books.add(b1);
        books.add(b2);

        return books;
    }
}