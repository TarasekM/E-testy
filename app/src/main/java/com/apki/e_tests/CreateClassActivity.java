package com.apki.e_tests;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.apki.e_tests.Models.LectureClass;
import com.apki.e_tests.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class CreateClassActivity extends AppCompatActivity {

    private LectureClass aLectureClass = new LectureClass();
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
                aLectureClass.setClassName(edClassName.getText().toString());
                FirebaseUser fbUser = auth.getCurrentUser();
                String email = fbUser.getEmail();

                DocumentReference documentReference = db.collection("USERS").document(email);
                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        User user = new User(task.getResult());
                        aLectureClass.setOwner(user);
                        aLectureClass.saveClassToDB(db);
                        finish();
                    }
                });
            }
        });


    }
}
