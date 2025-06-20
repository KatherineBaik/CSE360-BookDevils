

public class Book {
    private String title;
    private String author;
    private int publishedYear;
    private String category;
    private String condition;
    private double originalPrice;
    private double sellingPrice;
    private boolean isSold;
    
    public Book(String title, String author, int publishedYear, String category, String condition, double originalPrice) {
        this.title = title;
        this.author = author;
        this.publishedYear = publishedYear;
        this.category = category;
        this.condition = condition;
        this.originalPrice = originalPrice;
        this.sellingPrice = calculateSellingPrice();
        this.isSold = false;
    }

    private double calculateSellingPrice() {
        // Example logic: adjust based on condition
        double multiplier = switch (condition.toLowerCase()) {
            case "used like new" -> 0.8;
            case "moderately used" -> 0.6;
            case "heavily used" -> 0.4;
            default -> 0.5;
        };
        return Math.round(originalPrice * multiplier * 100.0) / 100.0;
    }

    public void markAsSold() {
        this.isSold = true;
    }

    public String getBookSummary() {
        return title + " by " + author + " (" + publishedYear + ") - $" + sellingPrice;
    }

    @Override
    public String toString() {
        return "Book[" + title + ", " + author + ", $" + sellingPrice + ", " + condition + "]";
    }

    // Getters, setters, equals, hashCode... as needed
}
