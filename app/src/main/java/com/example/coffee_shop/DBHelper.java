package com.example.coffee_shop;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "coffeeShopDB";
    private static final int DATABASE_VERSION = 4;

    private static final String TABLE_PRODUCTS = "products";
    private static final String TABLE_USERS = "users";
    private static final String TABLE_CART = "cart";
    private static final String TABLE_ADDRESSES = "addresses";
    private static final String TABLE_ORDERS = "orders";
    private static final String TABLE_ORDER_ITEMS = "order_items";

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
        String CREATE_ORDERS_TABLE = "CREATE TABLE " + TABLE_ORDERS + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "user_id INTEGER,"
                + "address TEXT,"
                + "order_date INTEGER,"
                + "total_amount REAL)";
        db.execSQL(CREATE_ORDERS_TABLE);

        String CREATE_ORDER_ITEMS_TABLE = "CREATE TABLE " + TABLE_ORDER_ITEMS + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "order_id INTEGER,"
                + "product_name TEXT,"
                + "quantity INTEGER,"
                + "price REAL)";
        db.execSQL(CREATE_ORDER_ITEMS_TABLE);

        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + TABLE_PRODUCTS + " ("
                + "id INTEGER PRIMARY KEY,"
                + "name TEXT NOT NULL,"
                + "price REAL NOT NULL,"
                + "image TEXT)";
        db.execSQL(CREATE_PRODUCTS_TABLE);

        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "username TEXT NOT NULL UNIQUE,"
                + "password TEXT NOT NULL)";
        db.execSQL(CREATE_USERS_TABLE);

        String CREATE_CART_TABLE = "CREATE TABLE " + TABLE_CART + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "user_id INTEGER,"
                + "product_id INTEGER,"
                + "quantity INTEGER DEFAULT 1,"
                + "FOREIGN KEY(user_id) REFERENCES " + TABLE_USERS + "(id) ON DELETE CASCADE,"
                + "FOREIGN KEY(product_id) REFERENCES " + TABLE_PRODUCTS + "(id) ON DELETE CASCADE)";
        db.execSQL(CREATE_CART_TABLE);

        String CREATE_ADDRESSES_TABLE = "CREATE TABLE " + TABLE_ADDRESSES + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "user_id INTEGER,"
                + "address TEXT NOT NULL)";
        db.execSQL(CREATE_ADDRESSES_TABLE);


        String CREATE_PAYMENTS_TABLE = "CREATE TABLE payments (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "amount REAL NOT NULL, " +
                "method TEXT NOT NULL, " +
                "timestamp INTEGER NOT NULL)";
        db.execSQL(CREATE_PAYMENTS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADDRESSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER_ITEMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        db.execSQL("DROP TABLE IF EXISTS payments");

        onCreate(db);
    }

    public long addUserAddress(int userId, String address) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", userId);
        values.put("address", address);
        long id = db.insert(TABLE_ADDRESSES, null, values);
        db.close();
        return id;
    }

    public String getUserAddress(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ADDRESSES, new String[]{"address"}, "user_id = ?", new String[]{String.valueOf(userId)}, null, null, "id DESC", "1");
        if (cursor != null && cursor.moveToFirst()) {
            String address = cursor.getString(0);
            cursor.close();
            return address;
        }
        return null;
    }

    public int getUserId(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{"id"}, "username = ?", new String[]{username}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            cursor.close();
            return id;
        }
        return -1;
    }

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
        return db.query(TABLE_PRODUCTS, new String[]{"id", "name", "price", "image"}, null, null, null, null, null);
    }

    public boolean updateProduct(int id, String name, double price, String image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("price", price);
        values.put("image", image);
        int rowsAffected = db.update(TABLE_PRODUCTS, values, "id = ?", new String[]{String.valueOf(id)});
        db.close();
        return rowsAffected > 0;
    }

    public boolean deleteProduct(int productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_PRODUCTS, "id = ?", new String[]{String.valueOf(productId)});
        db.close();
        return rowsAffected > 0;
    }

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
        Cursor cursor = db.query(TABLE_USERS, new String[]{"id"}, "username = ? AND password = ?", new String[]{username, password}, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public boolean addToCart(int userId, int productId, int quantity) {
        if (cartItemExists(userId, productId)) {
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
        int rowsAffected = db.update(TABLE_CART, values, "user_id = ? AND product_id = ?", new String[]{String.valueOf(userId), String.valueOf(productId)});
        db.close();
        return rowsAffected > 0;
    }

    public Cursor getCartItems(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT cart.id as cart_id, products.*, cart.quantity FROM " + TABLE_CART + " cart INNER JOIN " + TABLE_PRODUCTS + " products ON cart.product_id = products.id WHERE cart.user_id = ?", new String[]{String.valueOf(userId)});
    }

    public boolean cartItemExists(int userId, int productId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CART, new String[]{"id"}, "user_id = ? AND product_id = ?", new String[]{String.valueOf(userId), String.valueOf(productId)}, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public boolean updateCartItemQuantity(int cartId, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("quantity", quantity);
        int rowsAffected = db.update(TABLE_CART, values, "id = ?", new String[]{String.valueOf(cartId)});
        db.close();
        return rowsAffected > 0;
    }

    public boolean removeCartItem(int cartId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_CART, "id = ?", new String[]{String.valueOf(cartId)});
        db.close();
        return rowsAffected > 0;
    }

    public boolean clearUserCart(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_CART, "user_id = ?", new String[]{String.valueOf(userId)});
        db.close();
        return rowsAffected > 0;
    }



    // ---------- Payments ----------
    public long addPayment(double amount, String method) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("amount", amount);
        values.put("method", method);
        values.put("timestamp", System.currentTimeMillis());
        long result = db.insert("payments", null, values);
        db.close();
        return result;
    }


    public boolean createOrder(int userId, String address, List<CartItem> cartItems) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.beginTransaction();
            ContentValues orderValues = new ContentValues();
            orderValues.put("user_id", userId);
            orderValues.put("address", address);
            orderValues.put("order_date", System.currentTimeMillis());
            double total = 0;
            for (CartItem item : cartItems) {
                total += item.getPrice() * item.getQuantity();
            }
            orderValues.put("total_amount", total);
            long orderId = db.insert(TABLE_ORDERS, null, orderValues);
            if (orderId == -1) return false;
            for (CartItem item : cartItems) {
                ContentValues itemValues = new ContentValues();
                itemValues.put("order_id", orderId);
                itemValues.put("product_name", item.getProductName());
                itemValues.put("quantity", item.getQuantity());
                itemValues.put("price", item.getPrice());
                if (db.insert(TABLE_ORDER_ITEMS, null, itemValues) == -1) {
                    db.endTransaction();
                    return false;
                }
            }
            db.setTransactionSuccessful();
            return true;
        } finally {
            db.endTransaction();
            db.close();
        }
    }
}