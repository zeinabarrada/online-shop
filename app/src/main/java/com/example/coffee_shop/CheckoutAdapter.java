package com.example.coffee_shop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class CheckoutAdapter extends RecyclerView.Adapter<CheckoutAdapter.CheckoutViewHolder> {
    private List<CartItem> cartItems;
    private Context context;

    public CheckoutAdapter(List<CartItem> cartItems, Context context) {
        this.cartItems = cartItems;
        this.context = context;
    }

    @NonNull
    @Override
    public CheckoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_checkout_product, parent, false);
        return new CheckoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckoutViewHolder holder, int position) {
        CartItem item = cartItems.get(position);

        holder.productName.setText(item.getProductName());
        holder.productPrice.setText(String.format("$%.2f", item.getPrice()));
        holder.quantity.setText(String.format("Qty: %d", item.getQuantity()));
        holder.itemTotal.setText(String.format("$%.2f", item.getPrice() * item.getQuantity()));

        if (item.getImageUrl() != null && !item.getImageUrl().isEmpty()) {
            Glide.with(context)
                    .load(item.getImageUrl())
                    .into(holder.productImage);
        }
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    static class CheckoutViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productPrice, quantity, itemTotal;

        public CheckoutViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            quantity = itemView.findViewById(R.id.quantity);
            itemTotal = itemView.findViewById(R.id.itemTotal); // ✅ Make sure this ID exists in your XML
        }
    }
}
