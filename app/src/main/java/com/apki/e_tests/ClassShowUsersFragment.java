package com.apki.e_tests;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apki.e_tests.Models.User;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ClassShowUsersFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ClassShowUsersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClassShowUsersFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final ArrayList<User> USERS = new ArrayList<>();
    private static final String CLASS_CODE = "";

    // TODO: Rename and change types of parameters
    private ArrayList<User> users;
    private String classCode;

    private OnFragmentInteractionListener mListener;

    public ClassShowUsersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ClassShowUsersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ClassShowUsersFragment newInstance(ArrayList<User> users) {
        ClassShowUsersFragment fragment = new ClassShowUsersFragment();
        fragment.users = users;
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            users = USERS;
            classCode = getArguments().getString(CLASS_CODE);
        }
    }

    @SuppressLint("DefaultLocale")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_show_users, container, false);
        LinearLayout usersContainer = view.findViewById(R.id.usersContainer);
        for (User user : users){
            View userContainer = inflater.inflate(R.layout.template_user_name_preview, null);
            TextView userName = userContainer.findViewById(R.id.userNameTextView);
            int childCount = usersContainer.getChildCount();
            userName.setText(String.format("%d. %s", childCount + 1, user.getFullName()));
            usersContainer.addView(userContainer, childCount);
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
