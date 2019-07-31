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

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class UserRecyclerAdapter extends RecyclerView.Adapter<UserRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<UserProfile> userProfileList;
    private String currentUserId;
    private ArrayList<String> likes;
    private ImageView imageToast;
    private String currentUserZodiac;

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
        String userHeading = userProfile.getFirstName() + ", " + userProfile.getAge();
        String sun_sign = userProfile.getZodiac();
        String moon_sign = randomSignFromArray();
        String rising_sign = randomSignFromArray();
        String kids = userProfile.getKids();
        String drinking = userProfile.getDrinking();
        String smoking = userProfile.getSmoking();
        String height = userProfile.getHeight();
        String compatibility = setCompatibility(sun_sign);

        int id = context.getResources().getIdentifier("com.amy.haunt:drawable/" + compatibility, null, null);


        String imageUrl;
        viewHolder.blurb.setText(userProfile.getAboutMe());
        viewHolder.name.setText(userHeading);
        viewHolder.sunSign.setText(sun_sign);
        viewHolder.moonSign.setText(moon_sign);
        viewHolder.risingSign.setText(rising_sign);
        viewHolder.smoking.setText(smoking);
        viewHolder.drinking.setText(drinking);
        viewHolder.kids.setText(kids);
        viewHolder.height.setText(height);
        viewHolder.compatibility.setImageResource(id);
        imageUrl = userProfile.getProfilePhotoUrl();

        if (HauntApi.getInstance() != null) {
            likes = HauntApi.getInstance().getLikes();
        }
        boolean contains = likes.contains(userProfile.getUserId());
        if (contains) {
            viewHolder.likeButton.setImageResource(R.drawable.liked_button_large);
        } else {
            viewHolder.likeButton.setImageResource(R.drawable.like_button_large);
        }

        Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.image_loading_bg)
                .resize(600,1000)
                .centerInside()
                .into(viewHolder.image);

//        viewHolder.compatibility.setText(userProfile.getBirthday());

    }

    private String setCompatibility(String sunSign) {

        String compatibility = new String();

        ArrayList<String> waterSigns = new ArrayList<>();
        waterSigns.add("Cancer");
        waterSigns.add("Scorpio");
        waterSigns.add("Pisces");

        ArrayList<String> fireSigns = new ArrayList<>();
        fireSigns.add("Aries");
        fireSigns.add("Leo");
        fireSigns.add("Sagittarius");

        ArrayList<String> airSigns = new ArrayList<>();
        airSigns.add("Gemini");
        airSigns.add("Libra");
        airSigns.add("Aquarius");

        ArrayList<String> earthSigns = new ArrayList<>();
        earthSigns.add("Taurus");
        earthSigns.add("Virgo");
        earthSigns.add("Capricorn");

        currentUserZodiac = HauntApi.getInstance().getZodiac();

        if (waterSigns.contains(currentUserZodiac)) {
            if (waterSigns.contains(sunSign)) {
                compatibility = "ic_filter_drama_black_24dp";
            } else if (fireSigns.contains(sunSign)) {
                compatibility = "ic_extension_black_24dp";
            } else if (airSigns.contains(sunSign)) {
                compatibility = "ic_local_pizza_black_24dp";
            } else {
                compatibility = "ic_weekend_black_24dp";
            }
        } else if (fireSigns.contains(currentUserZodiac)) {
            if (waterSigns.contains(sunSign)) {
                compatibility = "ic_extension_black_24dp";
            } else if (fireSigns.contains(sunSign)) {
                compatibility = "ic_whatshot_black_24dp";
            } else if (airSigns.contains(sunSign)) {
                compatibility = "outline_motorcycle_black_18dp";
            } else {
                compatibility = "ic_flash_on_black_24dp";
            }
        } else if (airSigns.contains(currentUserZodiac)) {
            if (waterSigns.contains(sunSign)) {
                compatibility = "ic_local_pizza_black_24dp";
            } else if (fireSigns.contains(sunSign)) {
                compatibility = "outline_motorcycle_black_18dp";
            } else if (airSigns.contains(sunSign)) {
                compatibility = "outline_waves_black_18dp";
            } else {
                compatibility = "outline_pan_tool_black_18dp";
            }
        } else if (earthSigns.contains(currentUserZodiac)) {
            if (waterSigns.contains(sunSign)) {
                compatibility = "ic_weekend_black_24dp";
            } else if (fireSigns.contains(sunSign)) {
                compatibility = "ic_flash_on_black_24dp";
            } else if (airSigns.contains(sunSign)) {
                compatibility = "outline_pan_tool_black_18dp";
            } else {
                compatibility = "outline_wb_sunny_black_18dp";
            }
        }
        return compatibility;
    }

    @Override
    public int getItemCount() {
        return userProfileList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name,
                sunSign, blurb,
                moonSign, risingSign,
                height, kids,
                drinking, smoking;

        public ImageView image;
        public ImageView compatibility;
        public ImageView likeButton;

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;

            height = itemView.findViewById(R.id.browse_users_height);
            kids = itemView.findViewById(R.id.browse_users_kids);
            drinking = itemView.findViewById(R.id.browse_users_drink);
            smoking = itemView.findViewById(R.id.browse_users_smoke);
            name = itemView.findViewById(R.id.browse_users_name);
            sunSign = itemView.findViewById(R.id.browse_users_sun_sign);
            moonSign = itemView.findViewById(R.id.browse_users_moon_sign);
            risingSign = itemView.findViewById(R.id.browse_users_rising_sign);
            image = itemView.findViewById(R.id.browse_users_image);
            likeButton = itemView.findViewById(R.id.like_user);
            blurb = itemView.findViewById(R.id.browse_users_blurb);
            compatibility = itemView.findViewById((R.id.browse_users_compat));

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
                            String imageUrl = (String) documentSnapshot.get("profilePhotoUrl");
                            boolean contains = likes.contains(currentUserId);
                            if (contains) {
                                createMatch(likedUser, currentUserId);
                                showToast(imageUrl);
                            }
                        }
                    } else {
                        Log.d("checkMatch", "get failed with ", task.getException());
                    }
                }
            });
        }

        private void createMatch(final String likedUser, final String currentUserId) {
            collectionReference.document(likedUser)
                    .update("matches", FieldValue.arrayUnion(currentUserId))
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
            collectionReference.document(currentUserId)
                    .update("matches", FieldValue.arrayUnion(likedUser))
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
        }
    }

    public String randomSignFromArray()
    {
        String[] arr={"Capricorn", "Aquarius", "Virgo", "Taurus", "Sagittarius", "Cancer", "Leo", "Libra", "Aries", "Scorpio", "Gemini", "Pisces"};
        Random r = new Random();
        int randomNumber=r.nextInt(arr.length);

        return arr[randomNumber];
    }

    private void showToast(String imageUrl) {
        LayoutInflater inflater = LayoutInflater.from(context);
        Log.d("wttf", "showToast: " + context);
        View layout = inflater.inflate( R.layout.match_toast, null );
        imageToast = layout.findViewById(R.id.match_toast_image);

        Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.image_loading_bg)
                .resize(1000,1000)
                .transform(new CropCircleTransformation())
                .centerInside()
                .into(imageToast);

        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);

        toast.show();
    }
}
