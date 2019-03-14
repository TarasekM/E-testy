package com.apki.e_tests;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        addToDB();
        setContentView(R.layout.activity_login);
        configureLoginButton();
        configureRecoveryLink();
        configureNewAccountActivity();

    }

    private void addToDB(){
        FirebaseApp.initializeApp(this);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("first", "Ada");
        user.put("last", "Lovelace");
        user.put("born", 1815);

        // Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    private void configureLoginButton(){
        Button loginButton = findViewById(R.id.loginLink);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    private void configureRecoveryLink(){
        TextView textView = findViewById(R.id.linkRecoverAccount);
        textView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(LoginActivity.this, PasswordRecoveryActivity.class));
        }
    });
    }
    private void configureNewAccountActivity(){
        TextView textView = findViewById(R.id.linkSignUp);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, CreateAccountActivity.class));
            }
        });
    }
}
