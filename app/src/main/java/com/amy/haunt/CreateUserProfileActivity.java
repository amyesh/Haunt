package com.amy.haunt;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.amy.haunt.model.UserProfile;
import com.amy.haunt.util.HauntApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Objects;

public class CreateUserProfileActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, View.OnClickListener {

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
    private ImageView addPhotoButton;
    private TextView birthdayTextView;
    private ImageView imageView;
    private ProgressBar progressBar;
    private Uri imageUri;
    private String age;
    private String zodiac;


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
        birthdayTextView = findViewById(R.id.birthday_text);
        progressBar = findViewById(R.id.create_user_profile_progress);
        imageView = findViewById(R.id.add_image_view);

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

        findViewById(R.id.birthday_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
    }

    private void showDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private String getAge(int year, int month, int day){
        java.util.Calendar birthdate = java.util.Calendar.getInstance();
        java.util.Calendar today = java.util.Calendar.getInstance();

        birthdate.set(year, month, day);

        int age = today.get(java.util.Calendar.YEAR) - birthdate.get(java.util.Calendar.YEAR);

        if (today.get(java.util.Calendar.DAY_OF_YEAR) < birthdate.get(java.util.Calendar.DAY_OF_YEAR)){
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageString = ageInt.toString();

        return ageString;
    }

    private String getZodiac(int year, int month, int day){
        String sign = new String();

        switch (month) {
            case 1:
                if (day <= 19) {
                    sign = "Capricorn";
                } else {
                    sign = "Aquarius";
                }
                break;
            case 2:
                if (day <= 18) {
                    sign = "Aquarius";
                } else {
                    sign = "Pisces";
                }
                break;
            case 3:
                if (day <= 19) {
                    sign = "Pisces";
                } else {
                    sign = "Aries";
                }
                break;
            case 4:
                if (day <= 19) {
                    sign = "Aries";
                } else {
                    sign = "Taurus";
                }
                break;
            case 5:
                if (day <= 20) {
                    sign = "Taurus";
                } else {
                    sign = "Gemini";
                }
                break;
            case 6:
                if (day <= 20) {
                    sign = "Gemini";
                } else {
                    sign = "Cancer";
                }
                break;
            case 7:
                if (day <= 22) {
                    sign = "Cancer";
                } else {
                    sign = "Leo";
                }
                break;
            case 8:
                if (day <= 22) {
                    sign = "Leo";
                } else {
                    sign = "Virgo";
                }
                break;
            case 9:
                if (day <= 22) {
                    sign = "Virgo";
                } else {
                    sign = "Libra";
                }
                break;
            case 10:
                if (day <= 21) {
                    sign = "Libra";
                } else {
                    sign = "Scorpio";
                }
                break;
            case 11:
                if (day <= 21) {
                    sign = "Scorpio";
                } else {
                    sign = "Sagittarius";
                }
                break;
            case 12:
                if (day <= 21) {
                    sign = "Sagittarius";
                } else {
                    sign = "Capricorn";
                }
                break;
        }
        return sign;
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
        switch (view.getId()) {
            case R.id.create_user_profile_button:
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
        final String birthday = birthdayTextView.getText().toString().trim();
        final ArrayList<String> likes = new ArrayList<>();
        final ArrayList<String> matches = new ArrayList<>();

        progressBar.setVisibility(View.VISIBLE);

        if (!TextUtils.isEmpty(firstName) && !TextUtils.isEmpty(lastName) && !TextUtils.isEmpty(height)
                && !TextUtils.isEmpty(birthday) && imageUri != null) {

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

                                    final UserProfile userProfile = new UserProfile();
                                    userProfile.setFirstName(firstName);
                                    userProfile.setLastName(lastName);
                                    userProfile.setHeight(height);
                                    userProfile.setProfilePhotoUrl(imageUri);
                                    userProfile.setBirthday(birthday);
                                    userProfile.setUserId(currentUserId);
                                    userProfile.setLikes(likes);
                                    userProfile.setMatches(matches);
                                    userProfile.setZodiac(zodiac);
                                    userProfile.setAge(age);

                                    collectionReference.document(currentUserId)
                                            .set(userProfile, SetOptions.merge())
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    progressBar.setVisibility(View.INVISIBLE);

                                                    HauntApi hauntApi = HauntApi.getInstance();
                                                    hauntApi.setUserId(userProfile.getUserId());

                                                    startActivity(new Intent(CreateUserProfileActivity.this, AboutMeActivity.class));
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
                imageView.setImageURI(imageUri);
            }
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        int monthActual = month + 1;
        String date = monthActual + "/" + dayOfMonth + "/" + year;
        birthdayTextView.setText(date);
        age = getAge(year, monthActual, dayOfMonth);
        zodiac = getZodiac(year, monthActual, dayOfMonth);
    }
}
