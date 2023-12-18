package com.example.thebloomroom;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thebloomroom.adapter.OrdersAdapter;
import com.example.thebloomroom.model.Order;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class AdminHomeActivity extends AppCompatActivity {
    RecyclerView orderRecycler;

    OrdersAdapter ordersAdapter;
    List<Order> orderList;
    FirebaseFirestore firebaseFirestore;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        firebaseFirestore = FirebaseFirestore.getInstance();

        orderList = new ArrayList<>();
        ordersAdapter = new OrdersAdapter(this, orderList);
        setOrderRecycler();
        fetchOrderItems();
    }

    private void setOrderRecycler() {
        orderRecycler = findViewById(R.id.orders_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        orderRecycler.setLayoutManager(layoutManager);
        orderRecycler.setAdapter(ordersAdapter);
    }

    private void fetchOrderItems() {
        firebaseFirestore.collection("orders")
                .get()
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {

                            String id = document.getId();
                            String address = document.getString("address");
                            String flowerID = document.getString("flowerID");
                            String flowerName = document.getString("flowerName");
                            String quantity = document.getString("quantity");
                            String total = document.getString("total");
                            String userID = document.getString("userId");
                            String userName = document.getString("userName");
                            String status = document.getString("status");
                            String date = document.getString("date");

                            Order item = new Order(id, address, flowerID, flowerName, quantity, total, userID, userName, status, date);
                            orderList.add(item);
                        }

                        ordersAdapter.notifyDataSetChanged();
                    } else {
                        Exception e = task.getException();
                        if (e != null) {
                            e.printStackTrace();
                        }
                    }
                });
    }

}
