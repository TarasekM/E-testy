package com.apki.e_tests;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telecom.Call;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class MainActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setUI();
        // Add back button
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        configureFloatingAddButton();
    }

    private void setUI(){
        db.collection("TEST")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            fillTestPreview(task.getResult());
                        } else {
                            Log.w("doc", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    private void fillTestPreview(QuerySnapshot data){
        LinearLayout testPreviewContainer = findViewById(R.id.testPreviewContainer);
        for (QueryDocumentSnapshot document : data) {
            Log.d("doc", document.getId() + " => " + document.getData());
            View view = getLayoutInflater().inflate(R.layout.test_preview_template, null);
            TextView testTitle = view.findViewById(R.id.testTitleText);
            TextView subject = view.findViewById(R.id.subjectText);
            TextView section = view.findViewById(R.id.sectionText);
            Map<String, Object> map = (Map<String,Object>) document.getData().get("Details");
            testTitle.setText(map.get("examTitle").toString());
            subject.setText(map.get("subject").toString());
            section.setText(map.get("section").toString());

            testPreviewContainer.addView(view, testPreviewContainer.getChildCount());
        }
    }

    private void configureFloatingAddButton(){
        FloatingActionButton fab = findViewById(R.id.addTest);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CreateTestActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
