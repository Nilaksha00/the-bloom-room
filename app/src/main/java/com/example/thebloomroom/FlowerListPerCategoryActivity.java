package com.example.thebloomroom;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thebloomroom.adapter.FlowerListPerCategoryAdapter;
import com.example.thebloomroom.model.FlowerItem;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class FlowerListPerCategoryActivity extends AppCompatActivity {

    TextView categoryName;
    ImageView backIcon;
    RecyclerView flowerListRecycler;
    FlowerListPerCategoryAdapter flowerListPerCategoryAdapter;
    List<FlowerItem> flowerItemList;
    FirebaseFirestore firebaseFirestore;
    String categoryID;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flower_list_per_category);

        categoryName = findViewById(R.id.category_name);
        backIcon = findViewById(R.id.back_icon);

        Intent intent = getIntent();
        String category = intent.getStringExtra("category");
        categoryID = intent.getStringExtra("id");

        categoryName.setText(category);

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { onBackPressed(); }
        });

        firebaseFirestore = FirebaseFirestore.getInstance();

        flowerItemList = new ArrayList<>();
        flowerListPerCategoryAdapter = new FlowerListPerCategoryAdapter(this, flowerItemList);
        setFlowerListRecycler();
        fetchFlowerItems();
    }

    private void setFlowerListRecycler() {
        flowerListRecycler = findViewById(R.id.flowers_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        flowerListRecycler.setLayoutManager(layoutManager);
        flowerListRecycler.setAdapter(flowerListPerCategoryAdapter);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void fetchFlowerItems() {
        firebaseFirestore.collection("flowers")
                .whereEqualTo("category", categoryID)
                .get()
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {

                            String id = document.getId();
                            String description = document.getString("description");
                            String image = document.getString("image");
                            String category = document.getString("category");
                            String name = document.getString("name");
                            String price = document.getString("price");
                            String size = document.getString("size");

                            FlowerItem item = new FlowerItem(id, description, image, name, price, size);

                            flowerItemList.add(item);
                        }

                        flowerListPerCategoryAdapter.notifyDataSetChanged();
                    } else {
                        Exception e = task.getException();
                        if (e != null) {
                            e.printStackTrace();
                        }
                    }
                });
    }

}
