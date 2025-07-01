package Data;

import java.util.UUID;

import javafx.beans.property.*;



/**
 * Canonical Book model used by Buyer / Seller / Admin views.
 * <p>
 * – JavaFX properties are exposed so TableView works out-of-the-box.<br>
 * – A sellerId identifies the owner of the listing (needed by SellerPage).<br>
 * – All old getters / setters are still present for compatibility.
 */
public class Book {
    

    /* ------------------------------------------------------------------
     *  Fields + JavaFX properties
     * ------------------------------------------------------------------ */
    private final StringProperty  title          = new SimpleStringProperty(this, "title");
    private final StringProperty  author         = new SimpleStringProperty(this, "author");
    private final IntegerProperty publishedYear  = new SimpleIntegerProperty(this, "publishedYear");
    private       Category        category;
    private       Condition       condition;

    private final DoubleProperty  originalPrice  = new SimpleDoubleProperty(this, "originalPrice");
    private final DoubleProperty  sellingPrice   = new SimpleDoubleProperty(this, "sellingPrice");

    private final BooleanProperty sold           = new SimpleBooleanProperty(this, "sold", false);
    private       String          sellerId;                      // ASU ID of lister

    private String id; 

    /* ------------------------------------------------------------------
     *  Constructors
     * ------------------------------------------------------------------ */

    public Book(String title, String author, int year,
                Category category, Condition condition,
                double originalPrice, String sellerId) {
        this.id   = UUID.randomUUID().toString(); 
        this.title.set(title);
        this.author.set(author);
        this.publishedYear.set(year);
        this.category   = category;
        this.condition  = condition;
        this.originalPrice.set(originalPrice);
        this.sellerId   = sellerId;
        this.id = UUID.randomUUID().toString();

        this.sellingPrice.set(calculateSellingPrice());
    }


    // ③  getters / setters
    public String  getId()        { return id; }
    public void    setId(String i){ this.id = i; }   // used by BookStore on load

    /* no further code changes required */

    /* ------------------------------------------------------------------
     *  Business logic
     * ------------------------------------------------------------------ */

    private double calculateSellingPrice() {
        double multiplier = switch (condition) {
            case LIKE_NEW        -> 0.80;
            case MODERATELY_USED -> 0.60;
            case HEAVILY_USED    -> 0.40;
            case NEW             -> 1.00;
        };
        // round to cents
        return Math.round(originalPrice.get() * multiplier * 100.0) / 100.0;
    }

    public void markAsSold()           { sold.set(true);  }
    public boolean isSold()            { return sold.get(); }
    public BooleanProperty soldProperty() { return sold; }

    public String getBookSummary() {
        return "%s by %s (%d) – $%.2f".formatted(
                getTitle(), getAuthor(), getPublishedYear(), getSellingPrice());
    }

    /* ------------------------------------------------------------------
     *  JavaFX property accessors (used by TableView)
     * ------------------------------------------------------------------ */
    public StringProperty   titleProperty()         { return title; }
    public StringProperty   authorProperty()        { return author; }
    public DoubleProperty   priceProperty()         { return sellingPrice; }

    /* ------------------------------------------------------------------
     *  Classic getters / setters
     * ------------------------------------------------------------------ */
    public String  getTitle()                       { return title.get(); }
    public void    setTitle(String t)               { title.set(t); }

    public String  getAuthor()                      { return author.get(); }
    public void    setAuthor(String a)              { author.set(a); }

    public int     getPublishedYear()               { return publishedYear.get(); }
    public void    setPublishedYear(int y)          { publishedYear.set(y); }

    public Category  getCategory()                  { return category; }
    public void      setCategory(Category c)        { category = c; }

    public Condition getCondition()                 { return condition; }
    public void      setCondition(Condition c)      {
        condition = c;
        sellingPrice.set(calculateSellingPrice());
    }

    public double  getOriginalPrice()               { return originalPrice.get(); }
    public void    setOriginalPrice(double p)       { originalPrice.set(p); }

    public double  getSellingPrice()                { return sellingPrice.get(); }
    public void    setSellingPrice(double p)        { sellingPrice.set(p); }

    public String  getSellerId()                    { return sellerId; }
    public void    setSellerId(String id)           { sellerId = id; }

    /* ------------------------------------------------------------------
     *  Equality on meaningful fields (title+author+year+category+condition)
     * ------------------------------------------------------------------ */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Book b)) return false;
        return getTitle().equals(b.getTitle()) &&
               getAuthor().equals(b.getAuthor()) &&
               getPublishedYear() == b.getPublishedYear() &&
               category == b.category &&
               condition == b.condition;
    }

    @Override
    public String toString() {
        return "Book[" + getBookSummary() + ", " + condition + "]";
    }

    /* ------------------------------------------------------------------
     *  Enums
     * ------------------------------------------------------------------ */
    public enum Condition { NEW, LIKE_NEW, MODERATELY_USED, HEAVILY_USED }
    public enum Category  { SCIENCE, COMPUTERS, MATH, ENGLISH, OTHER }
}
