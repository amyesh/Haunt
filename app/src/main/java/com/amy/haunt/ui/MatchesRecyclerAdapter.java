package com.amy.haunt.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.amy.haunt.model.UserProfile;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
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
    public MatchesRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MatchesRecyclerAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
//            context = ctx;
        }
    }
}
