package com.example.thebloomroom.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thebloomroom.AdminCategoryDetailsActivity;
import com.example.thebloomroom.R;
import com.example.thebloomroom.model.Category;

import java.util.List;

public class AdminCategoryAdapter extends RecyclerView.Adapter<AdminCategoryAdapter.CategoryViewHolder>{

    Context context;
    List<Category> categoryList;

    public AdminCategoryAdapter(Context context, List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public AdminCategoryAdapter.CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.category, parent, false);
        return new AdminCategoryAdapter.CategoryViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.name.setText(categoryList.get(position).getName());
        holder.price.setText("LKR " +categoryList.get(position).getPriceMin() + ".00 - LKR " + categoryList.get(position).getPriceMax() + ".00" );

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AdminCategoryDetailsActivity.class);
                intent.putExtra("categoryName",categoryList.get(position).getName());
                intent.putExtra("id",categoryList.get(position).getId());
                intent.putExtra("minPrice",categoryList.get(position).getPriceMin());
                intent.putExtra("maxPrice",categoryList.get(position).getPriceMax());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView name, price;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.category_name);
            price = itemView.findViewById(R.id.price);
        }
    }
}
