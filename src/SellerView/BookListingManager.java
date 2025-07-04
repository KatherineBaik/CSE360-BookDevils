package SellerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Data.Book;
import Data.BookStore;

/**
 * BookListingManager is responsible for managing all books listed by a seller.
 * This class uses static methods to handle persistence and CRUD operations.
 */
public class BookListingManager {
    private static List<Book> listings = new ArrayList<>();

    // Load existing listings on class load
    static {
        loadFromFile();
    }

    public static void createListing(Book book) {
        listings.add(book);
        saveToFile();
    }

    public static boolean updateListing(String oldTitle, Book updatedBook) {
        for (int i = 0; i < listings.size(); i++) {
            Book b = listings.get(i);
            if (b.getTitle().equals(oldTitle)) {
                listings.set(i, updatedBook);
                saveToFile();
                return true;
            }
        }
        System.out.println("[WARN] Book not found for update: " + oldTitle);
        return false;
    }

    public static boolean deleteListing(String title) {
        for (Book b : listings) {
            if (b.getTitle().equals(title)) {
                listings.remove(b);
                saveToFile();
                return true;
            }
        }
        System.out.println("[WARN] Book not found for deletion: " + title);
        return false;
    }

    /** Called by CheckoutHandler after a successful purchase */
    public static void markSold(String bookId) {
        for(Book b : listings){
            if (b != null && !b.isSold()) {
                b.markAsSold();
                saveToFile();
            }
        }
    }

    /** Returns all listings currently in BookListingManager */
    public static List<Book> getAllListings() {
        return new ArrayList<>(listings);
    }

    /** All books that are NOT sold (for Buyer view) */
    public static List<Book> getAllListingsNotSold() {
        return listings.stream()
                .filter(b -> !b.isSold())
                .toList();
    }

    public static int getTotalListed() {
        return listings.size();
    }

    public static int getTotalSold() {
        int soldCount = 0;
        for (Book b : listings) {
            if (b.isSold()) soldCount++;
        }
        return soldCount;
    }

    /**
     * Loads existing listings from a CSV file.
     * CSV columns: title,author,publishedYear,category,condition,originalPrice,sellingPrice,isSold
     */
    private static void loadFromFile() {
        try {
            listings.addAll(BookStore.load().values());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Persists current listings to a CSV file.
     */
    private static void saveToFile() {
        try {
            BookStore.save(listings);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}


