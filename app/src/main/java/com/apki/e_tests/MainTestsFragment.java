package com.apki.e_tests;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apki.e_tests.Models.Test;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainTestsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainTestsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match[

    // TODO: Rename and change types of parameters
    private ArrayList<Test> tests = new ArrayList<>();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public MainTestsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MainTestsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainTestsFragment newInstance() {
        return new MainTestsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_main_tests, container, false);
        configureFloatingAddButton(view);
//        configurePlaceholder(view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        getTestsFromDB();
    }

    private void configureFloatingAddButton(View view){
        FloatingActionButton fab = view.findViewById(R.id.addTest);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CreateTestActivity.class));
            }
        });
    }

//    private void configurePlaceholder(View view){
//        Button button = view.findViewById(R.id.placeholder);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getActivity(), ClassActivity.class));
//            }
//        });
//    }

    private void fillTestPreview(View view, ArrayList<Test> tests){
        LinearLayout testPreviewContainer = (LinearLayout) view;

        for (final Test test : tests) {
            View testPreview = getLayoutInflater().inflate(R.layout.test_preview_template, null);
            TextView testTitle = testPreview.findViewById(R.id.testName);
            TextView subject = testPreview.findViewById(R.id.subjectText);
            TextView section = testPreview.findViewById(R.id.sectionText);

            testTitle.setText(test.getTitle());
            subject.setText(test.getSubject());
            section.setText(test.getSection());
            Button startTest = testPreview.findViewById(R.id.startTest);

            startTest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), PerformTestActivity.class);
                    intent.putExtra("TEST", test);
                    startActivity(intent);
                }
            });

            testPreviewContainer.addView(testPreview, testPreviewContainer.getChildCount());
        }
    }

    private void getTestsFromDB() {
        final ArrayList<Test> outTest = new ArrayList<>();
        db.collection("TEST")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                outTest.add(new Test(document));
                            }
                            tests = outTest;
                            fillTestPreview(getActivity().findViewById(R.id.testPreviewContainer), tests);

                        } else {
                            Log.w("doc", "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}
