package com.apki.e_tests;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;



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
        Button loginButton = findViewById(R.id.loginLink);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText edUsername = findViewById(R.id.inputUsername);
                EditText edPassword = findViewById(R.id.inputPassword);
                String username = edUsername.getText().toString();
                String password = edPassword.getText().toString();
                validateAccount(username, password);
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

    private void validateAccount(String username, final String password){
        final FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(username, password);
        try {
            FirebaseUser user = auth.getCurrentUser();
            user.reload();
            if(user.isEmailVerified()){
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }else{
                startActivity(new Intent(LoginActivity.this, VerifyAccountActivity.class));
            }
        } catch (NullPointerException e) {
            showWrongPasswordAlert();
            Log.d("USER NULL", e.toString());
        }
    }

    private void showWrongPasswordAlert(){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(LoginActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.activity_pop_up_on_wrong_password,null);
        TextView recover = mView.findViewById(R.id.recoveryLink);
        TextView cancel = mView.findViewById(R.id.cancel);
        mBuilder.setView(mView);
        final AlertDialog popup = mBuilder.create();
        popup.show();
        popup.getWindow().setLayout(840, 640);
        recover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, PasswordRecoveryActivity.class));
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
            }
        });
    }

}
