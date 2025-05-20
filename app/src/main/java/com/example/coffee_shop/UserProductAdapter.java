package com.example.coffee_shop;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserProductAdapter extends RecyclerView.Adapter<UserProductAdapter.ProductViewHolder> {
    private Context context;
    private List<Product> productList;
    private FragmentManager fragmentManager;
    private DBHelper dbHelper;
    private int currentUserId;



    public UserProductAdapter(Context context, List<Product> productList, FragmentManager fragmentManager, int userId) {
        this.context = context;
        this.productList = productList;
        this.fragmentManager = fragmentManager;
        this.currentUserId = userId;
    }
    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView name, price;
        Button addToCartButton;
        ImageView image;

        public ProductViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.productName);
            price = itemView.findViewById(R.id.productPrice);
            image=itemView.findViewById(R.id.viewProductImage);

            addToCartButton = itemView.findViewById(R.id.addToCartButton);
        }
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_products, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserProductAdapter.ProductViewHolder holder, int position) {
        Product p = productList.get(position);
        holder.name.setText(p.getName());
        holder.price.setText("Price: $" + p.getPrice());
        int imageResId = context.getResources().getIdentifier(
                p.getImage(), "drawable", context.getPackageName());

        if (imageResId != 0) {
            holder.image.setImageResource(imageResId);
        }

        // Add click listener for Add to Cart button
        holder.itemView.findViewById(R.id.addToCartButton).setOnClickListener(v -> {
            DBHelper dbHelper = new DBHelper(context);
            boolean success = dbHelper.addToCart(currentUserId, Integer.parseInt(p.getId()), 1);
            if(success) {
                Toast.makeText(context, p.getName() + " added to cart", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Failed to add to cart", Toast.LENGTH_SHORT).show();
            }
        });
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ViewProductActivity.class);
            intent.putExtra("productName", p.getName());
            intent.putExtra("productPrice", String.valueOf(p.getPrice()));
            intent.putExtra("productImage", p.getImage());
            context.startActivity(intent);
        });

    }


    @Override
    public int getItemCount() {
        return productList.size();
    }
}
