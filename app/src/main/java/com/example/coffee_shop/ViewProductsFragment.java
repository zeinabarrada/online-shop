package com.example.coffee_shop;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public class ViewProductsFragment extends Fragment {
    RecyclerView recyclerView;
    ProductAdapter adapter;
    DBHelper dbHelper;
    List<Product> productList;

    public ViewProductsFragment() {}



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_products, container, false);
        recyclerView = view.findViewById(R.id.productRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        dbHelper = new DBHelper(getContext());
        productList = new ArrayList<>();

        Cursor cursor = dbHelper.fetchAllProducts();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String id = cursor.getString(0);
                String name = cursor.getString(1);
                double price = cursor.getDouble(2);
                String image = cursor.getString(3);
                productList.add(new Product(id, name, price, image));
            }
            while (cursor.moveToFirst());
            cursor.close();
        }
        adapter = new ProductAdapter(getContext(), productList);
        recyclerView.setAdapter(adapter);
        return view;
    }
}