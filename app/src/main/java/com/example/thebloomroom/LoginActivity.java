package com.example.thebloomroom;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth firebaseAuth;
    EditText editTextEmail, editTextPassword;
    private Button loginButton;
    private ProgressBar spinner;
    SharedPreferences sharedPreferences;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences("UserDetails", MODE_PRIVATE);

        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        loginButton = findViewById(R.id.sign_in_button);
        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        spinner.setVisibility(View.GONE);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView registerTextView = findViewById(R.id.register_textview);

        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinner.setVisibility(View.VISIBLE);
                userLogin();
            }
        });
    }

    private void userLogin() {
        final String Email = editTextEmail.getText().toString().trim();
        String Password = editTextPassword.getText().toString().trim();

        if(Email.isEmpty()){
            editTextEmail.setError("Email Address Required");
            editTextEmail.requestFocus();
            return;
        }

        if(Password.isEmpty()){
            editTextPassword.setError("Password Required");
            editTextPassword.requestFocus();
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                spinner.setVisibility(View.GONE);
                if(task.isSuccessful()){
                    spinner.setVisibility(View.VISIBLE);
                    checkRole();
                    Toast.makeText(LoginActivity.this, "Login Successfully",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Incorrect Credentials", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void checkRole() {
        FirebaseFirestore firebaseFirestore= FirebaseFirestore.getInstance();
        String userID=FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String userRole = document.getString("role");

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("UserID", userID);
                        editor.putString("Email", document.getString("email"));
                        editor.putString("FullName", document.getString("fullname"));
                        editor.apply();

                        if ("admin".equals(userRole)) {
                            Intent memberIntent = new Intent(LoginActivity.this, AdminHomeActivity.class);
//                            memberIntent.putExtra("FullName", document.getString("fullname"));
//                            memberIntent.putExtra("Email", document.getString("email"));
//                            memberIntent.putExtra("UserID", userID);
                            startActivity(memberIntent);
                        } else {
                            Intent adminIntent = new Intent(LoginActivity.this, MemberHomeActivity.class);
                            adminIntent.putExtra("FullName", document.getString("fullname"));
                            startActivity(adminIntent);
                        }
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Error! Try Again",Toast.LENGTH_SHORT).show( );

                }
            }
        });
    }

    @Override
    public void onClick(View view) {

    }
}