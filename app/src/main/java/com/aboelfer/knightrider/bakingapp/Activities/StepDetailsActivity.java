package com.aboelfer.knightrider.bakingapp.Activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.aboelfer.knightrider.bakingapp.Fragments.StepDetailsFragment;
import com.aboelfer.knightrider.bakingapp.R;

import java.util.Objects;

public class StepDetailsActivity extends AppCompatActivity {

    private String stepId, shortDescription, fullDescription, videoUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent != null) {
            stepId = intent.getStringExtra(getString(R.string.STEP_ID_INTENT_KEY));
            shortDescription = intent.getStringExtra(getString(R.string.STEP_SHORT_DESCRIPTION_INTENT_KEY));
            fullDescription = intent.getStringExtra(getString(R.string.STEP_FULL_DESCRIPTION_INTENT_KEY));
            videoUrl = intent.getStringExtra(getString(R.string.STEP_VIDEO_URL_INTENT_KEY));

        }

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && !(Objects.equals(videoUrl, ""))) {

            //Remove title bar
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
            getSupportActionBar().hide();

            //Remove notification bar
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.activity_step_detail);

        }else {

            setContentView(R.layout.activity_step_detail);
        }

        if(savedInstanceState == null) {

            StepDetailsFragment stepsDetailFragment = new StepDetailsFragment();

            Bundle bundle = new Bundle();
            bundle.putString(getString(R.string.STEP_ID_BUNDLES_KEY), stepId);
            bundle.putString(getString(R.string.STEP_SHORT_DESCRIPTION_BUNDLES_KEY), shortDescription);
            bundle.putString(getString(R.string.STEP_FULL_DESCRIPTION_BUNDLES_KEY), fullDescription);
            bundle.putString(getString(R.string.STEP_VIDEO_URL_BUNDLES_KEY), videoUrl);
            stepsDetailFragment.setArguments(bundle);

            //Use a FragmentManager and transaction to add the fragment to the screen
            FragmentManager fragmentManager = getSupportFragmentManager();

            //Fragment transaction
            fragmentManager.beginTransaction()
                    .add(R.id.stepDetailContainer, stepsDetailFragment)
                    .commit();

            setTitle(shortDescription);
        }


    }

}
