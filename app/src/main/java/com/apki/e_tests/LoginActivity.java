package com.apki.e_tests;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private boolean nolog = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!isConnected(LoginActivity.this)) buildDialog(LoginActivity.this).show();
        else {
            setContentView(R.layout.activity_login);
            configureLoginButton();
            configureRecoveryLink();
            configureNewAccountActivity();
        }

    }

    public boolean isConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting())) return true;
            else return false;
        } else
            return false;
    }

    public AlertDialog.Builder buildDialog(Context c) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("Brak połączenia z Internetem");
        builder.setMessage("Musisz być połączony do sieci mobilnej lub Wi-Fi. Kliknij OK aby wyjść.");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();
            }
        });

        return builder;
    }

    private void configureLoginButton(){
        Button loginButton = findViewById(R.id.loginLink);

        if (nolog){
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }
            });
            return;
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText edUsername = findViewById(R.id.inputUsername);
                EditText edPassword = findViewById(R.id.inputPassword);
                String username = edUsername.getText().toString();
                String password = edPassword.getText().toString();
                if(!username.equals("") && !password.equals("")){
                    validateAccount(username, password);
                }

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
