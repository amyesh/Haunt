package com.amy.haunt;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.nav_view);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.browse_profiles:
                        Intent a = new Intent(BottomNavActivity.this, BrowseProfilesActivity.class);
                        startActivity(a);
                        break;
                    case R.id.view_matches:
                        Intent b = new Intent(BottomNavActivity.this, ViewMatchesActivity.class);
                        startActivity(b);
                        break;
                    case R.id.login:
                        Intent c = new Intent(BottomNavActivity.this, LoginActivity.class);
                        startActivity(c);
                        break;
                }
                return false;
            }
        });

    }
}
