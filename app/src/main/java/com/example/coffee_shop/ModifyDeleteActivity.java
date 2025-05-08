package com.example.coffee_shop;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class ModifyDeleteActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_delete);
        Intent intent = getIntent();

        String fragmentType = getIntent().getStringExtra("fragment");
        String productId = getIntent().getStringExtra("productId");

        Fragment fragment = null;

        if ("modify".equals(fragmentType)) {
            fragment = new ModifyProductFragment();

            Bundle args = new Bundle();
            args.putString("productId", productId);
            args.putString("productName", intent.getStringExtra("productName"));
            args.putString("productPrice", intent.getStringExtra("productPrice"));
            args.putString("productImage", intent.getStringExtra("productImage"));
            fragment.setArguments(args);
        } else if ("delete".equals(fragmentType)) {
            fragment = new DeleteProductFragment();

            Bundle args = new Bundle();
            args.putString("productId", productId);
            fragment.setArguments(args);
        }

        if (fragment != null) {

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
        }
    }
}
