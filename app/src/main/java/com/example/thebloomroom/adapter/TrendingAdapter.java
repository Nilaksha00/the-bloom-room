package com.example.thebloomroom.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
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

public class TrendingAdapter extends RecyclerView.Adapter<TrendingAdapter.TrendingViewHolder>{
    Context context;
    List<FlowerItem> trendingList;

    public TrendingAdapter(Context context, List<FlowerItem> trendingList){
        this.context = context;
        this.trendingList = trendingList;
    }

    @NonNull
    @Override
    public TrendingAdapter.TrendingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.trending_item, parent, false);
        return new TrendingViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull TrendingAdapter.TrendingViewHolder holder, int position) {
        holder.name.setText(trendingList.get(position).getName());
        holder.price.setText("LKR " +trendingList.get(position).getPrice() + ".00");
        Picasso.get().load(trendingList.get(position).getImage()).into(holder.image);

    }

    @Override
    public int getItemCount() {
       return trendingList.size();
    }

    public class TrendingViewHolder extends RecyclerView.ViewHolder {
        TextView name, price;
        ImageView image;

        public TrendingViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.bouquet_image);
            name = itemView.findViewById(R.id.flower_item_name);
            price = itemView.findViewById(R.id.price);


        }
    }
}
