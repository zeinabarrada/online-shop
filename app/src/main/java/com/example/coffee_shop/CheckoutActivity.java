package com.example.coffee_shop;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffee_shop.Payment.PaymentActivity;

import java.util.ArrayList;
import java.util.List;

public class CheckoutActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CheckoutAdapter adapter;
    private DBHelper dbHelper;
    private int currentUserId;
    private TextView totalPriceTextView;
    private EditText addressEditText;
    private List<CartItem> cartItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        currentUserId = getIntent().getIntExtra("USER_ID", -1);
        if(currentUserId == -1) {
            Toast.makeText(this, "User not authenticated!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        recyclerView = findViewById(R.id.checkoutRecyclerView); // Matches XML
        totalPriceTextView = findViewById(R.id.totalPrice); // Matches XML
        addressEditText = findViewById(R.id.addressEditText); // Matches XML
        Button placeOrderButton = findViewById(R.id.btnPlaceOrder); // Matches XM
        dbHelper = new DBHelper(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CheckoutAdapter(cartItems, this);
        recyclerView.setAdapter(adapter);

        loadCartItems();

        placeOrderButton.setOnClickListener(v -> {
            Intent intent = new Intent(CheckoutActivity.this, PaymentActivity.class);
            intent.putExtra("USER_ID", currentUserId);
            startActivity(intent);
        });
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
        totalPriceTextView.setText(String.format("Total: $%.2f", total));
    }

    private void placeOrder() {
        String address = addressEditText.getText().toString().trim();
        if(address.isEmpty()) {
            Toast.makeText(this, "Please enter delivery address", Toast.LENGTH_SHORT).show();
            return;
        }

        if(cartItems.isEmpty()) {
            Toast.makeText(this, "Your cart is empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save order to database (you'll need to implement this in DBHelper)
        boolean success = dbHelper.createOrder(currentUserId, address, cartItems);

        if(success) {
            dbHelper.clearUserCart(currentUserId);
            Toast.makeText(this, "Order placed successfully!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to place order", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCartItems();
    }
}