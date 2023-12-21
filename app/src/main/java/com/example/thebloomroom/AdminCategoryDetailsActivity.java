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

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminCategoryDetailsActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    EditText categoryNameEditText, minPriceEditText, maxPriceEditText;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category_details);

        Intent intent = getIntent();
        String categoryName = intent.getStringExtra("categoryName");
        String id = intent.getStringExtra("id");
        String minPrice = intent.getStringExtra("minPrice");
        String maxPrice = intent.getStringExtra("maxPrice");

        ImageView backIcon = findViewById(R.id.back_icon);
        Button updateButton = findViewById(R.id.update_button);
        Button deleteButton = findViewById(R.id.delete_button);
        categoryNameEditText = findViewById(R.id.category_name);
        minPriceEditText = findViewById(R.id.min_price);
        maxPriceEditText = findViewById(R.id.max_price);

        categoryNameEditText.setText(categoryName);
        minPriceEditText.setText(minPrice);
        maxPriceEditText.setText(maxPrice);

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { onBackPressed(); }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean hasChanges = !categoryName.equals(categoryNameEditText.getText().toString().trim())
                        || !minPrice.equals(minPriceEditText.getText().toString().trim())
                        || !maxPrice.equals(maxPriceEditText.getText().toString().trim());

                AlertDialog.Builder builder = new AlertDialog.Builder(AdminCategoryDetailsActivity.this);
                builder.setCancelable(true);

                if(hasChanges){
                    builder.setTitle("Confirm the change");
                    builder.setMessage(
                            "");

                    builder.setPositiveButton("Confirm",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    updateCategory(
                                            id,
                                            categoryNameEditText.getText().toString().trim(),
                                            minPriceEditText.getText().toString().trim(),
                                            maxPriceEditText.getText().toString().trim()
                                    );
                                }
                            });
                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                }else{
                    builder.setTitle("Category details has not changed");
                    builder.setMessage(
                            "");

                    builder.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                }
                builder.show();
            }
        });
        
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminCategoryDetailsActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Confirm the delete");
                builder.setMessage(
                        "");

                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteCategory(id);
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

    private void updateCategory(String id, String updatedCategoryName, String updatedMinPrice, String updatedMaxPrice) {

        if(updatedCategoryName.isEmpty()){
            categoryNameEditText.setError("Category Name is Required");
            categoryNameEditText.requestFocus();
            return;
        }

        if(updatedMinPrice.isEmpty()){
            minPriceEditText.setError("Minimum Price is Required");
            minPriceEditText.requestFocus();
            return;
        }

        if(updatedMaxPrice.isEmpty()){
            maxPriceEditText.setError("Maximum Price is Required");
            maxPriceEditText.requestFocus();
            return;
        }
            DocumentReference categoryRef = db.collection("categories").document(id);

            categoryRef.update("name", updatedCategoryName,
                            "priceMin", updatedMinPrice,
                            "priceMax", updatedMaxPrice)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(AdminCategoryDetailsActivity.this, "Category updated", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AdminCategoryDetailsActivity.this, AdminHomeActivity.class);
                        startActivity(intent);
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(AdminCategoryDetailsActivity.this, "Failed to update category", Toast.LENGTH_SHORT).show();
                    });

    }

    private void deleteCategory(String id) {
        DocumentReference categoryRef = db.collection("categories").document(id);

        categoryRef.delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(AdminCategoryDetailsActivity.this, "Category deleted", Toast.LENGTH_SHORT).show();
                    finish();
                    Intent intent = new Intent(AdminCategoryDetailsActivity.this, AdminHomeActivity.class);
                    startActivity(intent);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AdminCategoryDetailsActivity.this, "Failed to delete category", Toast.LENGTH_SHORT).show();
                });

    }


}
