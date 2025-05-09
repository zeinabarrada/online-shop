package com.example.coffee_shop;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "coffeeShopDB";
    private static final int DATABASE_VERSION = 3; // Incremented version

    // Table names
    private static final String TABLE_PRODUCTS = "products";
    private static final String TABLE_USERS = "users";
    private static final String TABLE_CART = "cart";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create products table
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + TABLE_PRODUCTS + "("
                + "id INTEGER PRIMARY KEY,"
                + "name TEXT NOT NULL,"
                + "price REAL NOT NULL,"
                + "image TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE);

        // Create users table
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "username TEXT NOT NULL UNIQUE,"
                + "password TEXT NOT NULL)";
        db.execSQL(CREATE_USERS_TABLE);

        // Create cart table with proper foreign keys
        String CREATE_CART_TABLE = "CREATE TABLE " + TABLE_CART + "("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "user_id INTEGER,"
                + "product_id INTEGER,"
                + "quantity INTEGER DEFAULT 1,"
                + "FOREIGN KEY(user_id) REFERENCES " + TABLE_USERS + "(id) ON DELETE CASCADE,"
                + "FOREIGN KEY(product_id) REFERENCES " + TABLE_PRODUCTS + "(id) ON DELETE CASCADE)";
        db.execSQL(CREATE_CART_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older tables if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);

        // Create tables again
        onCreate(db);
    }



    public int getUserId(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{"id"},
                "username = ?",
                new String[]{username},
                null, null, null);

        if(cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            cursor.close();
            return id;
        }
        return -1;}


    // ========== PRODUCT METHODS ==========
    public boolean addProduct(String name, double price, String image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("price", price);
        values.put("image", image);

        long result = db.insert(TABLE_PRODUCTS, null, values);
        db.close();
        return result != -1;
    }

    public Cursor fetchAllProducts() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_PRODUCTS,
                new String[]{"id", "name", "price", "image"},
                null, null, null, null, null);
    }

    public boolean updateProduct(int id, String name, double price, String image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("price", price);
        values.put("image", image);

        int rowsAffected = db.update(TABLE_PRODUCTS, values,
                "id = ?", new String[]{String.valueOf(id)});
        db.close();
        return rowsAffected > 0;
    }

    public boolean deleteProduct(int productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_PRODUCTS,
                "id = ?", new String[]{String.valueOf(productId)});
        db.close();
        return rowsAffected > 0;
    }

    // ========== USER METHODS ==========
    public boolean addUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);

        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1;
    }

    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{"id"},
                "username = ? AND password = ?",
                new String[]{username, password},
                null, null, null);


        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // ========== CART METHODS ==========
    public boolean addToCart(int userId, int productId, int quantity) {
        if(cartItemExists(userId, productId)) {
            return updateCartQuantity(userId, productId, quantity);
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", userId);
        values.put("product_id", productId);
        values.put("quantity", quantity);

        long result = db.insert(TABLE_CART, null, values);
        db.close();
        return result != -1;
    }

    private boolean updateCartQuantity(int userId, int productId, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("quantity", quantity);

        int rowsAffected = db.update(TABLE_CART, values,
                "user_id = ? AND product_id = ?",
                new String[]{String.valueOf(userId), String.valueOf(productId)});
        db.close();
        return rowsAffected > 0;
    }

    // In your DBHelper class
    // DBHelper.java
    public Cursor getCartItems(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT cart.id as cart_id, products.*, cart.quantity " +
                "FROM " + TABLE_CART + " cart " +
                "INNER JOIN " + TABLE_PRODUCTS + " products ON cart.product_id = products.id " +
                "WHERE cart.user_id = ?", new String[]{String.valueOf(userId)});
    }


    public boolean cartItemExists(int userId, int productId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CART,
                new String[]{"id"},
                "user_id = ? AND product_id = ?",
                new String[]{String.valueOf(userId), String.valueOf(productId)},
                null, null, null);

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }


    public boolean updateCartItemQuantity(int cartId, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("quantity", quantity);

        int rowsAffected = db.update(TABLE_CART, values,
                "id = ?", new String[]{String.valueOf(cartId)});
        db.close();
        return rowsAffected > 0;
    }

    public boolean removeCartItem(int cartId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_CART,
                "id = ?", new String[]{String.valueOf(cartId)});
        db.close();
        return rowsAffected > 0;
    }

    public boolean clearUserCart(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_CART,
                "user_id = ?", new String[]{String.valueOf(userId)});
        db.close();
        return rowsAffected > 0;
    }
}