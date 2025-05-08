package com.example.coffee_shop;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    Button getBtnGoToCart;
    RecyclerView recyclerView;
    UserProductAdapter adapter;
    List<Product> productList = new ArrayList<>();
    DBHelper dbHelper;
    Button btnGoToCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        btnGoToCart = findViewById(R.id.btnGoToCart); // Make sure the ID exists in your layout
        btnGoToCart.setOnClickListener(v -> {
            Toast.makeText(HomeActivity.this, "Navigating to Cart", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(HomeActivity.this, CartActivity.class);
            startActivity(intent);
        });

        dbHelper = new DBHelper(this);
        loadProducts();

        btnGoToCart.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, CartActivity.class);
            startActivity(intent);
        });
    }



    private void loadProducts() {
        Cursor cursor = dbHelper.fetchAllProducts();
        productList.clear();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
                String image = cursor.getString(cursor.getColumnIndexOrThrow("image")); // Can be null

                productList.add(new Product("id", name, price, image));
            } while (cursor.moveToNext());
            cursor.close();
        }

        getBtnGoToCart .setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, CartActivity.class)));

        adapter = new UserProductAdapter(this, productList, getSupportFragmentManager());
        recyclerView.setAdapter(adapter);
    }



    @Override
    protected void onResume() {
        super.onResume();
        loadProducts(); // Refresh when returning from add/modify/delete
    }
}
