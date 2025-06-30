package com.example.demo1.BuyerView;

import java.util.ArrayList;

public class Order {
    private String orderId;
    private User buyer;
    private ArrayList<Book> books;
    private double totalPrice;

    public Order(String orderId, User buyer, ArrayList<Book> books, double totalPrice) {
        this.orderId = orderId;
        this.buyer = buyer;
        this.books = books;
        this.totalPrice = totalPrice;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public User getBuyer() {
        return buyer;
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }

    public ArrayList<Book> getBooks() {
        return books;
    }

    public void setBooks(ArrayList<Book> books) {
        this.books = books;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
