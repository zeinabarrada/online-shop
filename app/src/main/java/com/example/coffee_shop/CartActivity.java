package com.example.coffee_shop;

import android.content.Context;
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

public class CartActivity extends AppCompatActivity implements CartAdapter.UpdateTotalListener {
    private RecyclerView recyclerView;
    private CartAdapter adapter;
    private DBHelper dbHelper;
    private int currentUserId;
    private TextView totalPriceTextView;
    private List<CartItem> cartItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_list);

        currentUserId = getIntent().getIntExtra("USER_ID", -1);
        if(currentUserId == -1) {
            Toast.makeText(this, "User not authenticated!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        recyclerView = findViewById(R.id.cartRecyclerView);
        totalPriceTextView = findViewById(R.id.totalPrice);
        Button checkoutButton = findViewById(R.id.btnCheckout);
        dbHelper = new DBHelper(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CartAdapter(cartItems, this, this);
        recyclerView.setAdapter(adapter);

        loadCartItems();

        checkoutButton.setOnClickListener(v -> proceedToCheckout());
    }

    private void loadCartItems() {
        Cursor cursor = dbHelper.getCartItems(currentUserId);
        cartItems.clear();
        double total = 0.0;

        if(cursor != null && cursor.moveToFirst()) {
            do {
                int cartId = cursor.getInt(cursor.getColumnIndexOrThrow("cart_id"));
                String productName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
                String image = cursor.getString(cursor.getColumnIndexOrThrow("image"));

                cartItems.add(new CartItem(cartId, productName, price, quantity, image));
                total += price * quantity;
            } while(cursor.moveToNext());
            cursor.close();
        }

        adapter.notifyDataSetChanged();
        updateTotalPrice(total);
    }

    private void updateTotalPrice(double total) {
        totalPriceTextView.setText(String.format("Total: $%.2f", total));
    }

    private void proceedToCheckout() {
        if(cartItems.isEmpty()) {
            Toast.makeText(this, "Your cart is empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        dbHelper.clearUserCart(currentUserId);
        Toast.makeText(this, "Checkout successful! Thank you!", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onUpdateTotal() {
        double total = 0.0;
        for(CartItem item : cartItems) {
            total += item.getPrice() * item.getQuantity();
        }
        updateTotalPrice(total);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCartItems();
    }
}