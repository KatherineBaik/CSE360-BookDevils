import java.util.ArrayList;
import java.util.List;

public class BookListingManager {
    private List<Book> listings;

    public BookListingManager() {
        listings = new ArrayList<>();
    }

    public void createListing(Book book) {
        if (book == null || book.getTitle().isEmpty() || book.getSellingPrice() <= 0) {
            System.out.println("Invalid book listing. Check all fields.");
            return;
        }
        listings.add(book);
        System.out.println("Book listed: " + book.getTitle());
    }

    public void removeListing(Book book) {
        if (listings.remove(book)) {
            System.out.println("Book removed: " + book.getTitle());
        } else {
            System.out.println("Book not found in listings.");
        }
    }

    public Book findListingByTitle(String title) {
        for (Book b : listings) {
            if (b.getTitle().equalsIgnoreCase(title)) {
                return b;
            }
        }
        return null;
    }

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

    public List<Book> getAllListings() {
        return listings;
    }
}

