package Data;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


/** -------------------------------------------------------------
 *  BookStore – persists the "books available for selling" list
 * ------------------------------------------------------------- */
public class BookStore {

    private static final Path FILE = Paths.get("data", "books.txt");
    private static final String HEADER = "# id,title,author,year,category,condition,origPrice,sellPrice,sellerId,sold\n";

    /* ---------------- public API ---------------- */

    /** Returns <bookId, Book> map. */
    public static Map<String,Book> load() throws IOException {
        ensureFile();
        Map<String,Book> map = new HashMap<>();
        for (String ln : Files.readAllLines(FILE)) {
            if (ln.isBlank() || ln.startsWith("#")) continue;
            Book b = fromCsv(ln);
            map.put(b.getId(), b);
        }
        return map;
    }

    /** Persists the entire collection (atomic overwrite). */
    public static void save(Collection<Book> books) throws IOException {
        ensureFile();
        List<String> out = new ArrayList<>();
        out.add(HEADER.trim());
        books.stream().map(BookStore::toCsv).forEach(out::add);
        Files.write(FILE, out);
    }

    /** Adds a new listing, auto‑generating an id. */
    public static synchronized String add(Book b) throws IOException {
        Map<String,Book> all = load();
        String id = nextId(all.keySet());
        b.setId(id);
        all.put(id, b);
        save(all.values());
        return id;
    }

    /** Helper to flag a book as sold and persist. */
    public static synchronized void markSold(String bookId) throws IOException {
        Map<String,Book> all = load();
        Book b = all.get(bookId);
        if (b != null) {
            b.markAsSold();
            save(all.values());
        }
    }

    /* ---------------- internal helpers ---------------- */

    private static void ensureFile() throws IOException {
        if (!Files.exists(FILE.getParent())) Files.createDirectories(FILE.getParent());
        if (!Files.exists(FILE)) Files.write(FILE, HEADER.getBytes());
    }

    private static String nextId(Set<String> used) {
        int n = 1;
        while (used.contains("B" + n)) n++;
        return "B" + n;
    }

    /* CSV ⇆ Book ------------------------------------------------- */
    private static Book fromCsv(String ln) {
        String[] p = ln.split(",", -1);
        Book b = new Book(
                p[1], p[2], Integer.parseInt(p[3]),
                Book.Category.valueOf(p[4]),
                Book.Condition.valueOf(p[5]),
                Double.parseDouble(p[6]),
                p[8]                    // sellerId
        );
        b.setId(p[0]);
        b.setSellingPrice(Double.parseDouble(p[7]));
        if (Boolean.parseBoolean(p[9])) b.markAsSold();
        return b;
    }

    private static String toCsv(Book b) {
        return String.join(",",
                b.getId(), b.getTitle(), b.getAuthor(),
                Integer.toString(b.getPublishedYear()),
                b.getCategory().name(),
                b.getCondition().name(),
                Double.toString(b.getOriginalPrice()),
                Double.toString(b.getSellingPrice()),
                b.getSellerId(),
                Boolean.toString(b.isSold()));
    }

    private BookStore() {}
}

