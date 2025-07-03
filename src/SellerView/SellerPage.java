package SellerView;

import java.util.List;
import Data.Book;

/**
 * SellerPage acts as the controller for the Seller UI.
 * It connects the user interface (like a dashboard or form) to the
 * listing logic in BookListingManager.
 *
 * Collaborates with:
 * - BookListingManager: handles persistence and static CRUD operations
 * - Book: the data model representing each listed item
 */
public class SellerPage {
    // Fixed markup rate (e.g. 20% above cost)
    private static final double MARKUP_RATE = 1.20;

    /**
     * Displays the seller dashboard: lists all books currently in inventory.
     */
    public void displayDashboard() {
        List<Book> books = BookListingManager.getAllListings();
        System.out.println("=== Seller Dashboard ===");
        System.out.printf("%-30s %-20s %-6s %-12s %-16s %-10s %-10s %-6s%n", 
                          "Title", "Author", "Year", "Category", "Condition", "Cost", "Price", "Sold");
        for (Book b : books) {
            String soldFlag = b.isSold() ? "Yes" : "No";
            System.out.printf("%-30s %-20s %-6d %-12s %-16s %-10.2f %-10.2f %-6s%n",
                              b.getTitle(), b.getAuthor(),
                              b.getPublishedYear(), b.getCategory().name(),
                              b.getCondition().name(), b.getOriginalPrice(),
                              b.getSellingPrice(), soldFlag);
        }
    }

    /**
     * Adds a new book listing.
     */
    public void listBook(Book book) {
        if (!validateBook(book)) {
            System.out.println("[ERROR] Invalid book details. Listing aborted.");
            return;
        }
        // Auto-calculate selling price and mark as not sold
        book.setSellingPrice(calculateSellingPrice(book.getOriginalPrice()));
        book.setSold(false);
        BookListingManager.createListing(book);
        System.out.println("[INFO] Book listed: " + book.getTitle());
    }

    /**
     * Edits an existing listing identified by the oldTitle.
     */
    public void editListing(String oldTitle, Book updatedBook) {
        if (!validateBook(updatedBook)) {
            System.out.println("[ERROR] Invalid book details. Update aborted.");
            return;
        }
        updatedBook.setSellingPrice(calculateSellingPrice(updatedBook.getOriginalPrice()));
        boolean ok = BookListingManager.updateListing(oldTitle, updatedBook);
        System.out.println(ok ? "[INFO] Book updated: " + oldTitle
                              : "[ERROR] Update failed for: " + oldTitle);
    }

    /**
     * Deletes a book listing by title.
     */
    public void deleteListing(String title) {
        boolean ok = BookListingManager.deleteListing(title);
        System.out.println(ok ? "[INFO] Book deleted: " + title
                              : "[ERROR] Deletion failed for: " + title);
    }

    /**
     * Shows seller statistics: total listed, total sold, total orders.
     */
    public void showStats() {
        System.out.println("=== Seller Stats ===");
        System.out.println("Total Listed: " + BookListingManager.getTotalListed());
        System.out.println("Total Sold:   " + BookListingManager.getTotalSold());
        System.out.println("Total Orders: " + BookListingManager.getTotalOrders());
    }

    /**
     * Validates essential Book input fields before listing or updating.
     */
    private boolean validateBook(Book book) {
        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) return false;
        if (book.getAuthor() == null || book.getAuthor().trim().isEmpty()) return false;
        if (book.getPublishedYear() <= 0) return false;
        if (book.getCategory() == null) return false;
        if (book.getCondition() == null) return false;
        if (book.getOriginalPrice() <= 0) return false;
        return true;
    }

    /**
     * Helper to compute selling price using a fixed markup rate.
     */
    private double calculateSellingPrice(double cost) {
        return Math.round(cost * MARKUP_RATE * 100.0) / 100.0;
    }
}
