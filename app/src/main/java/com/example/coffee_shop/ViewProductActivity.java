// ViewProductActivity.java
package com.example.coffee_shop;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;


public class ViewProductActivity extends AppCompatActivity {
    Button btnGoToCart;
    private int currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);

        TextView nameView = findViewById(R.id.viewProductName);
        TextView priceView = findViewById(R.id.viewProductPrice);
        ImageView imageView = findViewById(R.id.viewProductImage);

        String name = getIntent().getStringExtra("productName");
        String price = getIntent().getStringExtra("productPrice");
        String image = getIntent().getStringExtra("productImage");

        nameView.setText(name);
        priceView.setText("Price: $" + price);








        int imageResId = getResources().getIdentifier(image, "drawable", getPackageName());
        if (imageResId != 0) {
            imageView.setImageResource(imageResId);
        }
    }
}
