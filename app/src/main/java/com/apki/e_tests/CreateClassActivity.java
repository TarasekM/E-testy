package com.apki.e_tests;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.apki.e_tests.Models.Class;
import com.apki.e_tests.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class CreateClassActivity extends AppCompatActivity {

    private Class aClass;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_class);
        configureNextButton();
    }

    private void configureNextButton(){
        Button button = findViewById(R.id.next);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText edClassName = findViewById(R.id.inputClassName);
                aClass.setClassName(edClassName.getText().toString());
                FirebaseUser fbUser = auth.getCurrentUser();
                //TODO get user from db and set it to the class

//                String email = fbUser.getEmail();
//                DocumentReference userDoc = db.collection("USERS")
//                        .document(email);
//                User user = new User();
//                user.setEmail(fbUser.getEmail());
//                aClass.setOwner(user);

            }
        });
    }
}
