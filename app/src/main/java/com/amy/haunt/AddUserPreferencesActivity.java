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
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class AddUserPreferencesActivity extends AppCompatActivity implements View.OnClickListener{

    private Button saveInfoButton;
    private String preference;
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
        setContentView(R.layout.activity_add_user_preferences);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setLogo(R.drawable.haunt_logo_small);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.preferences_progress);

        saveInfoButton = findViewById(R.id.prefs_button);
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

    public void onRadioPrefButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.men:
                if (checked)
                    ((RadioButton) view).setChecked(true);
                preference = "Male";
                    break;
            case R.id.women:
                if (checked)
                    ((RadioButton) view).setChecked(true);
                preference = "Female";
                break;
            case R.id.everyone:
                if (checked)
                    ((RadioButton) view).setChecked(true);
                preference = "Everyone";
                break;
        }
    }

    @Override
    public void onClick(View view) {
        if (preference != null) {
            Map<String, Object> preferenceMap = new HashMap<>();
            preferenceMap.put("preference", preference);
            HauntApi hauntApi = HauntApi.getInstance();
            hauntApi.setPreference(preference);
            progressBar.setVisibility(View.VISIBLE);
            collectionReference.document(currentUserId)
                    .set(preferenceMap, SetOptions.merge())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(AddUserPreferencesActivity.this,
                    "Please select at least one gender preference.",
                    Toast.LENGTH_SHORT)
                    .show();
        }
        collectionReference
                .whereEqualTo("userId", currentUserId)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {

                        }
                        assert queryDocumentSnapshots != null;
                        if (!queryDocumentSnapshots.isEmpty()) {
                            progressBar.setVisibility(View.INVISIBLE);
                            for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                                HauntApi hauntApi = HauntApi.getInstance();
                                hauntApi.setUserEmail(snapshot.getString("userEmail"));
                                hauntApi.setUserId(snapshot.getString("userId"));
                                hauntApi.setPreference(snapshot.getString("preference"));
                                ArrayList<String> genders = (ArrayList<String>) snapshot.get("genders");
                                ArrayList<String> likes = (ArrayList<String>) snapshot.get("likes");
                                hauntApi.setGenders(genders);
                                hauntApi.setLikes(likes);

                                Intent intent = new Intent(AddUserPreferencesActivity.this, BrowseProfilesActivity.class);
                                startActivity(intent);
                            }
                        }
                    }
                });
    }
}
