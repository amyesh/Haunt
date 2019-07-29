package com.amy.haunt;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
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

import java.util.HashMap;
import java.util.Map;

public class OptionalInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private Button saveInfoButton;
    private String kids;
    private String drinking;
    private String smoking;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users");

    private String currentUserId;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_optional_info);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setLogo(R.drawable.haunt_logo_small);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.optional_progress);

        saveInfoButton = findViewById(R.id.opt_button);
        saveInfoButton.setOnClickListener(this);

        if (HauntApi.getInstance() != null) {
            currentUserId = HauntApi.getInstance().getUserId();
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
        if (kids != null) {
            Map<String, Object> optionalMap = new HashMap<>();
            optionalMap.put("kids", kids);
            optionalMap.put("drinking", drinking);
            optionalMap.put("smoking", smoking);
            HauntApi hauntApi = HauntApi.getInstance();
            hauntApi.setKids(kids);
            hauntApi.setDrinking(drinking);
            hauntApi.setSmoking(smoking);
                    progressBar.setVisibility(View.VISIBLE);
            collectionReference.document(currentUserId)
                    .set(optionalMap, SetOptions.merge())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
        startActivity(new Intent(OptionalInfoActivity.this, AddUserPreferencesActivity.class));
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
            Toast.makeText(OptionalInfoActivity.this,
                    "Please select at least one gender preference.",
                    Toast.LENGTH_SHORT)
                    .show();
        }
    }

    public void onRadioKidsButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.kids_yes:
                if (checked)
                    ((RadioButton) view).setChecked(true);
                kids = "Yes";
                break;
            case R.id.kids_no:
                if (checked)
                    ((RadioButton) view).setChecked(true);
                kids = "No";
                break;
            case R.id.kids_prefer_not:
                if (checked)
                    ((RadioButton) view).setChecked(true);
                kids = "N/A";
                break;
        }
    }

    public void onRadioDrinkButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.radio_yes:
                if (checked)
                    ((RadioButton) view).setChecked(true);
                drinking = "Yes";
                break;
            case R.id.radio_no:
                if (checked)
                    ((RadioButton) view).setChecked(true);
                drinking = "No";
                break;
            case R.id.radio_prefer_not:
                if (checked)
                    ((RadioButton) view).setChecked(true);
                drinking = "Sometimes";
                break;
        }
    }

    public void onRadioSmokeButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.smoke_yes:
                if (checked)
                    ((RadioButton) view).setChecked(true);
                smoking = "Yes";
                break;
            case R.id.smoke_no:
                if (checked)
                    ((RadioButton) view).setChecked(true);
                smoking = "No";
                break;
            case R.id.smoke_prefer_not:
                if (checked)
                    ((RadioButton) view).setChecked(true);
                smoking = "Sometimes";
                break;
        }
    }
}

