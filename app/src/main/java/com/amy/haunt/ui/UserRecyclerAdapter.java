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

public class UserRecyclerAdapter extends RecyclerView.Adapter<UserRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<UserProfile> userProfileList;
    private String currentUserId;

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
        String imageUrl;
        viewHolder.name.setText(userProfile.getFirstName());
        imageUrl = userProfile.getProfilePhotoUrl();

        Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.image_loading_bg)
                .fit()
                .into(viewHolder.image);

//        viewHolder.blurb.setText(userProfile.getLastName());
//        viewHolder.compatibility.setText(userProfile.getBirthday());
//        viewHolder.astro_sign.setText(userProfile.getBirthday());
    }

    @Override
    public int getItemCount() {
        return userProfileList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
//        astro_sign,
//        blurb,
//        compatibility;

        public ImageView image;
        public ImageView likeButton;
//        public ImageView dislikeButton;


        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;

            name = itemView.findViewById(R.id.browse_users_name);
//            astro_sign = itemView.findViewById(R.id.browse_users_sign);
//            blurb = itemView.findViewById(R.id.browse_users_blurb);
//            compatibility = itemView.findViewById(R.id.browse_users_compat);
            image = itemView.findViewById(R.id.browse_users_image);
            likeButton = itemView.findViewById(R.id.like_user);
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
                            if (contains == true) {
                                //Todo: create notification (Toast?) that user has matched!
                                createMatch(likedUser, currentUserId);
                            }
                        } else {
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
}

