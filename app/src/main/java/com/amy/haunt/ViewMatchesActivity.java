package com.amy.haunt;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ViewMatchesActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_matches);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setLogo(R.drawable.haunt_logo_small);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_browse:
                if (user != null && firebaseAuth != null) {
                    startActivity(new Intent(ViewMatchesActivity.this,
                            BrowseProfilesActivity.class));
                    //finish();
                }
                break;
            case R.id.action_matches:
                if (user != null && firebaseAuth != null) {
                    startActivity(new Intent(ViewMatchesActivity.this,
                            ViewMatchesActivity.class));
                    //finish();
                }
                break;
            case R.id.action_signout:
                if (user != null && firebaseAuth != null) {
                    firebaseAuth.signOut();

                    startActivity(new Intent(ViewMatchesActivity.this,
                            LoginActivity.class));
                    //finish();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
