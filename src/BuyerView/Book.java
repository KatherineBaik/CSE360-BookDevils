package com.example.demo1.BuyerView;

public class Book {
    private String title;
    private String author;
    private int year;
    private String category;
    private String condition;
    private double originalPrice;
    private double sellingPrice;
    private int stockQuantity;

    public Book(String title, String author, int year, String category, String condition,
        double originalPrice, double sellingPrice, int stockQuantity) {
        this.title = title;
        this.author = author;
        this.year = year;
        this.category = category;
        this.condition = condition;
        this.originalPrice = Math.round(originalPrice * 100) / 100.0;
        this.sellingPrice = Math.round(sellingPrice * 100) / 100.0;
        this.stockQuantity = stockQuantity;
    }

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

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(double originalPrice) {
        this.originalPrice = Math.round(originalPrice * 100) / 100.0;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = Math.round(sellingPrice * 100) / 100.0;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
}