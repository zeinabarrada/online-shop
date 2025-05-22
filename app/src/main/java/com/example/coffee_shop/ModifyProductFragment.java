package com.example.coffee_shop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class ModifyProductFragment extends Fragment {
    EditText idInput, nameInput, priceInput, imageInput;
    Button modifyButton;
    DBHelper dbHelper;

    public ModifyProductFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_modify_product, container, false);

        idInput = view.findViewById(R.id.idInput);
        nameInput = view.findViewById(R.id.nameInput);
        priceInput = view.findViewById(R.id.priceInput);
        imageInput = view.findViewById(R.id.imageInput);
        modifyButton = view.findViewById(R.id.modifyButton);
        dbHelper = new DBHelper(getContext());

        modifyButton.setOnClickListener(v -> {
            String idStr = idInput.getText().toString();
            String name = nameInput.getText().toString();
            String priceStr = priceInput.getText().toString();
            String image = imageInput.getText().toString();

            if (!idStr.isEmpty() && !name.isEmpty() && !priceStr.isEmpty()) {
                int id = Integer.parseInt(idStr);
                double price = Double.parseDouble(priceStr);
                boolean updated = dbHelper.updateProduct(id, name, price, image);
                Toast.makeText(getContext(), updated ? "Product updated" : "Update failed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
