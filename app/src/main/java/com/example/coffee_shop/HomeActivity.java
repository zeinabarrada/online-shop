package com.example.coffee_shop;

import android.os.Bundle;

import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HomeActivity extends AppCompatActivity {

    DBHelper dbHelper;
    RecyclerView recyclerView;
    UserProductAdapter userproductAdapter;
    int userId = 1; // Assume logged-in user

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        dbHelper = new DBHelper(this);

        List<Product> productList = UserProductAdapter.getAllProducts(dbHelper);
        userproductAdapter = new UserProductAdapter(this, productList, dbHelper, userId);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(userproductAdapter);
    }
}
