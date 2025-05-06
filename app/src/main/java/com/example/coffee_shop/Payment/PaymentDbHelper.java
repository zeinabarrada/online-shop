package com.example.coffee_shop.Payment;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;

public class PaymentDbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "paymentsDB";
    private static final int DB_VERSION = 1;

    public PaymentDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(@NonNull SQLiteDatabase db) {
        String sql= "CREATE TABLE payments (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "amount REAL NOT NULL, " +
                    "method TEXT NOT NULL, " +
                    "timestamp INTEGER NOT NULL" +
                    ");";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(@NonNull SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS payments");
        onCreate(db);
    }
}
