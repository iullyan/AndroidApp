package com.store.controller;

public class Product {
    public int id;
    public String productName;
    public int price;
    public String description;
    public String priceCurrency;
    public Product(int id, String productName, int price, String description, String currency) {
        this.id = id;
        this.price = price;
        this.description = description;
        this.productName = productName;
        this.priceCurrency = currency;
    }
}
