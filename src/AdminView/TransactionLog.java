package AdminView;

import Data.Order;
import Data.OrderStore;

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

    /* ------------------------------------------------------------------
     *  Persistence API
     * ------------------------------------------------------------------ */

    /**
     * Loads previously saved orders from {@link OrderStore} into memory.
     * Call once when the application starts <b>before</b> anybody
     * queries the log.
     */
    public static void loadData() {
        ORDER_LIST.clear();
        ORDER_LIST.addAll(OrderStore.getAll());
    }

    /* ------------------------------------------------------------------
     *  Basic collection‑style operations
     * ------------------------------------------------------------------ */

    public static List<Order> getOrderList() { return ORDER_LIST; }
    public static int         size()         { return ORDER_LIST.size(); }

    /** Adds an order and immediately persists the change.
     * NOTE: Does not autosave currently */
    public static synchronized void add(Order order) {
        ORDER_LIST.add(order);
        //saveData();                       // autosave for safety; remove if undesired
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

    /** FOR TESTING PURPOSES */
    public static synchronized void clear(){
        ORDER_LIST.clear();
    }
}
