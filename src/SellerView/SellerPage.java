package SellerView;

import java.util.List;
import Data.Book;

public class SellerPage {
    private BookListingManager bookListingManager;

    public SellerPage(BookListingManager manager) {
        this.bookListingManager = manager;
    }

    public List<Book> viewListings() {
        List<Book> books = bookListingManager.getAllListings();
        System.out.println("Current Listings:");
        for (Book b : books) {
            System.out.println("- " + b.getTitle() + " by " + b.getAuthor());
        }
        return books;
    }

    public void listBook(Book book) {
        bookListingManager.createListing(book);
    }
}

