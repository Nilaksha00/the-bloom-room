package com.example.thebloomroom;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdminFlowerDetailsActivity extends AppCompatActivity {
    EditText flowerNameEditText, flowerPriceEditText, flowerSizeEditText, flowerDescriptionEditText;
    ImageView flowerImageView;
    private FirebaseFirestore db;
    String category, selectedCategoryID;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_flower_details);

        db = FirebaseFirestore.getInstance();
        fetchCategoriesFromFirestore();

        ImageView backIcon = findViewById(R.id.back_icon);
        Button updateButton = findViewById(R.id.update_button);
        Button deleteButton = findViewById(R.id.delete_button);
        flowerNameEditText = findViewById(R.id.flower_name);
        flowerPriceEditText = findViewById(R.id.price);
        flowerSizeEditText = findViewById(R.id.size);
        flowerDescriptionEditText = findViewById(R.id.description);
        flowerImageView = findViewById(R.id.flower_image);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String flowerName = intent.getStringExtra("name");
        category = intent.getStringExtra("category");
        String price = intent.getStringExtra("price");
        String image = intent.getStringExtra("image");
        String size = intent.getStringExtra("size");
        String description = intent.getStringExtra("description");

        flowerNameEditText.setText(flowerName);
        flowerPriceEditText.setText(price);
        flowerSizeEditText.setText(size);
        flowerDescriptionEditText.setText(description);
        Picasso.get().load(image).into(flowerImageView);

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminFlowerDetailsActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Confirm the delete");
                builder.setMessage(
                        "");

                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteFlower(id);
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

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean hasChanges = !flowerName.equals(flowerNameEditText.getText().toString().trim())
                        || !price.equals(flowerPriceEditText.getText().toString().trim())
                        || !size.equals(flowerSizeEditText.getText().toString().trim())
                        || !description.equals(flowerDescriptionEditText.getText().toString().trim())
                        || !category.equals(selectedCategoryID);

                AlertDialog.Builder builder = new AlertDialog.Builder(AdminFlowerDetailsActivity.this);
                builder.setCancelable(true);

                if(hasChanges){
                    builder.setTitle("Confirm the change");
                    builder.setMessage(
                            "");

                    builder.setPositiveButton("Confirm",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    updateFlower(
                                            id,
                                            flowerNameEditText.getText().toString().trim(),
                                            selectedCategoryID,
                                            flowerPriceEditText.getText().toString().trim(),
                                            flowerSizeEditText.getText().toString().trim(),
                                            flowerDescriptionEditText.getText().toString().trim(),
                                            image
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

    }
   
    private void fetchCategoriesFromFirestore() {
        db.collection("categories")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<String> categoryNames = new ArrayList<>();
                        List<String> categoryIDs = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String categoryName = document.getString("name");
                            String categoryID = document.getId();
                            categoryNames.add(categoryName);
                            categoryIDs.add(categoryID);
                        }
                        setupCategorySpinner(categoryNames, categoryIDs);
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }

    private void setupCategorySpinner(List<String> categoryNames, List<String> categoryIDs) {
        Spinner categorySpinner = findViewById(R.id.category_spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                AdminFlowerDetailsActivity.this.category = categoryIDs.get(position);
                selectedCategoryID = categoryIDs.get(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        int defaultCategoryIndex = categoryIDs.indexOf(this.category);

        if (defaultCategoryIndex != -1) {
            categorySpinner.setSelection(defaultCategoryIndex);
        }
    }

    private void deleteFlower(String id) {
        DocumentReference categoryRef = db.collection("flowers").document(id);

        categoryRef.delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(AdminFlowerDetailsActivity.this, "Flower deleted", Toast.LENGTH_SHORT).show();
                    finish();
                    Intent intent = new Intent(AdminFlowerDetailsActivity.this, AdminHomeActivity.class);
                    startActivity(intent);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AdminFlowerDetailsActivity.this, "Failed to delete flower", Toast.LENGTH_SHORT).show();
                });

    }
    
    private void updateFlower(String id, String updatedName, String selectedCategoryID, String updatedPrice, String updatedSize, String updatedDescription, String image) {
        if(updatedName.isEmpty()){
            flowerNameEditText.setError("Flower Name is Required");
            flowerNameEditText.requestFocus();
            return;
        }

        if(updatedPrice.isEmpty()){
            flowerPriceEditText.setError("Price is Required");
            flowerPriceEditText.requestFocus();
            return;
        }

        if(updatedSize.isEmpty()){
            flowerSizeEditText.setError("Size is Required");
            flowerSizeEditText.requestFocus();
            return;
        }
        
        if(updatedDescription.isEmpty()){
            flowerDescriptionEditText.setError("Description is Required");
            flowerDescriptionEditText.requestFocus();
            return;
        }
        
        DocumentReference categoryRef = db.collection("flowers").document(id);

        categoryRef.update("name", updatedName,
                        "price", updatedPrice,
                        "category", selectedCategoryID,
                        "description", updatedDescription,
                        "size", updatedSize)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(AdminFlowerDetailsActivity.this, "Flower updated", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AdminFlowerDetailsActivity.this, AdminHomeActivity.class);
                    startActivity(intent);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AdminFlowerDetailsActivity.this, "Failed to update flower", Toast.LENGTH_SHORT).show();
                });
    }

}
