package com.example.thebloomroom;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.thebloomroom.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText editTextFullName, editTextEmail, editTextPassword;
    private ProgressBar spinner;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private Button signupButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView loginTextView = findViewById(R.id.login_textview);

        editTextFullName = findViewById(R.id.name);
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        signupButton = findViewById(R.id.sign_up_button);
        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        spinner.setVisibility(View.GONE);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        if(firebaseAuth.getCurrentUser() != null) {
            firebaseAuth.signOut();
        }

        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinner.setVisibility(View.VISIBLE);
                registerUser();
//                Toast.makeText(RegisterActivity.this,"Account Created Successfully", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void registerUser () {
        final String FullName = editTextFullName.getText().toString().trim();
        final String Email = editTextEmail.getText().toString().trim();
        final String Password = editTextPassword.getText().toString().trim();

        if(FullName.isEmpty()){
            editTextFullName.setError("Full Name Required");
            editTextFullName.requestFocus();
            return;
        }

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

        firebaseAuth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                final String Role = "member";
                spinner.setVisibility(View.GONE);
                if(task.isSuccessful()){
                    User user = new User(
                            FullName,
                            Email,
                            Role
                    );
                    spinner.setVisibility(View.VISIBLE);
                    updateDatabase(user);
                }
                else {
                    Toast.makeText(RegisterActivity.this, task.getException().getMessage(),Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void updateDatabase(User user){
        if (FirebaseAuth.getInstance().getCurrentUser()!=null) {

            String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

            DocumentReference documentReference = FirebaseFirestore.getInstance().collection("users").document(userID);
            HashMap<String,Object> hashMap=new HashMap<>();
            hashMap.put("fullname",user.getFullName());
            hashMap.put("email",user.getEmail());
            hashMap.put("password",user.getPassword());

            documentReference.set(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    spinner.setVisibility(View.GONE);

                    if (task.isSuccessful()){
                        Toast.makeText(RegisterActivity.this,("Registered Successfully!"), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(RegisterActivity.this,("Register Failed!"), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(RegisterActivity.this,("Failed!"), Toast.LENGTH_SHORT).show();
        }
    }
}
