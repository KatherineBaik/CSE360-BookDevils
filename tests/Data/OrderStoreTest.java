package Data;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderStoreTest {
    @Test
    void add() {
        //SETUP
        User buyer1 = new User("1", "word", User.Role.BUYER);
        User buyer2 = new User("2", "word2", User.Role.BUYER);

        Book book1 = new Book("MY BOOK", "Mr. Author Man",
                2000, Book.Category.OTHER, Book.Condition.NEW,
                10.99, "00");

        List<Book> bookList = new ArrayList<>();
        bookList.add(book1);

        Order order1 = new Order(buyer1, bookList);
        Order order2 = new Order(buyer2, bookList);

        //TEST
        List<Order> expected = new ArrayList<>();
        expected.add(order1);
        expected.add(order2);

        OrderStore.add(order1);
        OrderStore.add(order2);
        List<Order> result = OrderStore.getAll();

        assertEquals(expected, result);

        //check orders.txt to make sure stuff is written in there
    }

    @Test
    void load() {
        //NOTE: Test after running add() test
        //SETUP
        User buyer1 = new User("1", "word", User.Role.BUYER);
        User buyer2 = new User("2", "word2", User.Role.BUYER);

        Book book1 = new Book("MY BOOK", "Mr. Author Man",
                2000, Book.Category.OTHER, Book.Condition.NEW,
                10.99, "00");

        List<Book> bookList = new ArrayList<>();
        bookList.add(book1);

        Order order1 = new Order(buyer1, bookList);
        Order order2 = new Order(buyer2, bookList);

        //TEST
        List<Order> expected = new ArrayList<>();
        expected.add(order1);
        expected.add(order2);

        try{
            OrderStore.load();
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
        List<Order> result = OrderStore.getAll();

        assertEquals(expected, result);

        //check orders.txt to make sure stuff is written in there
    }

    @Test
    void findByBuyer() {
        //SETUP
        User buyer1 = new User("1", "word", User.Role.BUYER);
        User buyer2 = new User("2", "word2", User.Role.BUYER);

        Book book1 = new Book("MY BOOK", "Mr. Author Man",
                2000, Book.Category.OTHER, Book.Condition.NEW,
                10.99, "00");

        List<Book> bookList = new ArrayList<>();
        bookList.add(book1);

        Order order1 = new Order(buyer1, bookList);
        Order order2 = new Order(buyer2, bookList);

        OrderStore.add(order1);
        OrderStore.add(order2);

        //TEST
        List<Order> expected1 = new ArrayList<>();
        expected1.add(order1);

        List<Order> expected2 = new ArrayList<>();
        expected2.add(order2);

        assertEquals(expected1, OrderStore.findByBuyer("1"));
        assertNotEquals(expected1, OrderStore.findByBuyer("2"));

        assertEquals(expected2, OrderStore.findByBuyer("2"));
        assertNotEquals(expected2, OrderStore.findByBuyer("1"));
    }

    @Test
    void findBySeller() {
        //SETUP
        User buyer1 = new User("1", "word", User.Role.BUYER);

        Book book1 = new Book("MY BOOK", "Mr. Author Man",
                2000, Book.Category.OTHER, Book.Condition.NEW,
                10.99, "00");
        Book book2 = new Book("MY BOOK", "Mr. Author Man",
                2000, Book.Category.OTHER, Book.Condition.NEW,
                10.99, "01");

        List<Book> bookList1 = new ArrayList<>();
        bookList1.add(book1);
        Order order1 = new Order(buyer1, bookList1);

        List<Book> bookList2 = new ArrayList<>();
        bookList2.add(book2);
        Order order2 = new Order(buyer1, bookList2);

        OrderStore.add(order1);
        OrderStore.add(order2);

        //TEST
        List<Order> expected1 = new ArrayList<>();
        expected1.add(order1);

        List<Order> expected2 = new ArrayList<>();
        expected2.add(order2);

        assertEquals(expected1, OrderStore.findBySeller("00"));
        assertNotEquals(expected1, OrderStore.findBySeller("01"));

        assertEquals(expected2, OrderStore.findBySeller("01"));
        assertNotEquals(expected2, OrderStore.findBySeller("00"));
    }
}