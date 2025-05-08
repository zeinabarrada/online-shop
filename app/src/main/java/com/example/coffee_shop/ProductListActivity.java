package com.example.coffee_shop;

import android.database.Cursor;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ProductListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ProductAdapter adapter;
    List<Product> productList;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_products);

//        recyclerView = findViewById(R.id.userProductRecyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dbHelper = new DBHelper(this);
        productList = new ArrayList<>();

        adapter = new ProductAdapter(this, productList, null); // no admin controls
        recyclerView.setAdapter(adapter);

        loadProducts();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProducts(); // Refresh on screen resume
    }

    private void loadProducts() {
        productList.clear();
        Cursor cursor = dbHelper.fetchAllProducts();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String id = cursor.getString(0);
                String name = cursor.getString(1);
                double price = cursor.getDouble(2);
                String image = cursor.getString(3);
                productList.add(new Product(id, name, price, image));
            } while (cursor.moveToNext());
            cursor.close();
        }
        adapter.notifyDataSetChanged();
    }
}
