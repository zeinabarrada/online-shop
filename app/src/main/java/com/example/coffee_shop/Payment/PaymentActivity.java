package com.example.coffee_shop.Payment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.coffee_shop.databinding.ActivityPaymentBinding;
import androidx.databinding.DataBindingUtil;

import com.example.coffee_shop.R;
import com.example.coffee_shop.custom.CustomPaymentMethodRadioGroup;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class PaymentActivity extends AppCompatActivity {
    private CustomPaymentMethodRadioGroup radioGroup;
    private PaymentDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        radioGroup = findViewById(R.id.radioGroupMethod);
        Button buttonPay = findViewById(R.id.buttonPay);
        dbHelper = new PaymentDbHelper(this);
        double amount = 500.0;

        ActivityPaymentBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_payment);
        binding.setAmount(amount);

        buttonPay.setOnClickListener(v -> {
            String method = radioGroup.getSelectedMethod();
            if (method == null) {
                Toast.makeText(PaymentActivity.this, "Select method", Toast.LENGTH_SHORT).show();
                return;
            }

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("amount", amount);
            cv.put("method", method);
            long id = db.insert("paymentsDB", null, cv);
            Toast.makeText(PaymentActivity.this, "Payment saved (ID="+id+")", Toast.LENGTH_LONG).show();
            radioGroup.clearCheck();
        });
    }
}
