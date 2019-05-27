package com.apki.e_tests;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apki.e_tests.Models.LectureClass;
import com.apki.e_tests.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainClassesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainClassesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    private final static String COLLECTION_CLASS_PATH = "CLASS";
    private final static String COLLECTION_USERS_PATH = "USERS";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private User currentUser;

    // TODO: Rename and change types of parameters
    private ArrayList<LectureClass> lectureClasses = new ArrayList<>();

    public MainClassesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MainClassesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainClassesFragment newInstance() {
        return new MainClassesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_main_classes, container, false);
        configureCreateButton(view);
        configureJoinTheClassButton(view);
        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        getClassesFromDB();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode, resultCode, intent);
        getClassesFromDB();
    }

    private void configureCreateButton(View view){
        Button create = view.findViewById(R.id.createClass);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CreateClassActivity.class));
            }
        });
    }

    private void configureJoinTheClassButton(View view){
        Button join = view.findViewById(R.id.joinTheClass);
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
                final View mView = getLayoutInflater().inflate(R.layout.alter_dialog_join_the_class,null);
                Button proceed = mView.findViewById(R.id.proceed);
                mBuilder.setView(mView);
                final AlertDialog popup = mBuilder.create();
                popup.show();
                popup.getWindow().setLayout(1080, 640);
                proceed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText inputClassCode = mView.findViewById(R.id.inputClassCode);
                        String classCode = inputClassCode.getText().toString();

                        db.collection(COLLECTION_CLASS_PATH)
                                .document(classCode)
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                LectureClass aLectureClass = new LectureClass(documentSnapshot);
                                if( aLectureClass.getStudents().contains(currentUser)){
                                    Toast.makeText(getContext(),"Jesteś już uczniem w tej klasie",Toast.LENGTH_SHORT).show();
                                    return;
                                }else{
                                    aLectureClass.addStudent(currentUser);
                                }

                                db.collection(COLLECTION_CLASS_PATH)
                                        .document(aLectureClass.getClassCode())
                                        .set(aLectureClass)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    getClassesFromDB();
                                                    Toast.makeText(getContext(),"Dołączono do kalsy",Toast.LENGTH_SHORT).show();
                                                }else{
                                                    Toast.makeText(getContext(),"Nie udało się dołączyć do klasy",Toast.LENGTH_SHORT).show();
                                                }
                                                popup.dismiss();
                                            }
                                        });
                            }
                        });

                    }
                });
            }
        });
    }

    private void getClassesFromDB() {
        final ArrayList<LectureClass> outLectureClasses = new ArrayList<>();
        db.collection(COLLECTION_CLASS_PATH)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                outLectureClasses.add(new LectureClass(document));
                            }
                            lectureClasses = outLectureClasses;
                            fillClassPreview(getActivity().findViewById(R.id.classPreviewContainer), lectureClasses);

                        } else {
                            Log.w("doc", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    private void fillClassPreview(View view, final ArrayList<LectureClass> lectureClasses){
        final LinearLayout classContainer = (LinearLayout) view;
        classContainer.removeAllViews();
        FirebaseUser fbUser = auth.getCurrentUser();
        String email = fbUser.getEmail();

        DocumentReference documentReference = db.collection(COLLECTION_USERS_PATH).document(email);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    currentUser = new User(documentSnapshot);
                    for (final LectureClass aLectureClass : lectureClasses){
                        if (aLectureClass.contains(currentUser)){
                            View classPreview = getLayoutInflater().inflate(R.layout.template_class_preview, null);
                            TextView className = classPreview.findViewById(R.id.className);
                            TextView classCode = classPreview.findViewById(R.id.classCode);
                            className.setText("Klasa: " + aLectureClass.getClassName());
                            classCode.setText("Kod Dołączenia: " + aLectureClass.getClassCode());
                            Button manage = classPreview.findViewById(R.id.manageClass);
                            manage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getActivity(), ClassActivity.class);
                                    intent.putExtra("CLASS", aLectureClass);
                                    startActivity(new Intent(getActivity(), ClassActivity.class));
                                }
                            });

                            classContainer.addView(classPreview, classContainer.getChildCount());
                        }
                    }
                }
            }

        });
    }
}
