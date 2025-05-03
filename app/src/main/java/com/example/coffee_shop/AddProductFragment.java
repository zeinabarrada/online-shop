package com.example.coffee_shop;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddProductFragment extends Fragment {

EditText nameInput, priceInput,imageInput;
Button addButton;
DBHelper dbHelper;

    public AddProductFragment() {
    }

    public static AddProductFragment newInstance(String param1, String param2) {
        AddProductFragment fragment = new AddProductFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_add_product,container,false);
        nameInput=view.findViewById(R.id.nameInput);
        priceInput = view.findViewById(R.id.priceInput);
        imageInput = view.findViewById(R.id.imageInput);
        addButton = view.findViewById(R.id.addButton);
        dbHelper = new DBHelper(getContext());
        addButton.setOnClickListener(v->{
            String name=nameInput.getText().toString();
            String priceStr=priceInput.getText().toString();
            String image=imageInput.getText().toString();
            if(!name.isEmpty() && !priceStr.isEmpty())
            {
                double price=Double.parseDouble(priceStr);
                boolean inserted=dbHelper.addProduct(name,price,image);
                Toast.makeText(getContext(),inserted ? "Product added":"Failed to add product",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getContext(),"Please fill all fields", Toast.LENGTH_SHORT).show();
            }
        });

        return inflater.inflate(R.layout.fragment_add_product, container, false);
    }
}