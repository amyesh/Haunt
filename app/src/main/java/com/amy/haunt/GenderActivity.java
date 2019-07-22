package com.amy.haunt;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class GenderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gender);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setLogo(R.drawable.haunt_logo_small);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
    }

    public void onCheckboxClicked(View view) {
    }
}


//    TextView feedback = (TextView) findViewById(R.id.TextViewSendFeedback);
//feedback.setText(Html.fromHtml("<a href=\"mailto:ask@me.it\">Send Feedback</a>"));
//        feedback.setMovementMethod(LinkMovementMethod.getInstance());
