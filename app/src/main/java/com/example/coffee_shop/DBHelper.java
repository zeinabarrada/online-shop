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

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Product Table
        db.execSQL("CREATE TABLE product (" +
                "id INTEGER PRIMARY KEY," +
                "name TEXT NOT NULL," +
                "price INTEGER NOT NULL," +
                "image TEXT)");

        // User Table
        db.execSQL("CREATE TABLE user (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT NOT NULL," +
                "password TEXT NOT NULL)");

        // Cart Table
        db.execSQL("CREATE TABLE cart (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "product_id INTEGER," +
                "name TEXT," +
                "price REAL)");
        db.execSQL("create table product(id integer primary key,name text not null, price integer not null, image text)");
        db.execSQL("create table user(id integer primary key autoincrement,username text not null, password text not null)");
        db.execSQL("create table cart(id integer primary key autoincrement, user_id integer, product_id integer, " +
                "foreign key(user_id) references user(id), foreign key(product_id) references product(id))");

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
    // Add this method inside your DBHelper class
    public boolean updateProduct(int id, String name, double price, String image) {
        productDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("price", price);
        contentValues.put("image", image);

        int rowsAffected = productDatabase.update("product", contentValues, "id = ?", new String[]{String.valueOf(id)});
        productDatabase.close();
        return rowsAffected > 0;
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
        String[] rowDetails = {"username", "password"};
        Cursor cursor = productDatabase.query("user", rowDetails, "username=? AND password=?",
                new String[]{username, password}, null, null, null);
        boolean exists = false;
        if (cursor != null && cursor.moveToFirst()) {
            exists = true;
            cursor.close();
        }
        return exists;
    }

    // ------------------ Cart Methods ------------------

    public boolean addToCart(int productId, String name, double price) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("product_id", productId);
        values.put("name", name);
        values.put("price", price);
        long result = db.insert("cart", null, values);
        db.close();
        return result != -1;
    }

    public Cursor getCartItems() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM cart", null);
    }

    public void clearCart() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("cart", null, null);
        db.close();
    }

    public void removeCartItem(int cartId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("cart", "id = ?", new String[]{String.valueOf(cartId)});
        db.close();
    }

    // cart part
    public boolean addToCart(int userId, int productId){
        productDatabase = getWritableDatabase();
        ContentValues row = new ContentValues();
        row.put("user_id", userId);
        row.put("product_id", productId);
        long result = productDatabase.insert("cart", null, row);
        productDatabase.close();
        return result != -1;
    }
}
