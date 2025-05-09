package com.example.coffee_shop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<CartItem> cartItems;
    private Context context;
    private UpdateTotalListener updateTotalListener;

    public interface UpdateTotalListener {
        void onUpdateTotal();
    }

    public CartAdapter(List<CartItem> cartItems, Context context, UpdateTotalListener listener) {
        this.cartItems = cartItems;
        this.context = context;
        this.updateTotalListener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItems.get(position);

        holder.productName.setText(item.getProductName());
        holder.productPrice.setText(String.format("$%.2f", item.getPrice()));
        holder.quantity.setText(String.valueOf(item.getQuantity()));

        if(item.getImageUrl() != null && !item.getImageUrl().isEmpty()) {
            Glide.with(context)
                    .load(item.getImageUrl())
                    .into(holder.productImage);
        }

        holder.btnIncrease.setOnClickListener(v -> {
            int newQuantity = item.getQuantity() + 1;
            updateQuantity(item.getCartId(), newQuantity, position);
        });

        holder.btnDecrease.setOnClickListener(v -> {
            if(item.getQuantity() > 1) {
                int newQuantity = item.getQuantity() - 1;
                updateQuantity(item.getCartId(), newQuantity, position);
            }
        });

        holder.removeButton.setOnClickListener(v -> {
            DBHelper dbHelper = new DBHelper(context);
            if(dbHelper.removeCartItem(item.getCartId())) {
                cartItems.remove(position);
                notifyItemRemoved(position);
                updateTotalListener.onUpdateTotal();
            } else {
                Toast.makeText(context, "Failed to remove item", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateQuantity(int cartId, int newQuantity, int position) {
        DBHelper dbHelper = new DBHelper(context);
        if(dbHelper.updateCartItemQuantity(cartId, newQuantity)) {
            cartItems.get(position).setQuantity(newQuantity);
            notifyItemChanged(position);
            updateTotalListener.onUpdateTotal();
        } else {
            Toast.makeText(context, "Failed to update quantity", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productPrice, quantity;
        Button btnIncrease, btnDecrease, removeButton;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            quantity = itemView.findViewById(R.id.quantity);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
            removeButton = itemView.findViewById(R.id.removeButton);
        }
    }
}