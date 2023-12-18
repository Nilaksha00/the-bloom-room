package com.example.thebloomroom;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminOrderDetailsActivity extends AppCompatActivity {

    ImageView backIcon;
    Button acceptButton, rejectButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_order_details);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String flowerName = intent.getStringExtra("flowerName");
        String total = intent.getStringExtra("total");
        String quantity = intent.getStringExtra("quantity");
        String userName = intent.getStringExtra("userName");
        String status = intent.getStringExtra("status");
        String date = intent.getStringExtra("date");

        TextView flowerItemNameTextView = findViewById(R.id.flower_item_name);
        TextView priceTextView = findViewById(R.id.price);
        TextView quantityTextView = findViewById(R.id.quantity);
        TextView userNameTextView = findViewById(R.id.user_name);
        TextView statusTextView = findViewById(R.id.status);
        TextView dateTextView = findViewById(R.id.date);
        backIcon = findViewById(R.id.back_icon);
        acceptButton = findViewById(R.id.accept_button);
        rejectButton = findViewById(R.id.reject_button);

        flowerItemNameTextView.setText(flowerName);
        priceTextView.setText(total);
        quantityTextView.setText(quantity);
        userNameTextView.setText(userName);
        dateTextView.setText(date);
        if(status.equals("1")){
            statusTextView.setText("Delivered");
            acceptButton.setVisibility(View.GONE);
            rejectButton.setVisibility(View.GONE);
        }else if(status.equals("0")){
            statusTextView.setText("Pending");
        }else if(status.equals("-1")){
            statusTextView.setText("Rejected");
            acceptButton.setVisibility(View.GONE);
            rejectButton.setVisibility(View.GONE);
        }

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { onBackPressed(); }
        });

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { updateOrder(id, "1"); }
        });

        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { updateOrder(id, "-1"); }
        });
    }

    private void updateOrder(String id, String i) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference orderRef = db.collection("orders").document(id);

        orderRef.update("status", i)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(AdminOrderDetailsActivity.this, "Order Updated Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AdminOrderDetailsActivity.this, AdminHomeActivity.class);
                    startActivity(intent);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AdminOrderDetailsActivity.this, "Error while updating", Toast.LENGTH_SHORT).show();

                });


    }
}
