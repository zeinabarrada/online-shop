package com.example.coffee_shop;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HomeActivity extends AppCompatActivity {

    DBHelper dbHelper;
    RecyclerView recyclerView;
    ProductAdapter productAdapter;
    int userId = 1; // Assume logged-in user

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ho);

        dbHelper = new DBHelper(this);
        recyclerView = findViewById(R.id.recyclerView); // Make sure you have this in your layout

        List<Product> productList = ProductProvider.getAllProducts(dbHelper); // Add this method yourself
        productAdapter = new ProductAdapter(this, productList, dbHelper, userId);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(productAdapter);
    }
}
