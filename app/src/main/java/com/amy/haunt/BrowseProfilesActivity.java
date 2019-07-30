package com.amy.haunt;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amy.haunt.model.UserProfile;
import com.amy.haunt.ui.UserRecyclerAdapter;
import com.amy.haunt.util.HauntApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class BrowseProfilesActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<UserProfile> usersList;
    private RecyclerView recyclerView;
    private UserRecyclerAdapter userRecyclerAdapter;
    private String preference;
    private String currentUserId;
    private ArrayList<String> currentUserGenders;

    private CollectionReference collectionReference = db.collection("Users");
    private TextView noUsersToBrowse;
//    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_profiles);
        Objects.requireNonNull(getSupportActionBar()).setElevation(3);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setLogo(R.drawable.haunt_blk_smallish);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        noUsersToBrowse = findViewById(R.id.browse_no_users);
        usersList = new ArrayList<>();
//        progressBar = findViewById(R.id.browse_progress);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (HauntApi.getInstance() != null) {
            HauntApi hauntApi = HauntApi.getInstance();
            currentUserId = hauntApi.getUserId();
            preference = hauntApi.getPreference();
            currentUserGenders = HauntApi.getInstance().getGenders();
        }

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.nav_view);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_browse:
                        break;
                    case R.id.action_matches:
                        startActivity(new Intent(BrowseProfilesActivity.this, ViewMatchesActivity.class));
                        break;
                    case R.id.action_signout:
                        Intent c = new Intent(BrowseProfilesActivity.this, LoginActivity.class);
                        startActivity(c);
                        finish();
                        break;
                }
                return false;
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        noUsersToBrowse.setVisibility(View.INVISIBLE);

        if (Objects.equals(preference, "Male") || Objects.equals(preference, "Female")) {
            collectionReference.whereArrayContains("genders",
                    preference)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                noUsersToBrowse.setVisibility(View.INVISIBLE);
                                usersList.clear();
                                for (QueryDocumentSnapshot users : queryDocumentSnapshots) {

                                    UserProfile user = users.toObject(UserProfile.class);
                                    String userPreference = user.getPreference();

                                    if (!Objects.equals(userPreference, "Everyone")) {
                                        boolean contains = currentUserGenders.contains(userPreference);
                                        if (contains && !Objects.equals(user.getUserId(), currentUserId)) {
                                            usersList.add(user);
                                        }
                                    } else {
                                        if (!Objects.equals(user.getUserId(), currentUserId)) {
                                            usersList.add(user);
                                        }
                                    }
                                }

                                userRecyclerAdapter = new UserRecyclerAdapter(BrowseProfilesActivity.this,
                                        usersList);
                                recyclerView.setAdapter(userRecyclerAdapter);
                                userRecyclerAdapter.notifyDataSetChanged();
//                                if (HauntApi.getInstance().getPosition() != 0) {
                                recyclerView.scrollToPosition(HauntApi.getInstance().getPosition());
//                                }
                            } else {
                                noUsersToBrowse.setVisibility(View.VISIBLE);
                            }

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });

        } else {
            collectionReference.get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                usersList.clear();

                                noUsersToBrowse.setVisibility(View.INVISIBLE);
                                for (QueryDocumentSnapshot users : queryDocumentSnapshots) {
                                    UserProfile user = users.toObject(UserProfile.class);
                                    String userPreference = user.getPreference();

                                    boolean contains = currentUserGenders.contains(userPreference);

                                    if (!Objects.equals(userPreference, "Everyone")) {
                                        if (contains && !Objects.equals(user.getUserId(), currentUserId)) {
                                            usersList.add(user);
                                        }
                                    } else {
                                        if (!Objects.equals(user.getUserId(), currentUserId)) {
                                            usersList.add(user);
                                        }
                                    }
                                }
                                userRecyclerAdapter = new UserRecyclerAdapter(BrowseProfilesActivity.this,
                                        usersList);
                                recyclerView.setAdapter(userRecyclerAdapter);
                                userRecyclerAdapter.notifyDataSetChanged();
//                                if (HauntApi.getInstance().getPosition() != 0) {
                                recyclerView.scrollToPosition(HauntApi.getInstance().getPosition());
//                                }
                            } else {
                                noUsersToBrowse.setVisibility(View.VISIBLE);
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
}



//public class BrowseProfilesActivity extends AppCompatActivity {
//
//    private FirebaseAuth firebaseAuth;
//    private FirebaseUser user;
//    private FirebaseFirestore db = FirebaseFirestore.getInstance();
//    private List<UserProfile> usersList;
//    private ArrayList<UserProfile> usersInstance = new ArrayList<>();
//    private RecyclerView recyclerView;
//    private UserRecyclerAdapter userRecyclerAdapter;
//    private String preference;
//    private String currentUserId;
//    private ArrayList<String> currentUserGenders;
//    private static String LIST_STATE = "list_state";
//    private Parcelable savedRecyclerLayoutState;
//    private static final String BUNDLE_RECYCLER_LAYOUT = "recycler_layout";
//
//    private CollectionReference collectionReference = db.collection("Users");
//    private TextView noUsersToBrowse;
////    private ArrayList<String> genders;
////    private ProgressBar progressBar;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_browse_profiles);
//
//        firebaseAuth = FirebaseAuth.getInstance();
//        user = firebaseAuth.getCurrentUser();
//        usersList = new ArrayList<>();
//        if (HauntApi.getInstance() != null) {
//            currentUserId = HauntApi.getInstance().getUserId();
//            preference = HauntApi.getInstance().getPreference();
//            currentUserGenders = HauntApi.getInstance().getGenders();
//        }
//
//
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setTitle("");
//        actionBar.setLogo(R.drawable.haunt_logo_small);
//        actionBar.setDisplayUseLogoEnabled(true);
//        actionBar.setDisplayShowHomeEnabled(true);
//
//        if (savedInstanceState != null){
//            usersInstance = savedInstanceState.getParcelableArrayList(LIST_STATE);
//            savedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
//            displayData();
//        }else {
//            initViews();
//        }
//    }
//
//    private void displayData() {
//        noUsersToBrowse = findViewById(R.id.browse_no_users);
//        recyclerView = findViewById(R.id.recyclerView);
//        recyclerView.setAdapter(userRecyclerAdapter);
//        restoreLayoutManagerPosition();
//        userRecyclerAdapter.notifyDataSetChanged();
//
//    }
//
//    private void restoreLayoutManagerPosition() {
//        if (savedRecyclerLayoutState != null) {
//            recyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
//        }
//    }
//
//    private void initViews() {
//        noUsersToBrowse = findViewById(R.id.browse_no_users);
////        progressBar = findViewById(R.id.browse_progress);
//
//        recyclerView = findViewById(R.id.recyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        loadData();
//        recyclerView.setAdapter(userRecyclerAdapter);
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        switch (item.getItemId()) {
//            case R.id.action_browse:
//                if (user != null && firebaseAuth != null) {
//                    startActivity(new Intent(BrowseProfilesActivity.this,
//                            BrowseProfilesActivity.class));
//                    finish();
//                }
//                break;
//            case R.id.action_matches:
//                if (user != null && firebaseAuth != null) {
//                    startActivity(new Intent(BrowseProfilesActivity.this,
//                            ViewMatchesActivity.class));
//                    finish();
//                }
//                break;
//            case R.id.action_signout:
//                if (user != null && firebaseAuth != null) {
//                    firebaseAuth.signOut();
//
//                    startActivity(new Intent(BrowseProfilesActivity.this,
//                            LoginActivity.class));
//                    finish();
//                }
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//
//    protected void loadData() {
//        noUsersToBrowse.setVisibility(View.INVISIBLE);
//
//        if (Objects.equals(preference, "Male") || Objects.equals(preference, "Female")) {
//            collectionReference.whereArrayContains("genders",
//                    preference)
//                    .get()
//                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                        @Override
//                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                            if (!queryDocumentSnapshots.isEmpty()) {
//                                noUsersToBrowse.setVisibility(View.INVISIBLE);
//                                for (QueryDocumentSnapshot users : queryDocumentSnapshots) {
//
//                                    UserProfile user = users.toObject(UserProfile.class);
//                                    String userPreference = user.getPreference();
//
//                                    if (!Objects.equals(userPreference, "Everyone")) {
//                                        boolean contains = currentUserGenders.contains(userPreference);
//                                        if (contains && !Objects.equals(user.getUserId(), currentUserId)) {
//                                            usersList.add(user);
//                                            usersInstance.add(user);
//                                        }
//                                    } else {
//                                        if (!Objects.equals(user.getUserId(), currentUserId)) { // else if their preference is everyone it isn't the current user
//                                            usersList.add(user);
//                                            usersInstance.add(user);
//                                        }
//                                    }
//                                }
//
//                                userRecyclerAdapter = new UserRecyclerAdapter(BrowseProfilesActivity.this,
//                                        usersList);
//                                recyclerView.setAdapter(userRecyclerAdapter);
//                                userRecyclerAdapter.notifyDataSetChanged();
//                                recyclerView.smoothScrollToPosition(HauntApi.getInstance().getPosition());
//
//                            } else {
//                                noUsersToBrowse.setVisibility(View.VISIBLE);
//                            }
//
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//
//                        }
//                    });
//
//        } else {
//            collectionReference.get()
//                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                        @Override
//                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                            if (!queryDocumentSnapshots.isEmpty()) {
//                                Log.d("where", "onSuccess: in here");
//
//                                noUsersToBrowse.setVisibility(View.INVISIBLE);
//                                for (QueryDocumentSnapshot users : queryDocumentSnapshots) {
//
//                                    UserProfile user = users.toObject(UserProfile.class);
//                                    String userPreference = user.getPreference();
//
//                                    boolean contains = currentUserGenders.contains(userPreference);
//                                    Log.d("where", "onSuccess: " + currentUserGenders);
//
//
//                                    if (!Objects.equals(userPreference, "Everyone")) {
//                                        if (contains && !Objects.equals(user.getUserId(), currentUserId)) {
//                                            usersList.add(user);
//                                            usersInstance.add(user);
//                                        }
//                                    } else {
//                                        if (!Objects.equals(user.getUserId(), currentUserId)) {
//                                            usersList.add(user);
//                                            usersInstance.add(user);
//                                        }
//                                    }
//                                }
//                                userRecyclerAdapter = new UserRecyclerAdapter(BrowseProfilesActivity.this,
//                                        usersList);
//                                recyclerView.setAdapter(userRecyclerAdapter);
//                                userRecyclerAdapter.notifyDataSetChanged();
//                                recyclerView.smoothScrollToPosition(HauntApi.getInstance().getPosition());
//                            } else {
//                                noUsersToBrowse.setVisibility(View.VISIBLE);
//                            }
//
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//
//                        }
//                    });
//
//        }
//    }
//
//    @Override
//    public void onSaveInstanceState(Bundle savedInstanceState) {
//        super.onSaveInstanceState(savedInstanceState);
//        savedInstanceState.putParcelableArrayList(LIST_STATE, usersInstance);
//        savedInstanceState.putParcelable(BUNDLE_RECYCLER_LAYOUT, recyclerView.getLayoutManager().onSaveInstanceState());
//    }
//
//    @Override
//    public void onRestoreInstanceState(Bundle savedInstanceState) {
//        usersInstance = savedInstanceState.getParcelableArrayList(LIST_STATE);
//        savedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
//        super.onRestoreInstanceState(savedInstanceState);
//    }
//}
//
//
