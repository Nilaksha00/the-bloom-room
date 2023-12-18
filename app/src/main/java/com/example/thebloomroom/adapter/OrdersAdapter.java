package com.example.thebloomroom.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thebloomroom.R;
import com.example.thebloomroom.AdminOrderDetailsActivity;
import com.example.thebloomroom.model.Order;

import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderViewHolder>{

    Context context;
    List<Order> orderList;

    public OrdersAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrdersAdapter.OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_item, parent, false);
        return new OrdersAdapter.OrderViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull OrdersAdapter.OrderViewHolder holder, int position) {
        String[] words = orderList.get(position).getAddress().split("\\s+");

        holder.flowerName.setText(orderList.get(position).getFlowerName());
        holder.city.setText(orderList.get(position).getDate());
        holder.price.setText(orderList.get(position).getTotal());
        if(orderList.get(position).getStatus().equals("0")){
            holder.status.setBackgroundResource(R.drawable.order_status_indicator_pending);
        }else if(orderList.get(position).getStatus().equals("1")){
            holder.status.setBackgroundResource(R.drawable.order_status_indicator_delivered);
        }else if(orderList.get(position).getStatus().equals("-1")){
            holder.status.setBackgroundResource(R.drawable.order_status_indicator_rejected);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AdminOrderDetailsActivity.class);
                intent.putExtra("id",orderList.get(position).getId());
                intent.putExtra("flowerName",orderList.get(position).getFlowerName());
                intent.putExtra("total",orderList.get(position).getTotal());
                intent.putExtra("quantity",orderList.get(position).getQuantity());
                intent.putExtra("userName",orderList.get(position).getUserName());
                intent.putExtra("status",orderList.get(position).getStatus());
                intent.putExtra("date",orderList.get(position).getDate());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() { return orderList.size(); }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView flowerName, city, price;
        View status;
        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            flowerName = itemView.findViewById(R.id.order_name);
            city = itemView.findViewById(R.id.city);
            price = itemView.findViewById(R.id.price);
            status = itemView.findViewById(R.id.order_status);

        }
    }
}
