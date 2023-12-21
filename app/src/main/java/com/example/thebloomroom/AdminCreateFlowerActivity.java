package com.example.thebloomroom;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nullable;

public class AdminCreateFlowerActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;

    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    String category;
    private Uri imageUri;
    String downloadURL;
    EditText flowerNameEditText, flowerPriceEditText, flowerSizeEditText, flowerDescriptionEditText;
    Button createFlower;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_create_flower);

        db = FirebaseFirestore.getInstance();
        fetchCategoriesFromFirestore();

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        ImageView backIcon = findViewById(R.id.back_icon);
        Button uploadFlower = findViewById(R.id.upload_button);
        createFlower = findViewById(R.id.create_button);
        flowerNameEditText = findViewById(R.id.flower_name);
        flowerPriceEditText = findViewById(R.id.price);
        flowerSizeEditText = findViewById(R.id.size);
        flowerDescriptionEditText = findViewById(R.id.description);
        
        
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { onBackPressed(); }
        });

        uploadFlower.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                createFlower.setEnabled(false);
                createFlower.setBackgroundResource(R.drawable.disabled_button);
                choosePicture();
            }
        });

        createFlower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminCreateFlowerActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Confirm to add a new flower");
                builder.setMessage(
                        "");

                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                addFlower();

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

    
    private void fetchCategoriesFromFirestore() {
        db.collection("categories")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<String> categoryNames = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String categoryName = document.getString("name");
                            categoryNames.add(categoryName);
                        }
                        setupCategorySpinner(categoryNames);
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }

    private void setupCategorySpinner(List<String> categoryNames) {
        Spinner categorySpinner = findViewById(R.id.category_spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedCategory = (String) parentView.getItemAtPosition(position);
                category = selectedCategory;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }

    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode ==1 && resultCode==RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            uploadFlowerImage();
        }
    }

    private void uploadFlowerImage() {
        final String randomKey = UUID.randomUUID().toString();

        StorageReference flowerRef = storageRef.child("images/" + randomKey);

        flowerRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                flowerRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri downloadUri) {
                        downloadURL = downloadUri.toString();
                        Log.d("Download URL", downloadURL);
                        Toast.makeText(AdminCreateFlowerActivity.this, "Image Added Successfully", Toast.LENGTH_SHORT).show();
                        createFlower.setEnabled(true);
                        createFlower.setBackgroundResource(R.drawable.primary_button);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AdminCreateFlowerActivity.this, "Failed to Add the Image", Toast.LENGTH_SHORT).show();
                        createFlower.setEnabled(true);
                        createFlower.setBackgroundResource(R.drawable.primary_button);
                    }
                });
            }
        });
    };

    private void addFlower() {
        String flowerName = flowerNameEditText.getText().toString().trim();
        String flowerPrice = flowerPriceEditText.getText().toString().trim();
        String flowerSize = flowerSizeEditText.getText().toString().trim();
        String flowerDescription = flowerDescriptionEditText.getText().toString().trim();
       
        if(flowerName.isEmpty()){
            flowerNameEditText.setError("Flower Name is Required");
            flowerNameEditText.requestFocus();
            return;
        }
        
        if(flowerPrice.isEmpty()){
            flowerPriceEditText.setError("Flower Price is Required");
            flowerPriceEditText.requestFocus();
            return;
        }

        if(flowerSize.isEmpty()){
            flowerSizeEditText.setError("Flower Size is Required");
            flowerSizeEditText.requestFocus();
            return;
        }
        
        if(flowerDescription.isEmpty()){
            flowerDescriptionEditText.setError("Flower Description is Required");
            flowerDescriptionEditText.requestFocus();
            return;
        }

        if(downloadURL == null){
            Toast.makeText(AdminCreateFlowerActivity.this, "Image Required", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> flowerData = new HashMap<>();
        flowerData.put("name", flowerName);
        flowerData.put("category", category);
        flowerData.put("description", flowerDescription);
        flowerData.put("image", downloadURL);
        flowerData.put("price", flowerPrice);
        flowerData.put("size", flowerSize);

        db.collection("flowers")
                .add(flowerData)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Flower Added Successfully", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(AdminCreateFlowerActivity.this, AdminFlowerListActivity.class);
                    startActivity(i);

                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error while adding flower", Toast.LENGTH_SHORT).show();
                });



    }
}
