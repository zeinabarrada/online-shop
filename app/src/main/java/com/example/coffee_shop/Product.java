package com.example.coffee_shop;

public class Product {
    private String id;
    private String name;
    private double price;
    private String image;
    public Product(String id, String name, double price, String image) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
    }


    public String getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getImage() { return image; }
}


