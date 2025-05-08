package com.example.coffee_shop;

public class CartItem {
    private int id;
    private int productId;
    private String name;
    private double price;

    public CartItem(int id, int productId, String name, double price) {
        this.id = id;
        this.productId = productId;
        this.name = name;
        this.price = price;
    }

    public int getId() { return id; }
    public int getProductId() { return productId; }
    public String getName() { return name; }
    public double getPrice() { return price; }
}
