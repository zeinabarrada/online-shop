package com.example.coffee_shop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private Context context;
    private List<Product> productList;
    private FragmentManager fragmentManager;
    public ProductAdapter(Context context,List<Product> productList, FragmentManager fragmentManager){
        this.context=context;
        this.productList=productList;
        this.fragmentManager=fragmentManager;
    }
    public static class ProductViewHolder extends RecyclerView.ViewHolder{
        TextView name,price;
        Button deleteButton, modifyButton;
        ImageView productImage;

        public ProductViewHolder(View itemView){
            super(itemView);
            name=itemView.findViewById(R.id.productName);
            price=itemView.findViewById(R.id.productPrice);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            modifyButton = itemView.findViewById(R.id.modifyButton);
            productImage=itemView.findViewById(R.id.productImage);
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
        int imageResId = context.getResources().getIdentifier(
                p.getImage(), "drawable", context.getPackageName());

        if (imageResId != 0) {
            holder.productImage.setImageResource(imageResId);
        }





        holder.deleteButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, ModifyDeleteActivity.class);
            intent.putExtra("fragment", "delete");
            intent.putExtra("productId", p.getId());
            context.startActivity(intent);
        });

        holder.modifyButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, ModifyDeleteActivity.class);
            intent.putExtra("fragment", "modify");
            intent.putExtra("productId", p.getId());
            context.startActivity(intent);
        });



    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
