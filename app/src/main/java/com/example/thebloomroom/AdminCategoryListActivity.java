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

import com.example.thebloomroom.adapter.AdminCategoryAdapter;
import com.example.thebloomroom.model.Category;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class AdminCategoryListActivity extends AppCompatActivity {
    RecyclerView  categoryRecycler;
    AdminCategoryAdapter categoryAdapter;
    List<Category> categoryList;
    FirebaseFirestore firebaseFirestore;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category_list);

        ImageView backIcon = findViewById(R.id.back_icon);
        Button addCategory = findViewById(R.id.add_category_button);
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { onBackPressed(); }
        });

        addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AdminCategoryListActivity.this, AdminCreateCategoryActivity.class);
                startActivity(i);
            }
        });

        firebaseFirestore = FirebaseFirestore.getInstance();

        categoryList = new ArrayList<>();
        categoryAdapter = new AdminCategoryAdapter(this, categoryList);
        setCategoryRecycler();
        fetchCategories();
    }

    private void setCategoryRecycler() {
        categoryRecycler = findViewById(R.id.categories_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        categoryRecycler.setLayoutManager(layoutManager);
        categoryRecycler.setAdapter(categoryAdapter);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void fetchCategories() {
        firebaseFirestore.collection("categories")
                .get()
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {

                            String id = document.getId();
                            String name = document.getString("name");
                            String priceMin = document.getString("priceMin");
                            String priceMax = document.getString("priceMax");

                            Category category = new Category(id, name, priceMin, priceMax);
                            categoryList.add(category);
                        }

                        categoryAdapter.notifyDataSetChanged();
                    } else {
                        Exception e = task.getException();
                        if (e != null) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
