package com.amy.haunt.ui;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    private int currentPosition;
    private ArrayList<String> likes;
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
        viewHolder.blurb.setText(userProfile.getAboutMe());
        viewHolder.name.setText(userHeading);
        viewHolder.astro_sign.setText(zodiac_signs);
        imageUrl = userProfile.getProfilePhotoUrl();

        if (HauntApi.getInstance() != null) {
            likes = HauntApi.getInstance().getLikes();
        }
        boolean contains = likes.contains(userProfile.getUserId());
        if (contains) {
            viewHolder.likeButton.setImageResource(R.drawable.liked_button_large);
        } else {
            viewHolder.likeButton.setImageResource(R.drawable.like_button_large);
            Log.d("wtf", "likebutton");
        }

//        Picasso.get()
//                .load(imageUrl)
//                .placeholder(R.drawable.image_loading_bg)
//                .resize(600,1000)
//                .centerInside()
//                .into(viewHolder.matchImage);


        Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.image_loading_bg)
                .resize(600,1000)
                .centerInside()
                .into(viewHolder.image);

//        viewHolder.compatibility.setText(userProfile.getBirthday());

    }

    @Override
    public int getItemCount() {
        return userProfileList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name,
        astro_sign, blurb;
//        compatibility;

        public ImageView image;
        public ImageView matchImage;
        public ImageView likeButton;

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;

            matchImage = itemView.findViewById(R.id.match_toast_image);
            name = itemView.findViewById(R.id.browse_users_name);
            astro_sign = itemView.findViewById(R.id.browse_users_sign);
            image = itemView.findViewById(R.id.browse_users_image);
            likeButton = itemView.findViewById(R.id.like_user);
            blurb = itemView.findViewById(R.id.browse_users_blurb);
//            compatibility = itemView.findViewById(R.id.browse_users_compat);
            likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int position = getLayoutPosition();
                    String likedUserId = userProfileList.get(position).getUserId();

                    if (HauntApi.getInstance() != null) {
                        currentUserId = HauntApi.getInstance().getUserId();
                        HauntApi.getInstance().setPosition(position);
                    }

                    saveUserLike(likedUserId);
                }
            });
        }

//        public class MatchesDialogFragment extends DialogFragment {
//            @Override
//            public Dialog onCreateDialog(Bundle savedInstanceState) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                builder.setTitle("It's a match!");
//                builder.setMessage("Would you like to view your matches?")
//                        .setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                startActivity(new Intent(context, ViewMatchesActivity.class));
//                            }
//                        })
//                        .setNegativeButton("No thanks.", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                            }
//                        });
//                return builder.create();
//            }
//        }

        private void saveUserLike(final String likedUser) {
            collectionReference.document(currentUserId)
                .update("likes", FieldValue.arrayUnion(likedUser))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        checkUserMatch(likedUser, currentUserId);
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
//                            String matchUrl = (String) documentSnapshot.get("profilePhotoUrl");
                            boolean contains = likes.contains(currentUserId);
                            if (contains) {
                                createMatch(likedUser, currentUserId);
//                                Toast.makeText(context,
//                                        "It's a match!",
//                                        Toast.LENGTH_LONG)
//                                        .show();
                                showToast();
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

    private void showToast() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate( R.layout.match_toast, null );

        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);

        toast.show();
    }
}


