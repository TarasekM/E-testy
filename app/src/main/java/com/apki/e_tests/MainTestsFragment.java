package com.apki.e_tests;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.apki.e_tests.Models.Test;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainTestsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainTestsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final ArrayList<Test> TESTS = new ArrayList<>();
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private ArrayList<Test> tests = new ArrayList<>();



    public MainTestsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param tests Parameter 1.
     * @return A new instance of fragment MainTestsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainTestsFragment newInstance(ArrayList<Test> tests) {
        MainTestsFragment fragment = new MainTestsFragment();
        fragment.tests = tests;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tests = TESTS;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_main_tests, container, false);
        configureFloatingAddButton(view);
        configurePlaceholder(view);
        return view;
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

    private void configurePlaceholder(View view){
        Button button = view.findViewById(R.id.placeholder);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ClassActivity.class));
            }
        });
    }

}
