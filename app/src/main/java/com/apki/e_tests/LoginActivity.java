package com.apki.e_tests;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        configureLoginButton();
        configureRecoveryLink();
        configureNewAccountActivity();

    }

    private void configureLoginButton(){
        Button loginButton = findViewById(R.id.login);
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
