package com.example.thebloomroom;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class OrderFlowerActivity extends AppCompatActivity {
    EditText editTextQuantity, editTextUserName, editTextAddress, editTextFlowerName;
    TextView textViewTotal;
    Button orderButton;
    ImageView backIcon;
    String flowerName, flowerId, flowerPrice;

    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_flower);

        Intent intent = getIntent();
        flowerName = intent.getStringExtra("flowerName");
        flowerId = intent.getStringExtra("flowerId");
        flowerPrice = intent.getStringExtra("flowerPrice");

        editTextFlowerName = findViewById(R.id.flower_name);
        textViewTotal = findViewById(R.id.total_price);
        backIcon = findViewById(R.id.back_icon);
        orderButton = findViewById(R.id.order_button);
        editTextUserName = findViewById(R.id.user_name);
        editTextQuantity = findViewById(R.id.quantity);
        editTextAddress = findViewById(R.id.address);

        editTextFlowerName.setText(flowerName);
        textViewTotal.setText("LKR " + flowerPrice + ".00");

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { onBackPressed(); }
        });

        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderFlower();
            }
        });

        editTextQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                updateTotal();
            }
        });
    }

    private void updateTotal() {
        String quantityString = editTextQuantity.getText().toString().trim();
        if (!quantityString.isEmpty()) {
            int quantity = Integer.parseInt(quantityString);
            int total = Integer.parseInt(flowerPrice) * quantity;
            textViewTotal.setText("LKR " + total + ".00");
        }
        else {
            textViewTotal.setText("LKR 0.00");

        }
    }

    private void orderFlower() {
        final String UserName = editTextUserName.getText().toString().trim();
        final String Address = editTextAddress.getText().toString().trim();
        final String Quantity = editTextQuantity.getText().toString().trim();
        final String TotalPrice = textViewTotal.getText().toString().trim();

        if(UserName.isEmpty()){
            editTextUserName.setError("Person Name Required");
            editTextUserName.requestFocus();
            return;
        }
        if(Address.isEmpty()){
            editTextAddress.setError("Delivery Address Required");
            editTextAddress.requestFocus();
            return;
        }

        if(Quantity.isEmpty()){
            editTextQuantity.setError("Quantity Required");
            editTextQuantity.requestFocus();
            return;
        }

        if(!(Integer.parseInt(Quantity) > 0 && Integer.parseInt(Quantity) < 100)){
            editTextQuantity.setError("Quantity should be between 1 and 100");
            editTextQuantity.requestFocus();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(OrderFlowerActivity.this);
        builder.setCancelable(true);
        builder.setTitle("Confirm the order");
        builder.setMessage(
                "");


        builder.setPositiveButton("Confirm",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addOrderToFirebase(flowerId, flowerName, UserName, Address, Quantity, TotalPrice);
                    }
                });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void addOrderToFirebase(String flowerId,String flowerName, String userName, String address, String quantity, String total) {

        SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", MODE_PRIVATE);
        String userID = sharedPreferences.getString("UserID", "");

        Date currentDate = Calendar.getInstance().getTime();

        // Format the date in "dd/MM/yyyy" format
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedDate = dateFormat.format(currentDate);

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        Map<String, Object> orderData = new HashMap<>();
        orderData.put("flowerID", flowerId);
        orderData.put("flowerName", flowerName);
        orderData.put("userId", userID);
        orderData.put("userName", userName);
        orderData.put("address", address);
        orderData.put("quantity", quantity);
        orderData.put("total", total);
//        pending - 0 / delivered - 1 / rejected - (-1)
        orderData.put("status", "0");
        orderData.put("date", formattedDate);

        firestore.collection("orders").add(orderData)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(OrderFlowerActivity.this,("Order Placed Successfully!"), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(OrderFlowerActivity.this, MemberHomeActivity.class);
                        startActivity(intent);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(OrderFlowerActivity.this, "Failed to add order to Firebase", Toast.LENGTH_SHORT).show();
                });
    }
}
