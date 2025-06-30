package com.example.demo1.BuyerView;

import java.util.ArrayList;

public class CheckoutHandler {

    public Order processCheckout(Cart cart) {
        ArrayList<Book> books = cart.getBooks();

        if (books.size() == 0) {
            return null;
        }

        String orderId = "ORD" + System.currentTimeMillis();
        Order order = new Order(orderId, cart.getBuyer(), books, cart.getTotalPrice());

        for (int i = 0; i < books.size(); i++) {
            Book book = books.get(i);
            int stock = book.getStockQuantity();
            book.setStockQuantity(stock - 1);
        }

        cart.clearCart();
        return order;
    }

    public boolean checkCard(String cardNumber) {
        if (cardNumber.length() == 16) {
            return true;
        }
        return false;
    }

    public boolean checkCVV(String cvv) {
        if (cvv.length() == 3) {
            return true;
        }
        return false;
    }
}
