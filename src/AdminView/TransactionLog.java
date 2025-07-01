package AdminView;

import Data.Order;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Centralised storage for every completed checkout.
 * <p>
 * It keeps an <b>in‑memory</b> {@code ArrayList<Order>} while the
 * program runs and serialises the whole list to a file when asked.
 * <p>
 * Design choices:
 * <ul>
 *   <li>Uses Java built‑in object serialisation – keep it simple for now.</li>
 *   <li>Thread‑safe enough for a desktop app by synchronising write access.</li>
 * </ul>
 */
public final class TransactionLog {

    private TransactionLog() { /* no instances */ }

    /* ------------------------------------------------------------------
     *  Static state
     * ------------------------------------------------------------------ */
    private static final List<Order> ORDER_LIST = new ArrayList<>();

    /** Location on disc (relative to working dir) */
    private static final Path FILE = Path.of("data", "transactions.ser");

    /* ------------------------------------------------------------------
     *  Persistence API
     * ------------------------------------------------------------------ */

    /**
     * Loads previously saved orders from {@link #FILE} into memory.
     * Call once when the application starts <b>before</b> anybody
     * queries the log.
     */
    @SuppressWarnings("unchecked")
    public static void loadData() {
        if (!Files.exists(FILE)) return;               // first run: nothing to load
        try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(FILE))) {
            ORDER_LIST.clear();
            ORDER_LIST.addAll((List<Order>) in.readObject());
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();      // optional: log properly
        }
    }

    /**
     * Serialises the in‑memory list back to {@link #FILE}. Invoke this once
     * on clean application shutdown or whenever you commit a batch of new
     * orders.
     */
    public static void saveData() {
        try {
            Files.createDirectories(FILE.getParent());
            try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(FILE))) {
                out.writeObject(ORDER_LIST);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /* ------------------------------------------------------------------
     *  Basic collection‑style operations
     * ------------------------------------------------------------------ */

    public static List<Order> getOrderList() { return ORDER_LIST; }
    public static int         size()         { return ORDER_LIST.size(); }

    /** Adds an order and immediately persists the change. */
    public static synchronized void add(Order order) {
        ORDER_LIST.add(order);
        saveData();                       // autosave for safety; remove if undesired
    }

    /* ------------------------------------------------------------------
     *  Convenience search / sort helpers
     * ------------------------------------------------------------------ */

    /** Returns every order placed by the given buyer ASU id. */
    public static List<Order> byBuyer(String asuId) {
        return ORDER_LIST.stream()
                .filter(o -> o.getBuyer().getAsuId().equals(asuId))
                .collect(Collectors.toList());
    }

    /** Ascending sort by timestamp (mutates internal list). */
    public static void sortByDate() {
        ORDER_LIST.sort(Comparator.comparing(Order::getTimestamp));
    }

    /** Descending sort by total price (mutates internal list). */
    public static void sortByValue() {
        ORDER_LIST.sort(Comparator.comparingDouble(Order::getTotalPrice).reversed());
    }
}
