package com.aboelfer.knightrider.bakingapp.Activities;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.aboelfer.knightrider.bakingapp.Fragments.IngredientsStepsFragment;
import com.aboelfer.knightrider.bakingapp.Fragments.StepDetailsFragment;
import com.aboelfer.knightrider.bakingapp.R;

public class IngredientsStepsActivity extends AppCompatActivity {

    private String ID;
    private int id;
    private String recipeName;
    private boolean mTwoPane;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients_steps);

        // Determine if we are creating a two-pane or single-pane display
        if(findViewById(R.id.ingredientsStepsLinearLayout) == null) {
            // This LinearLayout will only initially exist in the handset case

            mTwoPane = false;

            Intent intent = getIntent();

            if (intent != null) {

                ID = intent.getStringExtra(getString(R.string.RECIPE_ID_INTENT_KEY));
                recipeName = intent.getStringExtra(getString(R.string.RECIPE_NAME_INTENT_KEY));
            }

            setTitle(recipeName);

            id = Integer.valueOf(ID);

            IngredientsStepsFragment ingredientsStepsFragment = new IngredientsStepsFragment();

            Bundle bundle = new Bundle();
            bundle.putInt(getString(R.string.RECIPE_ID_BUNDLES_KEY), id);
            bundle.putString(getString(R.string.RECIPE_NAME_BUNDLES_KEY), recipeName);
            bundle.putBoolean(getString(R.string.TWO_PANE_SITUATION), mTwoPane);
            ingredientsStepsFragment.setArguments(bundle);

            if (savedInstanceState == null) {
                //Use a FragmentManager and transaction to add the fragment to the screen
                FragmentManager fragmentManager = getSupportFragmentManager();

                //Fragment transaction
                fragmentManager.beginTransaction()
                        .add(R.id.ingredientsStepsContainer, ingredientsStepsFragment)
                        .commit();
            }

        } else {
            // This LinearLayout will only initially exist in the tablet case

            mTwoPane = true;

            IngredientsStepsFragment ingredientsStepsFragment = new IngredientsStepsFragment();
            StepDetailsFragment stepsDetailFragment = new StepDetailsFragment();

            if (savedInstanceState == null) {

                Intent intent = getIntent();
                if (intent != null) {
                    ID = intent.getStringExtra(getString(R.string.RECIPE_ID_INTENT_KEY));
                    recipeName = intent.getStringExtra(getString(R.string.RECIPE_NAME_INTENT_KEY));
                }

                setTitle(recipeName);

                id = Integer.valueOf(ID);

                Bundle bundle = new Bundle();
                bundle.putInt(getString(R.string.RECIPE_ID_BUNDLES_KEY), id);
                bundle.putString(getString(R.string.RECIPE_NAME_BUNDLES_KEY), recipeName);
                bundle.putBoolean(getString(R.string.TWO_PANE_SITUATION), mTwoPane);
                ingredientsStepsFragment.setArguments(bundle);

                //Use a FragmentManager and transaction to add the fragment to the screen
                FragmentManager fragmentManager = getSupportFragmentManager();

                //Fragment transaction
                fragmentManager.beginTransaction()
                        .add(R.id.ingredientsStepsContainer, ingredientsStepsFragment)
                        .commit();

                fragmentManager.beginTransaction()
                        .add(R.id.stepDetailContainer, stepsDetailFragment)
                        .commit();
            }

        }
    }

}
