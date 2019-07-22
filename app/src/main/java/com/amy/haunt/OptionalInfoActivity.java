package com.amy.haunt;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class OptionalInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_optional_info);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setLogo(R.drawable.haunt_logo_small);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
    }

    public void onRadioKidsButtonClicked(View view) {
    }

    public void onRadioDrinkButtonClicked(View view) {
    }

    public void onRadioSmokeButtonClicked(View view) {
    }

    public void onRadioDrugsButtonClicked(View view) {
    }
}
