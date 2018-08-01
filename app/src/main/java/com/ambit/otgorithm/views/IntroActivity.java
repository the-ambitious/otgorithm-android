package com.ambit.otgorithm.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ambit.otgorithm.R;

public class IntroActivity extends AppCompatActivity {

    TextView privacy;
    TextView terms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        privacy = (TextView) findViewById(R.id.privacy);
        terms = (TextView) findViewById(R.id.terms);
    }   // end of create()

    public void onClick(View v) {
        Intent intent = null;

        switch (v.getId()) {
            case R.id.privacy:
                intent = new Intent(this, DescriptionActivity.class);
                intent.putExtra("description", "privacy");
                break;
            case R.id.terms:
                intent = new Intent(this, DescriptionActivity.class);
                intent.putExtra("description", "terms");
                break;
        }
        startActivity(intent);
    }
}
