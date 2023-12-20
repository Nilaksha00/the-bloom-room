package com.example.thebloomroom.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thebloomroom.R;
import com.example.thebloomroom.model.FlowerItem;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdminFlowerAdapter extends RecyclerView.Adapter<AdminFlowerAdapter.FlowerViewHolder>{

    Context context;
    List<FlowerItem> flowerList;

    public AdminFlowerAdapter(Context context, List<FlowerItem> flowerList) {
        this.context = context;
        this.flowerList = flowerList;
    }

    @NonNull
    @Override
    public AdminFlowerAdapter.FlowerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.flower_item, parent, false);
        return new AdminFlowerAdapter.FlowerViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull FlowerViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        Log.e("AS",flowerList.get(position).getImage());
        holder.name.setText(flowerList.get(position).getName());
        holder.price.setText(flowerList.get(position).getPrice());
        Picasso.get().load(flowerList.get(position).getImage()).into(holder.image);

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context, AdminFlowerDetailsActivity.class);
//                intent.putExtra("categoryName",categoryList.get(position).getName());
//                intent.putExtra("id",categoryList.get(position).getId());
//                intent.putExtra("minPrice",categoryList.get(position).getPriceMin());
//                intent.putExtra("maxPrice",categoryList.get(position).getPriceMax());
//                context.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return flowerList.size();
    }

    public class FlowerViewHolder extends RecyclerView.ViewHolder {
        TextView name, price;
        ImageView image;
        public FlowerViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.flower_item_name);
            price = itemView.findViewById(R.id.price);
            image = itemView.findViewById(R.id.bouquet_image);
        }
    }
}
