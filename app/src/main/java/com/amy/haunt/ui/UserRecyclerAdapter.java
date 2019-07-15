package com.amy.haunt.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.amy.haunt.R;
import com.amy.haunt.model.UserProfile;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UserRecyclerAdapter extends RecyclerView.Adapter<UserRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<UserProfile> userProfileList;

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
                .placeholder(R.drawable.ic_launcher_background)
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

        public TextView
                name,
//                astro_sign,
//                blurb,
                compatibility;
        public ImageView image;
        String currentUserId;

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;

            name = itemView.findViewById(R.id.browse_users_name);
//            astro_sign = itemView.findViewById(R.id.browse_users_sign);
//            blurb = itemView.findViewById(R.id.browse_users_blurb);
//            compatibility = itemView.findViewById(R.id.browse_users_compat);
            image = itemView.findViewById(R.id.browse_users_image);
        }
    }
}
