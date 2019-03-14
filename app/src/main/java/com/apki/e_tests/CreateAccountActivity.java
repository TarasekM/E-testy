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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;


public class CreateAccountActivity extends AppCompatActivity {

    private static final String TAG = "user";

    ConstraintLayout email_content, log_passw_content;
    Map<String, Object> user = new HashMap<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

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
        configureNextButton();
        configureSecondNextButton();
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
                    isValidate(emailData, db);
                    user.put("email", emailData);
                }
            }
        });
    }

    private void isValidate(final String emailData , FirebaseFirestore db){

        db.collection("users").whereEqualTo("email",emailData)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        boolean exists = false;
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                exists = true;
                                showEmailExistsAlert();
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                        if (!exists){
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
                EditText etLogin = findViewById(R.id.inputLogin);
                EditText etPassword = findViewById(R.id.inputPassword);
                EditText etPasswordConfirm = findViewById(R.id.inputPasswordConfirm);

                String login = etLogin.getText().toString();
                String password = etPassword.getText().toString();
                String passwordConfirm = etPasswordConfirm.getText().toString();

                if(password.equals(passwordConfirm)){
                    user.put("username", login);
                    user.put("password",password);
                    saveData(user, db);
                }

                startActivity(new Intent(CreateAccountActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    private void saveData(Map user, FirebaseFirestore db){

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
