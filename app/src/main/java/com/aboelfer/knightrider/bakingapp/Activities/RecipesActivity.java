package com.aboelfer.knightrider.bakingapp.Activities;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.aboelfer.knightrider.bakingapp.Fragments.RecipesFragment;
import com.aboelfer.knightrider.bakingapp.IdlingResources.SimpleIdlingResource;
import com.aboelfer.knightrider.bakingapp.R;

public class RecipesActivity extends AppCompatActivity {

    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        if(savedInstanceState == null) {

            RecipesFragment recipeFragment = new RecipesFragment();

            //Use a FragmentManager and transaction to add the fragment to the screen
            FragmentManager fragmentManager = getSupportFragmentManager();

            //Fragment transaction
            fragmentManager.beginTransaction()
                    .add(R.id.recipeContainer, recipeFragment)
                    .commit();
        }

        getIdlingResource();

    }

}