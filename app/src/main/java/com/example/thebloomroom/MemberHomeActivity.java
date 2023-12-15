package com.example.thebloomroom;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MemberHomeActivity extends AppCompatActivity {

    TextView greetName;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_home);

        greetName = findViewById(R.id.greetName);

        Intent intent = getIntent();
        String Name = intent.getStringExtra("FullName");

        String firstName =   Name.split(" ")[0];

        SpannableString spannableString = new SpannableString(firstName);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        greetName.setText(spannableString);
    }
}
