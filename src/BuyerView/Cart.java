package BuyerView;

import Data.Book;
import Data.User;
import java.util.ArrayList;
import java.util.List;

public class Cart {
    private final User buyer;
    private final List<Book> books = new ArrayList<>();

    public Cart(User buyer) { this.buyer = buyer; }

    public boolean add(Book b) {
        if (b.isSold() || books.contains(b)) return false;       // already gone or already in cart
        books.add(b);
        return true;
    }

    public void remove(Book b)  { books.remove(b); }
    public void clear()         { books.clear();   }

    public List<Book> getBooks()   { return books; }
    public User       getBuyer()   { return buyer; }
    public double     getTotal()   { return books.stream().mapToDouble(Book::getSellingPrice).sum(); }
}
