package com.apki.e_tests;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class VerifyAccountActivity extends AppCompatActivity {

    FirebaseUser user;
    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_account);
        configureLoginButton();
        configureSendAgainButton();
    }

    private void configureLoginButton(){
        Button login = findViewById(R.id.verifyButton);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = auth.getCurrentUser();
                user.reload();
                startActivity(new Intent(VerifyAccountActivity.this,LoginActivity.class));
                finish();
            }
        });
    }

    private void configureSendAgainButton(){
        Button sendAgain = findViewById(R.id.sendEmailAgain);
        sendAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = auth.getCurrentUser();
                try {
                    user.sendEmailVerification();
                } catch (NullPointerException e){
                    Log.d("USER NULL", e.toString());
                }
            }
        });
    }
}
