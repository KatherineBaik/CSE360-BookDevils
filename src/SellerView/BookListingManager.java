package SellerView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import Data.Book;

/**
 * BookListingManager is responsible for managing all books listed by a seller.
 * This class uses static methods to handle persistence and CRUD operations.
 */
public class BookListingManager {
    private static final String FILE_PATH = "seller_listings.csv";
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

    public static List<Book> getAllListings() {
        return new ArrayList<>(listings);
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

    public static int getTotalOrders() {
        // each sold book counts as one order
        return getTotalSold();
    }

    /**
     * Loads existing listings from a CSV file.
     * CSV columns: title,author,publishedYear,category,condition,originalPrice,sellingPrice,isSold
     */
    private static void loadFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            listings.clear();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                Book b = new Book(
                    parts[0],
                    parts[1],
                    Integer.parseInt(parts[2]),
                    Book.Category.valueOf(parts[3]),
                    Book.Condition.valueOf(parts[4]),
                    Double.parseDouble(parts[5])
                );
                b.setSellingPrice(Double.parseDouble(parts[6]));
                b.setSold(Boolean.parseBoolean(parts[7]));
                listings.add(b);
            }
        } catch (IOException | IllegalArgumentException e) {
            System.out.println("[ERROR] Failed to load listings: " + e.getMessage());
        }
    }

    /**
     * Persists current listings to a CSV file.
     */
    private static void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Book b : listings) {
                writer.write(String.join(",",
                    b.getTitle(),
                    b.getAuthor(),
                    String.valueOf(b.getPublishedYear()),
                    b.getCategory().name(),
                    b.getCondition().name(),
                    String.valueOf(b.getOriginalPrice()),
                    String.valueOf(b.getSellingPrice()),
                    String.valueOf(b.isSold())
                ));
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("[ERROR] Failed to save listings: " + e.getMessage());
        }
    }
}


