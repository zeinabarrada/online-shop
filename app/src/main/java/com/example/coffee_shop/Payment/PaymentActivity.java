package com.example.coffee_shop.Payment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coffee_shop.DBHelper;
import com.example.coffee_shop.R;

public class PaymentActivity extends AppCompatActivity {

    private TextView amountTextView;
    private RadioGroup paymentMethodGroup;
    private Button payButton;

    private DBHelper dbHelper;
    private int currentUserId;
    private double totalAmount = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        amountTextView = findViewById(R.id.paymentAmount);
        paymentMethodGroup = findViewById(R.id.radioGroupMethod);
        //payButton = findViewById(R.id.but);

        dbHelper = new DBHelper(this);

        // Get user ID from intent extras
        currentUserId = getIntent().getIntExtra("USER_ID", -1);
        if (currentUserId == -1) {
            Toast.makeText(this, "User not authenticated!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Load total amount from cart
        totalAmount = getTotalAmountFromCart(currentUserId);
        amountTextView.setText(String.format("Amount: $%.2f", totalAmount));

        payButton = findViewById(R.id.buttonPay);    // or R.id.payButton (make sure it is correct)
        payButton.setOnClickListener(v -> processPayment());

        //payButton.setOnClickListener(v -> processPayment());
    }

    private double getTotalAmountFromCart(int userId) {
        double total = 0.0;
        Cursor cursor = dbHelper.getCartItems(userId);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
                total += price * quantity;
            } while (cursor.moveToNext());
            cursor.close();
        }
        return total;
    }

    private void processPayment() {
        int selectedMethodId = paymentMethodGroup.getCheckedRadioButtonId();
        if (selectedMethodId == -1) {
            Toast.makeText(this, "Please select a payment method", Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton selectedMethodButton = findViewById(selectedMethodId);
        String paymentMethod = selectedMethodButton.getText().toString();

        if (totalAmount <= 0) {
            Toast.makeText(this, "Cart is empty or invalid amount", Toast.LENGTH_SHORT).show();
            return;
        }

        long paymentId = dbHelper.addPayment(totalAmount, paymentMethod);
        if (paymentId != -1) {
            Toast.makeText(this, "Payment successful!", Toast.LENGTH_SHORT).show();

            // Clear cart after successful payment
            dbHelper.clearUserCart(currentUserId);

            // Navigate to home page
            Intent intent = new Intent(PaymentActivity.this, com.example.coffee_shop.HomeActivity.class);
            intent.putExtra("USER_ID", currentUserId);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Payment failed. Try again.", Toast.LENGTH_SHORT).show();
        }
    }
}
