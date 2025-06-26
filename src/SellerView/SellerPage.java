package SellerView;

import java.util.List;
import Data.Book;

/**
 * SellerPage acts as the controller for the Seller UI.
 * It connects the user interface (like a dashboard or form) to the
 * listing logic in BookListingManager.
 *
 * Collaborates with:
 * - BookListingManager: handles the underlying data and operations on listings
 * - Book: the data model used to represent each listed item
 */
public class SellerPage {
    private BookListingManager bookListingManager;

    /**
     * Constructor receives an instance of BookListingManager.
     * This allows the SellerPage to perform actions like listing or viewing books.
     */
    public SellerPage(BookListingManager manager) {
        this.bookListingManager = manager;
    }

    /**
     * Displays all books currently listed by the seller.
     * Called when the Seller Dashboard loads, or when refreshing listings.
     *
     * Returns: a List of Book objects that can be shown in a UI table or panel.
     */
    public List<Book> viewListings() {
        List<Book> books = bookListingManager.getAllListings();
        System.out.println("Current Listings:");
        for (Book b : books) {
            System.out.println("- " + b.getTitle() + " by " + b.getAuthor());
        }
        return books;
    }

    /**
     * Adds a new book listing.
     * Typically triggered when a seller fills out a form and clicks “List Book”.
     *
     * Input: a Book object containing details like title, author, price, etc.
     */
    public void listBook(Book book) {
        bookListingManager.createListing(book);
    }
}

