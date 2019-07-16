package com.amy.haunt;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amy.haunt.model.UserProfile;
import com.amy.haunt.util.HauntApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class CreateUserProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PROFILE_PHOTO_CODE = 1;
    private Button saveInfoButton;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storageReference;
    private CollectionReference collectionReference = db.collection("Users");

    private String currentUserId;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText heightEditText;
    private EditText genderEditText;
    private ImageView addPhotoButton;
//    private ImageView imageView;
    private ProgressBar progressBar;
    private Uri imageUri;
    private EditText birthdayEditText; //change back to TextView if doing spinner

    private DatePickerDialog.OnDateSetListener onSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user_profile);
        Objects.requireNonNull(getSupportActionBar()).setElevation(0);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setLogo(R.drawable.haunt_logo_small);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        firstNameEditText = findViewById(R.id.first_name_profile);
        lastNameEditText = findViewById(R.id.last_name_profile);
        heightEditText = findViewById(R.id.height_profile);
        genderEditText = findViewById(R.id.gender_profile);
        birthdayEditText = findViewById(R.id.birthday_profile);
        progressBar = findViewById(R.id.create_user_profile_progress);
//        imageView = findViewById(R.id.add_image_view);

        saveInfoButton = findViewById(R.id.create_user_profile_button);
        saveInfoButton.setOnClickListener(this);
        addPhotoButton = findViewById(R.id.add_image_button);
        addPhotoButton.setOnClickListener(this);

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

//    public static class DatePickerFragment extends DialogFragment
//            implements DatePickerDialog.OnDateSetListener {
//
//        private final TextView birthday;
//
//        public DatePickerFragment(TextView birthday) {
//            this.birthday = birthday;
//        }
//
//        @Override
//        public Dialog onCreateDialog(Bundle savedInstanceState) {
//            // Use the current date as the default date in the picker
//            final Calendar c = Calendar.getInstance();
//            int year = c.get(Calendar.YEAR);
//            int month = c.get(Calendar.MONTH);
//            int day = c.get(Calendar.DAY_OF_MONTH);
//
//            // Create a new instance of DatePickerDialog and return it
//            return new DatePickerDialog(getActivity(), this, year, month, day);
//        }
//
//        public void onDateSet(DatePicker view, int year, int month, int day) {
//            Log.d("DateSet", "onDateSet: is firing");
//            // Do something with the date chosen by the user
//            month = month + 1;
//            String date = month+"/"+day+"/"+year;
//            birthday.setText(date);
//        }
//    }

//    public void showDatePickerDialog(View v) {
//        DialogFragment newFragment = new DatePickerFragment(b);
//        newFragment.show(getSupportFragmentManager(), "datePicker");
//    }

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
        switch (view.getId()) {
            case R.id.create_user_profile_button:
                //save user info
                saveUserInfo();
                break;
            case R.id.add_image_button:
                Intent profilePhotoIntent = new Intent(Intent.ACTION_GET_CONTENT);
                profilePhotoIntent.setType("image/*");
                startActivityForResult(profilePhotoIntent, PROFILE_PHOTO_CODE);
                break;
        }
    }

    private void saveUserInfo() {
        final String firstName = firstNameEditText.getText().toString().trim();
        final String lastName = lastNameEditText.getText().toString().trim();
        final String height = heightEditText.getText().toString().trim();
        final String gender = genderEditText.getText().toString().trim();
        final String birthday = birthdayEditText.getText().toString().trim();
        final ArrayList<String> likes = new ArrayList<>();
        final ArrayList<String> matches = new ArrayList<>();

        progressBar.setVisibility(View.VISIBLE);

        if (!TextUtils.isEmpty(firstName) && !TextUtils.isEmpty(lastName) && !TextUtils.isEmpty(height)
                && !TextUtils.isEmpty(gender) && !TextUtils.isEmpty(birthday) && imageUri != null) {

            final StorageReference filepath = storageReference
                    .child("profile_images")
                    .child("profile_image_" + currentUserId);
            filepath.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressBar.setVisibility(View.INVISIBLE);

                            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    String imageUri = uri.toString();

                                    UserProfile userProfile = new UserProfile();
                                    userProfile.setFirstName(firstName);
                                    userProfile.setLastName(lastName);
                                    userProfile.setGender(gender);
                                    userProfile.setHeight(height);
                                    userProfile.setProfilePhotoUrl(imageUri);
                                    userProfile.setBirthday(birthday);
                                    userProfile.setUserId(currentUserId);
                                    userProfile.setLikes(likes);
                                    userProfile.setMatches(matches);

                                    collectionReference.document(currentUserId)
                                            .set(userProfile, SetOptions.merge())
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    progressBar.setVisibility(View.INVISIBLE);
                                                    startActivity(new Intent(CreateUserProfileActivity.this, BrowseProfilesActivity.class));
                                                    finish();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.d("CreateProfileActivity", "onFailure: " + e.getMessage());
                                                }
                                            });
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });
        }else {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PROFILE_PHOTO_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                imageUri = data.getData();
//                imageView.setImageURI(imageUri);//show image
            }
        }
    }
}
