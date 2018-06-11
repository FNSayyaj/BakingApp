package com.aboelfer.knightrider.bakingapp;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.aboelfer.knightrider.bakingapp.Activities.RecipesActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by KNIGHT RIDER on 6/12/2018.
 */

@RunWith(AndroidJUnit4.class)
public class RecipesDownloadTest {

    private static final String RECIPE_ITEM = "Yellow Cake";

    @Rule
    public ActivityTestRule<RecipesActivity> activityTestRule = new ActivityTestRule<>(RecipesActivity.class);
    private IdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource() {
        mIdlingResource = activityTestRule.getActivity().getIdlingResource();
        Espresso.registerIdlingResources(mIdlingResource);
    }

    @Test
    public void checkText_RecipesActivity() {

        if (!mIdlingResource.isIdleNow()){
            onView(ViewMatchers.withId(R.id.recipeFragmentRecyeclerview)).perform(RecyclerViewActions.scrollToPosition(3));
            onView(withText(RECIPE_ITEM)).check(matches(isDisplayed()));
        }

    }


    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            Espresso.unregisterIdlingResources(mIdlingResource);
        }
    }

}