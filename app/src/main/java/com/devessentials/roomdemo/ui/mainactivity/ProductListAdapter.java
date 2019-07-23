package com.devessentials.roomdemo.ui.mainactivity;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.devessentials.roomdemo.Product;
import com.devessentials.roomdemo.R;

import java.util.List;

class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {

    private final int mItemLayout;
    private List<Product> mProducts;

    ProductListAdapter(@LayoutRes int layoutId) {
        mItemLayout = layoutId;
    }

    void setProducts(List<Product> products) {
        mProducts = products;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(mItemLayout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Product product = mProducts.get(position);
        viewHolder.bind(product);
    }

    @Override
    public int getItemCount() {
        return mProducts == null ? 0 : mProducts.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView mTextView;

        ViewHolder(View view) {
            super(view);
            mTextView = view.findViewById(R.id.product_row);
        }

        void bind(Product product) {
            mTextView.setText(product.getName());
        }
    }
}
