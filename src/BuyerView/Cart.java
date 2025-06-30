package com.example.demo1.BuyerView;

import java.util.ArrayList;

public class Cart {
    private ArrayList<Book> books;
    private User buyer;
    private double totalPrice;

    public Cart(User buyer) {
        this.buyer = buyer;
        this.books = new ArrayList<Book>();
        this.totalPrice = 0;
    }

    public boolean addBook(Book book) {
        int count = 0;
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).equals(book)) {
                count++;
            }
        }

        if (count >= book.getStockQuantity()) {
            return false;
        }

        books.add(book);
        totalPrice = totalPrice + book.getSellingPrice();
        return true;
    }

    public void removeBook(Book book) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).equals(book)) {
                totalPrice = totalPrice - book.getSellingPrice();
                books.remove(i);
                break;
            }
        }
    }

    public void clearCart() {
        books.clear();
        totalPrice = 0;
    }

    public ArrayList<Book> getBooks() {
        return books;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public User getBuyer() {
        return buyer;
    }

    public int getItemCount() {
        return books.size();
    }
}
