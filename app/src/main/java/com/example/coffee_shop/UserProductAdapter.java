package com.example.coffee_shop;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class UserProductAdapter extends RecyclerView.Adapter<UserProductAdapter.ProductViewHolder> {
    private Context context;
    private List<Product> productList;


    public UserProductAdapter(Context context,List<Product> productList){
        this.context=context;
        this.productList=productList;
    }


    public static class ProductViewHolder extends RecyclerView.ViewHolder{
        TextView name,price;
        Button addtocartButton;
        public ProductViewHolder(View itemView){
            super(itemView);
            name=itemView.findViewById(R.id.productName);
            price=itemView.findViewById(R.id.productPrice);
            addtocartButton=itemView.findViewById(R.id.addtocartButton);
        }
    }


    @NonNull
    @Override
    public UserProductAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_item, parent, false);
        return new ProductViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull UserProductAdapter.ProductViewHolder holder, int position) {
        Product p = productList.get(position);
        holder.name.setText(p.getName());
        holder.price.setText("Price: $" + p.getPrice());
        holder.addtocartButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, ModifyDeleteActivity.class);
            intent.putExtra("fragment", "delete");
            intent.putExtra("productId", p.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}