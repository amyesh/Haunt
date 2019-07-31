package com.amy.haunt.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amy.haunt.R;
import com.amy.haunt.VoiceActivity;
import com.amy.haunt.model.UserProfile;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

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
                .resize(1000,1000)
                .transform(new CropCircleTransformation())
                .centerInside()
                .into(viewHolder.image);
    }

    @Override
    public int getItemCount() {
        return matchProfileList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public ImageView image;
        public FloatingActionButton callButton;

        public ViewHolder(@NonNull View itemView, final Context ctx) {
            super(itemView);
            context = ctx;

            name = itemView.findViewById(R.id.match_name);
            image = itemView.findViewById(R.id.match_image);
            callButton = itemView.findViewById(R.id.match_call);

            callButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent makeCall = new Intent(ctx, VoiceActivity.class);
                    makeCall.putExtra("MATCH", name.getText());

                    ctx.startActivity(makeCall);
                }
            });

        }
    }
}
