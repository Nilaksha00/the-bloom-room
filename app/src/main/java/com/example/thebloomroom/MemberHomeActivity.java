package com.example.thebloomroom;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thebloomroom.adapter.TrendingAdapter;
import com.example.thebloomroom.model.Category;
import com.example.thebloomroom.model.FlowerItem;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MemberHomeActivity extends AppCompatActivity {

    TextView greetName;
    RecyclerView trendingRecycler;
    RecyclerView categoryRecycler;
    TrendingAdapter trendingAdapter;
    CategoryAdapter categoryAdapter;
    List<FlowerItem> trendingList;
    List<Category> categoryList;
    FirebaseFirestore firebaseFirestore;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_home);

        greetName = findViewById(R.id.greetName);

//        Intent intent = getIntent();
//        String Name = intent.getStringExtra("FullName");
//
//        String firstName =   Name.split(" ")[0];
//
//        SpannableString spannableString = new SpannableString(firstName);
//        spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        greetName.setText(spannableString);

        trendingList = new ArrayList<>();
        trendingAdapter = new TrendingAdapter(this, trendingList);

        setTrendingRecycler();

        firebaseFirestore = FirebaseFirestore.getInstance();

        fetchTrendingItems();
    }

    private void setTrendingRecycler() {
        trendingRecycler = findViewById(R.id.trending_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        trendingRecycler.setLayoutManager(layoutManager);
        trendingRecycler.setAdapter(trendingAdapter);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void fetchTrendingItems() {
        firebaseFirestore.collection("flowers")
                .get()
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {

                            String id = document.getId();
                            String description = document.getString("description");
                            String image = document.getString("image");
                            String name = document.getString("name");
                            String price = document.getString("price");
                            String size = document.getString("size");

                            FlowerItem item = new FlowerItem(id, description, image, name, price, size);

                            trendingList.add(item);
                            Log.e("FLOWER ARRAY", item.getImage());
                        }

                        trendingAdapter.notifyDataSetChanged();
                    } else {
                        Exception e = task.getException();
                        if (e != null) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
