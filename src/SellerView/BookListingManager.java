package SellerView;

import Data.Book;
import Data.BookStore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * BookListingManager is responsible for managing all books listed by a seller.
 * This class uses static methods to handle persistence and CRUD operations
 * by interacting directly with the BookStore, ensuring data is always live.
 */
public class BookListingManager {

    public static void createListing(Book book) {
        try {
            BookStore.add(book);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static boolean updateListing(String id, Book updatedBook) {
        try {
            Map<String, Book> all = BookStore.load();
            if (all.containsKey(id)) {
                all.put(id, updatedBook);
                BookStore.save(all.values());
                return true;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.println("[WARN] Book not found for update: " + id);
        return false;
    }

    public static boolean deleteListing(String id) {
        try {
            Map<String, Book> all = BookStore.load();
            if (all.remove(id) != null) {
                BookStore.save(all.values());
                return true;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.println("[WARN] Book not found for deletion: " + id);
        return false;
    }

    /** Called by CheckoutHandler after a successful purchase */
    public static void markSold(String id) {
        try {
            BookStore.markSold(id);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /** Returns all listings currently in the BookStore */
    public static List<Book> getAllListings() {
        try {
            return new ArrayList<>(BookStore.load().values());
        } catch (IOException ex) {
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }

    /** All books that are NOT sold (for Buyer view) */
    public static List<Book> getAllListingsNotSold() {
        try {
            return BookStore.load().values().stream()
                    .filter(b -> !b.isSold())
                    .toList();
        } catch (IOException ex) {
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static int getTotalListed() {
        try {
            return BookStore.load().size();
        } catch (IOException ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    public static int getTotalSold() {
        try {
            return (int) BookStore.load().values().stream()
                    .filter(Book::isSold)
                    .count();
        } catch (IOException ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    /** FOR TESTING PURPOSES */
    public static void clear() {
        try {
            BookStore.save(new ArrayList<>());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
