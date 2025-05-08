package com.example.coffee_shop;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    Button btnGoToCart;
    RecyclerView recyclerView;
    UserProductAdapter adapter;
    List<Product> productList = new ArrayList<>();
    DBHelper dbHelper;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        // Initialize views with null checks
        recyclerView = findViewById(R.id.recyclerView);
        if (recyclerView == null) {
            Toast.makeText(this, "RecyclerView not found!", Toast.LENGTH_SHORT).show();
            return;
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnGoToCart = findViewById(R.id.btnGoToCart);
        if (btnGoToCart == null) {
            Toast.makeText(this, "Cart button not found!", Toast.LENGTH_SHORT).show();
        } else {
            btnGoToCart.setOnClickListener(v -> {
                Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                startActivity(intent);
            });
        }

        dbHelper = new DBHelper(this);
        loadProducts();
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

                productList.add(new Product(String.valueOf(id), name, price, image));
            } while (cursor.moveToNext());
            cursor.close();
        }

        adapter = new UserProductAdapter(this, productList, getSupportFragmentManager());
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProducts(); // Refresh product list when returning to this activity
    }
}
