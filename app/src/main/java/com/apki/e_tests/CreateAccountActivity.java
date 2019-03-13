package com.apki.e_tests;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.HashMap;
import java.util.Map;


public class CreateAccountActivity extends AppCompatActivity {

    ConstraintLayout email_content, log_passw_content;
    Map<String, Object> user = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        // Add back button

        email_content = findViewById(R.id.email_content);
        log_passw_content = findViewById(R.id.nick_password_content);

        email_content.setVisibility(ConstraintLayout.VISIBLE);
        log_passw_content.setVisibility(ConstraintLayout.GONE);
        configureNextButton();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void configureNextButton(){
        Button nextButton = findViewById(R.id.next);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email_content.setVisibility(ConstraintLayout.GONE);
                log_passw_content.setVisibility(ConstraintLayout.VISIBLE);
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
