package com.example.demo1.BuyerView;

import java.util.ArrayList;

public class FilterEngine {

    public ArrayList<Book> filterByCategory(ArrayList<Book> books, String category) {
        ArrayList<Book> result = new ArrayList<Book>();

        for (int i = 0; i < books.size(); i++) {
            Book book = books.get(i);
            if (book.getCategory().equals(category)) {
                result.add(book);
            }
        }

        return result;
    }

    public ArrayList<Book> filterByPrice(ArrayList<Book> books, double maxPrice) {
        ArrayList<Book> result = new ArrayList<Book>();

        for (int i = 0; i < books.size(); i++) {
            Book book = books.get(i);
            if (book.getSellingPrice() <= maxPrice) {
                result.add(book);
            }
        }

        return result;
    }

    public ArrayList<Book> searchBooks(ArrayList<Book> books, String search) {
        ArrayList<Book> result = new ArrayList<Book>();

        for (int i = 0; i < books.size(); i++) {
            Book book = books.get(i);
            String title = book.getTitle().toLowerCase();
            String author = book.getAuthor().toLowerCase();
            String searchLower = search.toLowerCase();

            if (title.contains(searchLower) || author.contains(searchLower)) {
                result.add(book);
            }
        }

        return result;
    }

    public ArrayList<Book> filterByCondition(ArrayList<Book> books, String condition) {
        ArrayList<Book> result = new ArrayList<Book>();

        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getCondition().equals(condition)) {
                result.add(books.get(i));
            }
        }

        return result;
    }
}
