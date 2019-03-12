package com.apki.e_tests;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class CreateAccountActivity extends AppCompatActivity {

    ConstraintLayout email, nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        email = findViewById(R.id.email_content);
        nickname = findViewById(R.id.nickname_content);

        email.setVisibility(ConstraintLayout.VISIBLE);
        nickname.setVisibility(ConstraintLayout.GONE);
        configureNextButton();
    }

    private void configureNextButton(){
        Button nextButton = findViewById(R.id.next);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email.setVisibility(ConstraintLayout.GONE);
                nickname.setVisibility(ConstraintLayout.VISIBLE);
            }
        });
    }

}
