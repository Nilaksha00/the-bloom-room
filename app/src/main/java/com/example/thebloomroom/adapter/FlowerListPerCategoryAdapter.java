package com.example.thebloomroom.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thebloomroom.FlowerDetailsActivity;
import com.example.thebloomroom.R;
import com.example.thebloomroom.model.FlowerItem;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FlowerListPerCategoryAdapter extends RecyclerView.Adapter<FlowerListPerCategoryAdapter.FlowerItemViewHolder>{

    Context context;
    List<FlowerItem> flowerItemList;

    public FlowerListPerCategoryAdapter(Context context, List<FlowerItem> flowerItemList) {
        this.context = context;
        this.flowerItemList = flowerItemList;
    }

    @NonNull
    @Override
    public FlowerListPerCategoryAdapter.FlowerItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.flower_item, parent, false);
        return new FlowerListPerCategoryAdapter.FlowerItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlowerListPerCategoryAdapter.FlowerItemViewHolder holder, int position) {
        holder.name.setText(flowerItemList.get(position).getName());
        holder.price.setText("LKR " +flowerItemList.get(position).getPrice() + ".00");
        Picasso.get().load(flowerItemList.get(position).getImage()).into(holder.image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FlowerDetailsActivity.class);
//                intent.putExtra("category",categoryList.get(position).getName());
//                intent.putExtra("id",categoryList.get(position).getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() { return flowerItemList.size(); }

    public class FlowerItemViewHolder extends RecyclerView.ViewHolder {
        TextView name, price;
        ImageView image;
        public FlowerItemViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.bouquet_image);
            name = itemView.findViewById(R.id.flower_item_name);
            price = itemView.findViewById(R.id.price);

        }
    }
}
