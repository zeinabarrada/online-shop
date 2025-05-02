package com.example.coffee_shop;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class   DBHelper extends SQLiteOpenHelper {
    private static String databaseName = "productDB";
    SQLiteDatabase productDatabase;

    public DBHelper(Context context) {
        super(context, databaseName, null, 1);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table product(id integer primary key,name text not null, price integer not null, image text)");
        db.execSQL("create table user(id integer primary key autoincrement,username text not null, password text not null)");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists product");
        db.execSQL("drop table if exists user");
        onCreate(db);
    }

    // Products Part
    public boolean addProduct(String name, double price, String image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues row = new ContentValues();
        row.put("name", name);
        row.put("price", price);
        row.put("image", image);
        long result = db.insert("product", null, row);
        db.close();
        return result != -1;
    }

    public Cursor fetchAllProducts() {
        productDatabase = getReadableDatabase();
        String[] rowDetails = {"id", "name", "price", "image" };
        Cursor cursor = productDatabase.query("product", rowDetails, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public void deleteProduct(int productId) {
        productDatabase = getWritableDatabase();
        productDatabase.delete("product", "id='" + productId + "'", null);
        productDatabase.close();
    }

    // User's Part
    public boolean addUser(String username, String password) {
        productDatabase = getWritableDatabase();
        ContentValues row = new ContentValues();
        row.put("username", username);
        row.put("password", password);
        long result = productDatabase.insert("user", null, row);
        productDatabase.close();
        return result != -1;

    }

    public boolean checkUser(String username, String password) {
        productDatabase = getReadableDatabase();
        String[] rowDetails = {"username", "password" };
        Cursor cursor = productDatabase.query("user", rowDetails, "username=? AND password=?", new String[]{username, password}, null, null, null);
        boolean exists=false;
        if (cursor!=null && cursor.moveToFirst())
        {
            exists=true;
            cursor.close();
        }
        return exists;
    }
}
