package com.amy.haunt;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amy.haunt.model.UserProfile;
import com.amy.haunt.ui.MatchesRecyclerAdapter;
import com.amy.haunt.util.HauntApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ViewMatchesActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storageReference;
    private List<UserProfile> matchesList;
    private RecyclerView recyclerView;
    private MatchesRecyclerAdapter matchesRecyclerAdapter;

    private CollectionReference collectionReference = db.collection("Users");
    private TextView noMatches;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_matches);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setLogo(R.drawable.haunt_logo_small);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        noMatches = findViewById(R.id.browse_no_users);

        matchesList = new ArrayList<>();

        recyclerView = findViewById(R.id.matchRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_browse:
                if (user != null && firebaseAuth != null) {
                    startActivity(new Intent(ViewMatchesActivity.this,
                            BrowseProfilesActivity.class));
                    //finish();
                }
                break;
            case R.id.action_matches:
                if (user != null && firebaseAuth != null) {
                    startActivity(new Intent(ViewMatchesActivity.this,
                            ViewMatchesActivity.class));
                    //finish();
                }
                break;
            case R.id.action_signout:
                if (user != null && firebaseAuth != null) {
                    firebaseAuth.signOut();

                    startActivity(new Intent(ViewMatchesActivity.this,
                            LoginActivity.class));
                    //finish();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        noMatches.setVisibility(View.INVISIBLE);

        if (HauntApi.getInstance() != null) {
            currentUserId = HauntApi.getInstance().getUserId();
        }

        collectionReference.whereArrayContains("matches",
                currentUserId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            noMatches.setVisibility(View.INVISIBLE);
                            for (QueryDocumentSnapshot users : queryDocumentSnapshots) {
                                UserProfile user = users.toObject(UserProfile.class);
                                matchesList.add(user);
                            }

                            matchesRecyclerAdapter = new MatchesRecyclerAdapter(ViewMatchesActivity.this,
                                    matchesList);
                            recyclerView.setAdapter(matchesRecyclerAdapter);
                            matchesRecyclerAdapter.notifyDataSetChanged();

                        } else {
                            noMatches.setVisibility(View.VISIBLE);
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }
}
