package com.apki.e_tests;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.apki.e_tests.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CreateAccountActivity extends AppCompatActivity {

    private static final String TAG = "user";

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    Button dalej, dalej2;
    EditText mail, password, passwordConfirm;

    ConstraintLayout email_content, log_passw_content;
    User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        // Add back button
        FirebaseApp.initializeApp(this);

        email_content = findViewById(R.id.email_content);
        log_passw_content = findViewById(R.id.nick_password_content);

        email_content.setVisibility(ConstraintLayout.VISIBLE);
        log_passw_content.setVisibility(ConstraintLayout.GONE);

        mail = (EditText)findViewById(R.id.inputEmail);
        dalej = (Button)findViewById(R.id.next);
        dalej.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String validemail = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                        "\\@" +
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                        "(" +
                        "\\." +
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                        ")+";

                String email = mail.getText().toString();
                Matcher matcher = Pattern.compile(validemail).matcher(email);

                if (!matcher.matches()) {
                    Toast.makeText(getApplicationContext(), "Nieprawidłowy adres email!", Toast.LENGTH_LONG).show();
                }
                else {
                    configureNextButton();
                }
            }
        });

        password = (EditText)findViewById(R.id.inputPassword);
        passwordConfirm = (EditText)findViewById(R.id.inputPasswordConfirm);
        dalej2 = (Button)findViewById(R.id.next2);

        dalej2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.length() == 0) {
                    Toast.makeText(CreateAccountActivity.this, "Puste pole!", Toast.LENGTH_LONG).show();
                } else if (passwordConfirm.length() == 0) {
                    Toast.makeText(CreateAccountActivity.this, "Puste pole!", Toast.LENGTH_LONG).show();
                } else {
                    configureSecondNextButton();
                }
            }
        });

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void configureNextButton(){
        Button nextButton = findViewById(R.id.next);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText mEdit = findViewById(R.id.inputEmail);
                String emailData = mEdit.getText().toString();
                if(!emailData.equals("")){
                    isValidate(emailData);
                    user.setEmail(emailData);
                }
            }
        });
    }

    private void isValidate(final String emailData ){

        auth.fetchSignInMethodsForEmail(emailData)
                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        if(!task.getResult().getSignInMethods().isEmpty()){
                            showEmailExistsAlert();
                        }else{
                            Log.d(TAG, "doesn't exists");
                            Toast.makeText(CreateAccountActivity.this,
                                    "Adres email jest dostępny",
                                    Toast.LENGTH_SHORT).show();
                            email_content.setVisibility(ConstraintLayout.GONE);
                            log_passw_content.setVisibility(ConstraintLayout.VISIBLE);
                        }
                    }
                });
    }

    private void showEmailExistsAlert(){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(CreateAccountActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.activity_pop_up_on_email_exist,null);
        TextView login = mView.findViewById(R.id.loginLink);
        TextView cancel = mView.findViewById(R.id.cancel);
        mBuilder.setView(mView);
        final AlertDialog popup = mBuilder.create();
        popup.show();
        popup.getWindow().setLayout(840, 540);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
            }
        });
    }

    private void configureSecondNextButton(){
        Button nextButton = findViewById(R.id.next2);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etPassword = findViewById(R.id.inputPassword);
                EditText etPasswordConfirm = findViewById(R.id.inputPasswordConfirm);
                EditText etName = findViewById(R.id.inputName);
                EditText etSurname = findViewById(R.id.inputSurname);

                String password = etPassword.getText().toString();
                String passwordConfirm = etPasswordConfirm.getText().toString();
                String name = etName.getText().toString();
                String surname = etSurname.getText().toString();

                user.setName(name);
                user.setSurname(surname);

                if(password.equals(passwordConfirm)){
                    createUser(user, password);
                }
            }
        });
    }

    private void createUser(final User user, String password){

        auth.createUserWithEmailAndPassword(user.getEmail(), password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>(){
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "createUserWithEmailAndPassword : success");
                            final FirebaseUser firebaseUser = auth.getCurrentUser();
                            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        db.collection("USERS").document(user.getEmail()).set(user);
                                        Intent intent = new Intent(CreateAccountActivity.this, VerifyAccountActivity.class);
                                        startActivity(intent);
                                        finish();
                                        Log.e(TAG, "successful");
                                        Toast.makeText(CreateAccountActivity.this,
                                                "Verification email sent to " + firebaseUser.getEmail(),
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        Log.e(TAG, "sendEmailVerification", task.getException());
                                        Toast.makeText(CreateAccountActivity.this,
                                                "Failed to send verification email.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else{
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(CreateAccountActivity.this, "Autentykacja niezakończona.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home)
        {
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

}

