package com.amy.haunt;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.amy.haunt.util.HauntApi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    private Button loginButton;
    private Button createAcctButton;

    private AutoCompleteTextView emailAddress;
    private EditText password;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;
    private ProgressBar progressBar;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressBar = findViewById(R.id.login_progress);

        getSupportActionBar().hide();

        firebaseAuth = FirebaseAuth.getInstance();

        emailAddress = findViewById(R.id.email);
        password = findViewById(R.id.password);

        loginButton = findViewById(R.id.email_sign_in_button);
        createAcctButton = findViewById(R.id.create_acct_button_login);

        createAcctButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, CreateAccount.class));
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginEmailPasswordUser(emailAddress.getText().toString().trim(),
                        password.getText().toString().trim());
            }
        });
    }

    private void loginEmailPasswordUser(String email, String pwd) {

        progressBar.setVisibility(View.VISIBLE);

        if (!TextUtils.isEmpty(email)
                && !TextUtils.isEmpty(pwd)) {
            firebaseAuth.signInWithEmailAndPassword(email, pwd)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            assert user != null;
                            final String currentUserId = user.getUid();

                            collectionReference
                                    .whereEqualTo("userId", currentUserId)
                                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots,
                                                            @Nullable FirebaseFirestoreException e) {

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
                                                    hauntApi.setZodiac(snapshot.getString("zodiac"));
                                                    ArrayList<String> genders = (ArrayList<String>) snapshot.get("genders");
                                                    ArrayList<String> likes = (ArrayList<String>) snapshot.get("likes");
                                                    hauntApi.setGenders(genders);
                                                    hauntApi.setLikes(likes);

                                                    startActivity(new Intent(LoginActivity.this,
                                                            BrowseProfilesActivity.class));
                                                }
                                            }
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(LoginActivity.this,
                                        "We don't have a record of that username and/or password",
                                        Toast.LENGTH_LONG)
                                        .show();
                            }
                    });
        }else {

            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(LoginActivity.this,
                    "Please enter email and password",
                    Toast.LENGTH_LONG)
                    .show();
        }
    }
}
