package Data;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Central ledger for every completed checkout.
 * <p>
 *     • keeps an in‑memory <code>List&lt;Order&gt;</code> (static)<br>
 *     • serialises to <code>data/orders.txt</code> after each change<br>
 *     • additionally maintains buyer‑centric and seller‑centric views:<br>
 *       <code>data/purchases.txt</code>  (one line per order, grouped by buyer)<br>
 *       <code>data/sales.txt</code>      (one line per order, grouped by seller)
 */
public final class OrderStore {

    /* ----------------------------------------------------------- */
    private static final Path ORDERS_FILE    = Paths.get("data", "orders.txt");
    private static final Path PURCHASES_FILE = Paths.get("data", "purchases.txt");
    private static final Path SALES_FILE     = Paths.get("data", "sales.txt");

    private static final String HEADER =
            "# id,buyerId,sellerIds(';'),timestamp,totalPrice,bookIds(';')\n";

    private static final DateTimeFormatter FMT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static final List<Order> log = new ArrayList<>();
    /* ----------------------------------------------------------- */

    /* ----- public API ------------------------------------------ */

    /** load orders.txt into memory (idempotent; called once in BookListingManager ctor) */
    public static synchronized void load() throws IOException {
        ensureFile(ORDERS_FILE);
        for (String ln : Files.readAllLines(ORDERS_FILE)) {
            if (ln.isBlank() || ln.startsWith("#")) continue;
            log.add(fromCsv(ln));
        }
    }

    /** add a freshly‑completed order and persist all three CSV views */
    public static synchronized void add(Order o) {
        log.add(o);
        persistAll();
    }

    /** Master list (immutable copy) */
    public static synchronized List<Order> getAll() {
        return List.copyOf(log);
    }

    public static synchronized int size() { return log.size(); }

    /** orders placed by one buyer */
    public static synchronized List<Order> findByBuyer(String buyerId) {
        return filter(o -> o.getBuyer().getAsuId().equals(buyerId));
    }

    /** orders that include at least one book sold by given seller */
    public static synchronized List<Order> findBySeller(String sellerId) {
        return filter(o -> o.getBooks().stream()
                            .anyMatch(b -> sellerId.equals(b.getSellerId())));
    }

    /* ----- private helpers ------------------------------------- */

    private static List<Order> filter(Predicate<Order> p) {
        return log.stream().filter(p).toList();
    }

    private static void persistAll() {
        try {
            saveView(ORDERS_FILE,    log);                           // master
            saveView(PURCHASES_FILE, log);                           // buyer view
            // seller view: one order may belong to multiple sellers – duplicate ok
            saveView(SALES_FILE,     log);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void saveView(Path file, Collection<Order> orders) throws IOException {
        ensureFile(file);
        List<String> out = new ArrayList<>();
        out.add(HEADER.trim());
        orders.stream().map(OrderStore::toCsv).forEach(out::add);
        Files.write(file, out);
    }

    private static void ensureFile(Path p) throws IOException {
        if (!Files.exists(p.getParent())) Files.createDirectories(p.getParent());
        if (!Files.exists(p)) Files.write(p, HEADER.getBytes());
    }

    /* ----- CSV helpers ----------------------------------------- */

    private static Order fromCsv(String ln) {
        String[] f = ln.split(",", -1);
        int id = Integer.parseInt(f[0]);
        User buyer = new User(f[1], "", User.Role.BUYER);

        // sellerIds not needed to rebuild Order object itself, so skip f[2]
        LocalDateTime ts = LocalDateTime.parse(f[3], FMT);
        double total = Double.parseDouble(f[4]);   // we recompute anyway but keep for completeness

        List<Book> books = Arrays.stream(f[5].split(";"))
                                 .map(bookId -> {
                                     try { return BookStore.load().get(bookId); }
                                     catch (IOException e) { return null; }
                                 })
                                 .filter(Objects::nonNull)
                                 .toList();

        Order o = new Order(buyer, books);
        o.setOrderId(id);
        o.setTimestamp(ts);
        return o;
    }

    private static String toCsv(Order o) {
        String sellerIds = o.getBooks().stream()
                             .map(Book::getSellerId)
                             .distinct()
                             .collect(Collectors.joining(";"));
        String bookIds   = o.getBooks().stream()
                             .map(Book::getId)
                             .collect(Collectors.joining(";"));

        return String.join(",",
                Integer.toString(o.getOrderId()),
                o.getBuyer().getAsuId(),
                sellerIds,
                o.getTimestamp().format(FMT),
                String.valueOf(o.getTotalPrice()),
                bookIds);
    }

    private OrderStore() {}
}
