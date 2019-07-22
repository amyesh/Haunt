package com.amy.haunt;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class OptionalInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private Button saveInfoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_optional_info);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setLogo(R.drawable.haunt_logo_small);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        saveInfoButton = findViewById(R.id.opt_button);
        saveInfoButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        startActivity(new Intent(OptionalInfoActivity.this, AddUserPreferencesActivity.class));
        finish();
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
