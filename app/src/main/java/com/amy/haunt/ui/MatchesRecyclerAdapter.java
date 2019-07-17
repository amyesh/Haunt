package com.amy.haunt.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.amy.haunt.R;
import com.amy.haunt.model.UserProfile;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MatchesRecyclerAdapter extends RecyclerView.Adapter<MatchesRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<UserProfile> matchProfileList;
    private String currentUserId;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users");

    public MatchesRecyclerAdapter(Context context, List<UserProfile> matchProfileList) {
        this.context = context;
        this.matchProfileList = matchProfileList;
    }

    @NonNull
    @Override
    public MatchesRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.match_row, viewGroup, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchesRecyclerAdapter.ViewHolder viewHolder, int position) {
        UserProfile userProfile = matchProfileList.get(position);
        String imageUrl;

        viewHolder.name.setText(userProfile.getFirstName());
        imageUrl = userProfile.getProfilePhotoUrl();

        Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.image_loading_bg)
                .fit()
                .into(viewHolder.image);
    }

    @Override
    public int getItemCount() {
        return matchProfileList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public ImageView image;
        public CardView viewProfileButton;
        public ImageView callButton;

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;

            name = itemView.findViewById(R.id.match_name);
            image = itemView.findViewById(R.id.match_image);
            viewProfileButton = itemView.findViewById(R.id.match_cardview);
            callButton = itemView.findViewById(R.id.match_call);
            viewProfileButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Todo: Add functionality to view user profile
                    //Todo: Add ViewProfileActivity - reuse user_row view?
//                    int position = getLayoutPosition();
//
//                    //creating an intent
//                    Intent intent = new Intent(getApplicationContext(), ViewProfileActivity.class);
//
//                    //putting artist name and id to intent
//
//                    intent.putExtra(MATCH_USER_ID, userProfile.getUserId());
//                    intent.putExtra(MATCH_NAME, userProfile.getUserName());
//
//                    //starting the activity with intent
//                    startActivity(intent);
                }
            });

            callButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Todo: Add functionality to call user
//                    int position = getLayoutPosition();
//
//                    //creating an intent
//                    Intent intent = new Intent(getApplicationContext(), ViewProfileActivity.class);
//
//                    //putting artist name and id to intent
//
//                    intent.putExtra(MATCH_USER_ID, userProfile.getUserId());
//                    intent.putExtra(MATCH_NAME, userProfile.getUserName());
//
//                    //starting the activity with intent
//                    startActivity(intent);
                }
            });

        }
    }
}
