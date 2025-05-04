package com.example.coffee_shop;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class ModifyDeleteActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_delete);

        String fragmentType = getIntent().getStringExtra("fragment");
        String productId = getIntent().getStringExtra("productId");

        Fragment fragment = null;
        Bundle args = new Bundle();
        args.putString("productId", productId);

        if ("modify".equals(fragmentType)) {
            fragment = new ModifyProductFragment();
        } else if ("delete".equals(fragmentType)) {
            fragment = new DeleteProductFragment();
        }

        if (fragment != null) {
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
        }
    }
}
