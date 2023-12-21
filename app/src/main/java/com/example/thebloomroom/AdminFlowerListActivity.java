package com.example.thebloomroom;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thebloomroom.adapter.AdminFlowerAdapter;
import com.example.thebloomroom.model.FlowerItem;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class AdminFlowerListActivity extends AppCompatActivity {
    RecyclerView  flowerRecycler;
    AdminFlowerAdapter flowerAdapter;
    List<FlowerItem> flowerList;
    FirebaseFirestore firebaseFirestore;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_flower_list);

        ImageView backIcon = findViewById(R.id.back_icon);
        Button addFlower= findViewById(R.id.add_flower_button);
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { Intent i = new Intent(AdminFlowerListActivity.this, AdminHomeActivity.class);
                startActivity(i);}
        });

        addFlower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AdminFlowerListActivity.this, AdminCreateFlowerActivity.class);
                startActivity(i);
            }
        });

        firebaseFirestore = FirebaseFirestore.getInstance();

        flowerList = new ArrayList<>();
        flowerAdapter = new AdminFlowerAdapter(this, flowerList);
        setFlowerRecycler();
        fetchFlowers();
    }

    private void setFlowerRecycler() {
        flowerRecycler = findViewById(R.id.flowers_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        flowerRecycler.setLayoutManager(layoutManager);
        flowerRecycler.setAdapter(flowerAdapter);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void fetchFlowers() {
        firebaseFirestore.collection("flowers")
                .get()
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {

                            String id = document.getId();
                            String name = document.getString("name");
                            String image = document.getString("image");
                            String description = document.getString("description");
                            String category = document.getString("category");
                            String size = document.getString("size");
                            String price = document.getString("price");


                            FlowerItem flower = new FlowerItem(id, name, image, description, category, size, price);
                            flowerList.add(flower);
                        }
                        flowerAdapter.notifyDataSetChanged();
                    } else {
                        Exception e = task.getException();
                        if (e != null) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
