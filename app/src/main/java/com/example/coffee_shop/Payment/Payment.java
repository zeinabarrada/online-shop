package com.example.coffee_shop.Payment;

import android.content.ContentValues;
import android.database.Cursor;

import androidx.annotation.NonNull;

public class Payment {
    private long id;
    private double amount;
    private String method;

    // Constructor for new payments (id assigned by DB)
    public Payment(double amount, String method) {
        this.amount = amount;
        this.method = method;
    }

    // Constructor for loading from DB
    public Payment(long id, double amount, String method) {
        this.id = id;
        this.amount = amount;
        this.method = method;
    }

    // Getters & setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }

    // Convert to ContentValues for DB insert/update
    public ContentValues toContentValues() {
        ContentValues cv = new ContentValues();
        cv.put("amount", amount);
        cv.put("method", method);
        return cv;
    }

    // Create a Payment instance from a DB Cursor
    public static Payment fromCursor(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndexOrThrow("id"));
        double amount = cursor.getDouble(cursor.getColumnIndexOrThrow("amount"));
        String method = cursor.getString(cursor.getColumnIndexOrThrow("method"));
        return new Payment(id, amount, method);
    }

    @NonNull
    @Override
    public String toString() {
        return "Payment{id=" + id + ", amount=" + amount + ", method='" + method + '}';
    }
}