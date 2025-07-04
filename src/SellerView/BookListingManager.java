package SellerView;

import Data.Book;
import Data.BookStore;

import java.io.IOException;
import java.util.*;

/**
 * Singleton that mediates between Seller/Buyer UIs and BookStore.csv
 */
public class BookListingManager {

    /* ------------ singleton boiler-plate ------------ */
    private static final BookListingManager INSTANCE = new BookListingManager();
    public static BookListingManager getInstance() { return INSTANCE; }

    /* ------------ in-memory state ------------ */
    /** key = bookId (UUID), value = Book object */
    private final Map<String, Book> listings = new LinkedHashMap<>();

    /** private ctor loads books from CSV once at program start */
    private BookListingManager() {
        try {
            listings.putAll(BookStore.load());        // Map<String,Book>
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /* =================================================
     *  Public API used by SellerPage, BuyerPage, Checkout
     * ================================================= */

    /** All books that are NOT sold (for Buyer view) */
    public Collection<Book> getAllListings() {
        return listings.values().stream()
                       .filter(b -> !b.isSold())
                       .toList();
    }

    /** Seller adds a new listing */
    public void createListing(Book book) {
        if (book.getId() == null || book.getId().isBlank())
            book.setId(UUID.randomUUID().toString());

        listings.put(book.getId(), book);
        persist();
    }

    /** Called by CheckoutHandler after a successful purchase */
    public void markSold(String bookId) {
        Book b = listings.get(bookId);
        if (b != null && !b.isSold()) {
            b.markAsSold();
            persist();
        }
    }

    /** Seller removes a listing before it’s sold */
    public boolean removeListing(String bookId) {
        Book removed = listings.remove(bookId);
        if (removed != null) {
            persist();
            return true;
        }
        return false;
    }

    /** Find a listing by its title (case-insensitive) */
    public Book findListingByTitle(String title) {
        return listings.values().stream()
                       .filter(b -> b.getTitle().equalsIgnoreCase(title))
                       .findFirst()
                       .orElse(null);
    }

    /** Replace the book with matching id by an updated copy */
    public boolean editListing(String bookId, Book updated) {
        if (listings.containsKey(bookId)) {
            updated.setId(bookId);           // keep the same UUID
            listings.put(bookId, updated);
            persist();
            return true;
        }
        return false;
    }

    /* ------------ helper ------------ */
    private void persist() {
        try {
            BookStore.save(listings.values());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}