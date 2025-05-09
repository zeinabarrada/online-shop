package com.example.coffee_shop;

import android.content.Intent;
import android.content.SharedPreferences;
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
    private int currentUserId;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);

        // Get user ID from multiple sources
        currentUserId = getIntent().getIntExtra("USER_ID", -1);
        if(currentUserId == -1) {
            currentUserId = sharedPreferences.getInt("USER_ID", -1);
        }

        if(currentUserId == -1) {
            Toast.makeText(this, "Please login first!", Toast.LENGTH_SHORT).show();
            redirectToLogin();
            return;
        }

        // Initialize views
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnGoToCart = findViewById(R.id.btnGoToCart);
        btnGoToCart.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, CartActivity.class);
            intent.putExtra("USER_ID", currentUserId);
            startActivity(intent);
        });

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
                String image = cursor.getString(cursor.getColumnIndexOrThrow("image"));

                productList.add(new Product(String.valueOf(id), name, price, image));
            } while (cursor.moveToNext());
            cursor.close();
        }

        adapter = new UserProductAdapter(this, productList, getSupportFragmentManager(), currentUserId);
        recyclerView.setAdapter(adapter);
    }

    private void redirectToLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProducts();
    }
}