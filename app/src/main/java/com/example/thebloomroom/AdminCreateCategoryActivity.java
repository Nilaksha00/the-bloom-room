package com.example.thebloomroom;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AdminCreateCategoryActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    EditText categoryNameEditText, minPriceEditText, maxPriceEditText;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_create_category);

        ImageView backIcon = findViewById(R.id.back_icon);
        Button createButton = findViewById(R.id.create_button);
        categoryNameEditText = findViewById(R.id.category_name);
        minPriceEditText = findViewById(R.id.min_price);
        maxPriceEditText = findViewById(R.id.max_price);

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { onBackPressed(); }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminCreateCategoryActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Confirm to add a new category");
                builder.setMessage(
                        "");

                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                addCategory();
                                Intent i = new Intent(AdminCreateCategoryActivity.this, AdminCategoryListActivity.class);
                                startActivity(i);
                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        });
    }

    private void addCategory() {
        String categoryName = categoryNameEditText.getText().toString().trim();
        String minPrice = minPriceEditText.getText().toString().trim();
        String maxPrice = maxPriceEditText.getText().toString().trim();


        if(categoryName.isEmpty()){
            categoryNameEditText.setError("Category Name is Required");
            categoryNameEditText.requestFocus();
            return;
        }

        if(minPrice.isEmpty()){
            minPriceEditText.setError("Minimum Price is Required");
            minPriceEditText.requestFocus();
            return;
        }

        if(maxPrice.isEmpty()){
            maxPriceEditText.setError("Maximum Price is Required");
            maxPriceEditText.requestFocus();
            return;
        }


        // Create a new category map
        Map<String, Object> categoryData = new HashMap<>();
        categoryData.put("name", categoryName);
        categoryData.put("priceMin", minPrice);
        categoryData.put("priceMax", maxPrice);

        // Add the category to the "categories" collection
        db.collection("categories")
                .add(categoryData)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Category Added Successfully", Toast.LENGTH_SHORT).show();

                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error while adding category", Toast.LENGTH_SHORT).show();
                });

    }


}
