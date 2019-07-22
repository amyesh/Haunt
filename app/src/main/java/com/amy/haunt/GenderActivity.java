package com.amy.haunt;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.amy.haunt.util.HauntApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GenderActivity extends AppCompatActivity implements View.OnClickListener {

    private Button saveInfoButton;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users");

    private String currentUserId;
    private ProgressBar progressBar;
    ArrayList<String> genders = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gender);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setLogo(R.drawable.haunt_logo_small);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.gender_progress);

        saveInfoButton = findViewById(R.id.gender_button);
        saveInfoButton.setOnClickListener(this);

        if (HauntApi.getInstance() != null) {
            currentUserId = HauntApi.getInstance().getUserId();
            Log.d("userId", "onCreate: " + HauntApi.getInstance().getUserEmail());
        }

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser = firebaseAuth.getCurrentUser();

                if (currentUser != null) {

                } else {

                }
            }
        };
    }

    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        switch(view.getId()) {
            case R.id.checkbox_androgynous:
                if (checked) {
                    genders.add("Androgynous");
                }
                break;
            case R.id.checkbox_man:
                if (checked) {
                    genders.add("Male");
                }
                break;
            case R.id.checkbox_woman:
                if (checked) {
                    genders.add("Female");
                }
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (firebaseAuth != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

    @Override
    public void onClick(View view) {

        if (genders.size() > 0) {
            Map<String, ArrayList> genderMap = new HashMap<>();
            genderMap.put("genders", genders);
            progressBar.setVisibility(View.VISIBLE);
            collectionReference.document(currentUserId)
                    .set(genderMap, SetOptions.merge())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            startActivity(new Intent(GenderActivity.this, OptionalInfoActivity.class));
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(GenderActivity.this,
                    "Please select at least one gender preference.",
                    Toast.LENGTH_SHORT)
                    .show();
        }
    }
    //Todo: add feedback link
}


//    TextView feedback = (TextView) findViewById(R.id.TextViewSendFeedback);
//      feedback.setText(Html.fromHtml("<a href=\"mailto:ask@me.it\">Send Feedback</a>"));
//        feedback.setMovementMethod(LinkMovementMethod.getInstance());
