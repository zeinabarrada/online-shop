package com.example.coffee_shop;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class UserProductAdapter extends RecyclerView.Adapter<UserProductAdapter.ProductViewHolder> {
    private Context context;
    private List<Product> productList;
    private DBHelper dbHelper;
    private int userId;

    public UserProductAdapter(Context context, List<Product> productList, DBHelper dbHelper, int userId) {
        this.context = context;
        this.productList = productList;
        this.dbHelper = dbHelper;
        this.userId = userId;
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView name, price;
        ImageView image;
        Button addToCartButton;

        public ProductViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.productName);
            price = itemView.findViewById(R.id.productPrice);
            image = itemView.findViewById(R.id.productImage);
            addToCartButton = itemView.findViewById(R.id.addtocartButton);
        }
    }

    @NonNull
    @Override
    public UserProductAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_item_user, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserProductAdapter.ProductViewHolder holder, int position) {
        Product p = productList.get(position);
        int id = Integer.parseInt(p.getId());
        holder.name.setText(p.getName());
        holder.price.setText("Price: $" + p.getPrice());

        int imageResId = context.getResources().getIdentifier(p.getImage(), "drawable", context.getPackageName());
        if (imageResId != 0) {
            holder.image.setImageResource(imageResId);
        }

        holder.addToCartButton.setOnClickListener(v -> {
            boolean added = dbHelper.addToCart(userId, id);
            if (added) {
                Toast.makeText(context, "Added to cart!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Failed or already in cart", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static List<Product> getAllProducts(DBHelper db) {
        List<Product> productList = new ArrayList<>();
        Cursor cursor = db.fetchAllProducts();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String id = cursor.getString(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
                String image = cursor.getString(cursor.getColumnIndexOrThrow("image"));


                Product product = new Product(id, name, price, image);
                productList.add(product);
            } while (cursor.moveToNext());

            cursor.close();
        }

        return productList;
    }
}
