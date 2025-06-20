package Data;

public class Book {
    private String title;
    private String author;
    private int publishedYear;
    private Category category;
    private Condition condition;
    private double originalPrice;
    private double sellingPrice;
    private boolean isSold;
    
    public Book(String title, String author, int publishedYear, Category category, Condition condition, double originalPrice) {
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
        double multiplier = switch (condition) {
            case LIKE_NEW -> 0.8;
            case MODERATELY_USED -> 0.6;
            case HEAVILY_USED -> 0.4;
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
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getPublishedYear() {
        return publishedYear;
    }

    public void setPublishedYear(int publishedYear) {
        this.publishedYear = publishedYear;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public boolean isSold() {
        return isSold;
    }

    public void setSold(boolean sold) {
        this.isSold = sold;
    }

    //Enums
    public enum Condition{
        LIKE_NEW, MODERATELY_USED, HEAVILY_USED
    }

    public enum Category{
        SCIENCE, COMPUTERS, MATH, ENGLISH, OTHER
    }
}
