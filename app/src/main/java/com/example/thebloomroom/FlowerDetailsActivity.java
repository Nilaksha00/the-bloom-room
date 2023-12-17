package com.example.thebloomroom;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class FlowerDetailsActivity extends AppCompatActivity {
    ImageView backIcon, flowerImageView;
    TextView flowerNameView, flowerPriceView, flowerSizeView, flowerDescriptionView;
    Button orderButton;
    String flowerName, flowerId, flowerPrice, flowerImage, flowerSize, flowerDescription;

    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flower_details);

        backIcon = findViewById(R.id.back_icon);
        orderButton = findViewById(R.id.order_button);
        flowerImageView = findViewById(R.id.flower_image);
        flowerNameView = findViewById(R.id.flower_item_name);
        flowerPriceView = findViewById(R.id.price);
        flowerSizeView = findViewById(R.id.size);
        flowerDescriptionView = findViewById(R.id.description);

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { onBackPressed(); }
        });


        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FlowerDetailsActivity.this, OrderFlowerActivity.class);
                intent.putExtra("flowerName",flowerName);
                intent.putExtra("flowerId",flowerId);
//                intent.putExtra("flowerImage",flowerItemList.get(position).getImage());
//                intent.putExtra("flowerSize",flowerItemList.get(position).getSize());
//                intent.putExtra("flowerDescription",flowerItemList.get(position).getDescription());
                intent.putExtra("flowerPrice",flowerPrice);
                startActivity(intent);
            }
        });

        Intent intent = getIntent();
         flowerName = intent.getStringExtra("flowerName");
         flowerId = intent.getStringExtra("flowerId");
         flowerPrice = intent.getStringExtra("flowerPrice");
         flowerImage = intent.getStringExtra("flowerImage");
         flowerSize = intent.getStringExtra("flowerSize");
         flowerDescription = intent.getStringExtra("flowerDescription");

        flowerNameView.setText(flowerName);
        flowerPriceView.setText("LKR " + flowerPrice + ".00");
        flowerSizeView.setText(flowerSize + " inch");
        flowerDescriptionView.setText(flowerDescription);
        Picasso.get().load(flowerImage).into(flowerImageView);

    }
}
