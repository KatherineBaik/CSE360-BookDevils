import java.util.ArrayList;
import java.util.List;

/**
 * BookListingManager is responsible for managing all books listed by a seller.
 * This class stores the seller's book data and performs listing-related actions.
 * 
 * Collaborates with:
 * - Book (data class): Each listing is a Book object.
 * - SellerPage (UI logic): Calls methods here to update listings from the Seller UI.
 */
public class BookListingManager {
    private List<Book> listings; // Data: holds all books listed by the seller

    public BookListingManager() {
        listings = new ArrayList<>();
    }

    /**
     * Adds a book to the seller’s listings.
     * Called from SellerPage when the seller submits a listing form.
     * Depends on Book class to hold book information like title, author, and price.
     */
    public void createListing(Book book) {
        if (book == null || book.getTitle().isEmpty() || book.getSellingPrice() <= 0) {
            System.out.println("Invalid book listing. Check all fields.");
            return;
        }
        listings.add(book);
        System.out.println("Book listed: " + book.getTitle());
    }

    /**
     * Removes a book from the seller’s listings.
     * Typically triggered by SellerPage when a seller clicks “Remove”.
     */
    public void removeListing(Book book) {
        if (listings.remove(book)) {
            System.out.println("Book removed: " + book.getTitle());
        } else {
            System.out.println("Book not found in listings.");
        }
    }

    /**
     * Searches listings by book title.
     * Useful in the Seller UI for “Edit” or “Search” features.
     */
    public Book findListingByTitle(String title) {
        for (Book b : listings) {
            if (b.getTitle().equalsIgnoreCase(title)) {
                return b;
            }
        }
        return null;
    }

    /**
     * Edits an existing listing by replacing it with a new Book object.
     * Used when the seller updates listing details like price or condition.
     * Input: old book title (identifier), new Book (edited values)
     */
    public boolean editListing(String oldTitle, Book updatedBook) {
        for (int i = 0; i < listings.size(); i++) {
            if (listings.get(i).getTitle().equalsIgnoreCase(oldTitle)) {
                listings.set(i, updatedBook);
                System.out.println("Listing updated for: " + updatedBook.getTitle());
                return true;
            }
        }
        System.out.println("Book not found: " + oldTitle);
        return false;
    }

    /**
     * Returns all books currently listed by the seller.
     * Used by SellerPage to populate the dashboard inventory table.
     */
    public List<Book> getAllListings() {
        return listings;
    }
}

