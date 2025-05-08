package com.example.coffee_shop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class DeleteProductFragment extends Fragment {
    EditText idInput;
    Button deleteButton;
    DBHelper dbHelper;

    public DeleteProductFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delete_product, container, false);

        idInput = view.findViewById(R.id.idInput);
        deleteButton = view.findViewById(R.id.deleteButton);
        dbHelper = new DBHelper(getContext());

        Bundle args = getArguments();
        if (args != null) {
            String productId = args.getString("productId");
            if (productId != null) {
                idInput.setText(productId);
            }
        }
        deleteButton.setOnClickListener(v -> {
            String idStr = idInput.getText().toString();
            if (!idStr.isEmpty()) {
                int id = Integer.parseInt(idStr);
                dbHelper.deleteProduct(id);
                Toast.makeText(getContext(), "Product deleted (if existed)", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Please enter product ID", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
