package com.amy.haunt.ui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amy.haunt.R;
import com.amy.haunt.model.UserProfile;
import com.amy.haunt.util.HauntApi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UserRecyclerAdapter extends RecyclerView.Adapter<UserRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<UserProfile> userProfileList;
    private String currentUserId;
    private String userHeading;
    private String zodiac_signs;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users");

    public UserRecyclerAdapter(Context context, List<UserProfile> userProfileList) {
        this.context = context;
        this.userProfileList = userProfileList;
    }

    @NonNull
    @Override
    public UserRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.user_row, viewGroup, false);

        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull UserRecyclerAdapter.ViewHolder viewHolder, int position) {

        UserProfile userProfile = userProfileList.get(position);
        userHeading = userProfile.getFirstName() + ", " + userProfile.getAge();
        zodiac_signs = userProfile.getZodiac() + " / " + randomSignFromArray() + " / " + randomSignFromArray();
        String imageUrl;
        viewHolder.name.setText(userHeading);
        viewHolder.astro_sign.setText(zodiac_signs);
        imageUrl = userProfile.getProfilePhotoUrl();

        Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.image_loading_bg)
                .resize(600,1000)
                .centerInside()
                .into(viewHolder.image);

//        viewHolder.blurb.setText(userProfile.getLastName());
//        viewHolder.compatibility.setText(userProfile.getBirthday());
    }

    @Override
    public int getItemCount() {
        return userProfileList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name,
        astro_sign;
//        blurb,
//        compatibility;

        public ImageView image;
        public ImageView likeButton;
//        public ImageView dislikeButton;


        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;

            name = itemView.findViewById(R.id.browse_users_name);
            astro_sign = itemView.findViewById(R.id.browse_users_sign);
            image = itemView.findViewById(R.id.browse_users_image);
            likeButton = itemView.findViewById(R.id.like_user);
//            blurb = itemView.findViewById(R.id.browse_users_blurb);
//            compatibility = itemView.findViewById(R.id.browse_users_compat);
//            dislikeButton = itemView.findViewById(R.id.dislike_user);
            likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    context.startActivity();

                    if (HauntApi.getInstance() != null) {
                        currentUserId = HauntApi.getInstance().getUserId();
                    }

                    int position = getLayoutPosition();
                    String likedUserId = userProfileList.get(position).getUserId();
                    likeButton.setImageResource(R.drawable.liked_button_large);
                    Log.d("likeButton", "onClick: likedUserId " + likedUserId + " Current User ID: " + currentUserId);

                    saveUserLike(likedUserId);
                }
            });
        }

        private void saveUserLike(final String likedUser) {
            collectionReference.document(currentUserId)
                .update("likes", FieldValue.arrayUnion(likedUser))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        checkUserMatch(likedUser, currentUserId);
                        Log.d("AddLikeInRecyclerAdapter", "onSuccess: " + likedUser + " Current User: " + currentUserId);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("AddLikeInRecyclerAdapter", "onFailure: " + e.getMessage());
                    }
                });
        }
        private void checkUserMatch(final String likedUser, final String currentUserId) {
            collectionReference.document(likedUser).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if (documentSnapshot.exists()) {
                            ArrayList<String> likes = (ArrayList<String>) documentSnapshot.get("likes");
                            boolean contains = likes.contains(currentUserId);
                            if (contains) {
                                //Todo: create notification (Toast?) that user has matched!
                                createMatch(likedUser, currentUserId);
                            }
                        }
                    } else {
                        Log.d("checkMatch", "get failed with ", task.getException());
                    }
                }
            });
        }

        private void createMatch(final String likedUser, final String currentUserId) {
            collectionReference.document(currentUserId)
                    .update("matches", FieldValue.arrayUnion(likedUser))
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("createMatch", "onSuccess: added " + likedUser);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("createMatch", "onFailure: " + e.getMessage());
                        }
                    });
            collectionReference.document(likedUser)
                    .update("matches", FieldValue.arrayUnion(currentUserId))
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("createMatch", "onSuccess: added " + currentUserId);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("createMatch", "onFailure: " + e.getMessage());
                        }
                    });
        }
    }

    public String randomSignFromArray()
        {
            String[] arr={"Capricorn", "Aquarius", "Virgo", "Taurus", "Sagittarius", "Cancer", "Leo", "Libra", "Aries", "Scorpio", "Gemini", "Pisces"};
            Random r = new Random();
            int randomNumber=r.nextInt(arr.length);

            return arr[randomNumber];
        }
}

