package com.example.coffee_shop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
     private Context context;
     private List<Product> productList;
     public ProductAdapter(Context context,List<Product> productList){
         this.context=context;
         this.productList=productList;
     }
     public static class ProductViewHolder extends RecyclerView.ViewHolder{
         TextView name,price;
         public ProductViewHolder(View itemView){
             super(itemView);
             name=itemView.findViewById(R.id.productName);
             price=itemView.findViewById(R.id.productPrice);
         }
     }
    @NonNull

    @Override
    public ProductAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ProductViewHolder holder, int position) {
        Product p = productList.get(position);
        holder.name.setText(p.getName());
        holder.price.setText("Price: $" + p.getPrice());
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
