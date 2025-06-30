package Data;

import java.util.List;

public class Order {
    private static int orderCounter = 1000; // auto-increment order ID
    private int orderID;
    private User buyer;
    private List<Book> books;
    private double totalPrice;

    public Order(User buyer, List<Book> books) {
        this.orderID = orderCounter++;
        this.buyer = buyer;
        this.books = books;
        this.totalPrice = calculateTotalPrice();
    }

    private double calculateTotalPrice() {
        double total = 0.0;
        for (Book b : books) {
            total += b.getSellingPrice();
        }
        return total;
    }

    // Getters
    public int getOrderID() {
        return orderID;
    }

    public User getBuyer() {
        return buyer;
    }

    public List<Book> getBooks() {
        return books;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    // Optional: for printing order info
    public void printOrderDetails() {
        System.out.println("Order ID: " + orderID);
        //System.out.println("Buyer: " + buyer.getUsername());
        System.out.println("Books:");
        for (Book b : books) {
            System.out.println("- " + b.getTitle() + " by " + b.getAuthor() + " | $" + b.getSellingPrice());
        }
        System.out.println("Total Price: $" + totalPrice);
    }
}
