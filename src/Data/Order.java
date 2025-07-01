package Data;

import java.time.LocalDateTime;
import java.util.List;

/** Immutable record of a completed checkout. */
public class Order {

    /* ------------------------------------------------------------ */
    private static int nextId = 1;

    private int                   orderId;
    private final User            buyer;
    private final List<Book>      books;
    private final double          totalPrice;
    private LocalDateTime         timestamp;
    /* ------------------------------------------------------------ */

    public Order(User buyer, List<Book> books) {
        this.orderId   = nextId++;
        this.buyer     = buyer;
        this.books     = List.copyOf(books);
        this.totalPrice = books.stream()
                               .mapToDouble(Book::getSellingPrice)
                               .sum();
        this.timestamp = LocalDateTime.now();
    }

    /* ---------------- getters ---------------- */
    public int            getOrderId()  { return orderId; }
    public User           getBuyer()    { return buyer; }
    public List<Book>     getBooks()    { return books; }
    public double         getTotalPrice(){ return totalPrice; }
    public LocalDateTime  getTimestamp(){ return timestamp; }
    public void setOrderId(int id) {          // needed by OrderStore.load()
    // reflection-free tweak – use field via reflection or make orderId non-final
        this.orderId = id;
    }
    public void setTimestamp(LocalDateTime ts) {
        this.timestamp = ts;
    }

    /* ---------------- convenience ------------ */
    @Override public String toString() {
        return "Order#" + orderId + " – " + books.size() +
               " item(s) – $" + "%.2f".formatted(totalPrice);
    }
}
