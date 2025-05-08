package com.example.coffee_shop.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.coffee_shop.R;

public class CustomPaymentMethodRadioGroup extends RadioGroup {
    private RadioButton rbCredit, rbCash;

    public CustomPaymentMethodRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.payment_radiobuttons, this, true);
        rbCredit = findViewById(R.id.rbCreditCard);
        rbCash = findViewById(R.id.rbCash);
    }

    public String getSelectedMethod() {
        int id = getCheckedRadioButtonId();
        if (id == R.id.rbCreditCard) return "CREDIT_CARD";
        if (id == R.id.rbCash) return "CASH";
        return null;
    }
}