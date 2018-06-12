package com.aboelfer.knightrider.bakingapp.Fragments;


import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.aboelfer.knightrider.bakingapp.Activities.StepDetailsActivity;
import com.aboelfer.knightrider.bakingapp.Adapters.IngredientsAdapter;
import com.aboelfer.knightrider.bakingapp.Adapters.StepsAdapter;
import com.aboelfer.knightrider.bakingapp.Models.Ingredients;
import com.aboelfer.knightrider.bakingapp.Models.Steps;
import com.aboelfer.knightrider.bakingapp.R;
import com.aboelfer.knightrider.bakingapp.Widget.RecipeIngredientsWidgetProvider;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.android.volley.Request.Method.GET;

public class IngredientsStepsFragment extends Fragment implements StepsAdapter.ListStepClickListener{

    private IngredientsAdapter ingredientsAdapter;
    private StepsAdapter stepsAdapter;
    private List<Ingredients> ingredientList;
    private List<Steps> stepList;
    private final String URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    private int id;
    boolean mTwoPane;
    private String recipeName;
    LinearLayoutManager layoutIngredientsManager;
    LinearLayoutManager layoutStepsManager;
    private Parcelable mListStateI;
    private Parcelable mListStateS;
    public static final String I_RECYCLER_VIEW_STATE = "recycler1ViewScroll";
    public static final String S_RECYCLER_VIEW_STATE = "recycler1ViewScroll";


    @BindView(R.id.ingredientRecyclerview)
    RecyclerView ingredientsRecyclerView;

    @BindView(R.id.stepRecyclerview)
    RecyclerView stepsRecyclerView;

    public IngredientsStepsFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



        View rootView = inflater.inflate(R.layout.fragment_ingredients_steps, container, false);

        ButterKnife.bind(this, rootView);

        setHasOptionsMenu(true);


        layoutIngredientsManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        ingredientsRecyclerView.setLayoutManager(layoutIngredientsManager);
        if (savedInstanceState != null) {
            mListStateI = savedInstanceState.getParcelable(I_RECYCLER_VIEW_STATE);
        }
        ingredientsRecyclerView.setHasFixedSize(true);
        ingredientList = new ArrayList<>();


        layoutStepsManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        stepsRecyclerView.setLayoutManager(layoutStepsManager);
        if (savedInstanceState != null) {
            mListStateS = savedInstanceState.getParcelable(S_RECYCLER_VIEW_STATE);
        }
        stepsRecyclerView.setHasFixedSize(true);
        stepList = new ArrayList<>();


        Bundle bundle = getArguments();
        if (bundle != null) {
            id = bundle.getInt(getString(R.string.RECIPE_ID_BUNDLES_KEY));
            recipeName = bundle.getString(getString(R.string.RECIPE_NAME_BUNDLES_KEY));
            mTwoPane = bundle.getBoolean(getString(R.string.TWO_PANE_SITUATION));
        }

        loadIngredientsData(URL, id);
        loadStepsData(URL, id);

        return rootView;
    }

    private void loadIngredientsData(String url, int id) {

        StringRequest stringRequest = new StringRequest(GET, url, response -> {

            try {
                JSONArray jsonArray = new JSONArray(response);
                JSONObject jsonObject = jsonArray.optJSONObject(id - 1);
                JSONArray array = jsonObject.optJSONArray(getString(R.string.INGREDIENTS_KEY));
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jo = array.optJSONObject(i);
                    Ingredients ingredients = new Ingredients(
                            jo.optString(getString(R.string.INGREDIENT_QUANTITY_KEY)),
                            jo.optString(getString(R.string.INGREDIENT_MEASURE_KEY)),
                            jo.optString(getString(R.string.INGREDIENT_NAME_KEY)));
                    ingredientList.add(ingredients);
                }
                ingredientsAdapter = new IngredientsAdapter(ingredientList, getContext());
                ingredientsRecyclerView.setAdapter(ingredientsAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(getContext(),
                getContext().getText(R.string.no_connectivity_error),
                Toast.LENGTH_LONG).show()
        );
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

    }

    private void loadStepsData(String url, int id) {

        StringRequest stringRequest = new StringRequest(GET, url, response -> {

            try {
                JSONArray jsonArray = new JSONArray(response);
                JSONObject jsonObject = jsonArray.optJSONObject(id - 1);
                JSONArray array = jsonObject.optJSONArray(getString(R.string.STEPS_KEY));
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jo = array.optJSONObject(i);
                    Steps steps = new Steps(
                            jo.optString(getString(R.string.STEPS_ID_KEY)),
                            jo.optString(getString(R.string.STEPS_SHORT_DESCRIPTION_KEY)),
                            jo.optString(getString(R.string.STEPS_FULL_DESCRIPTION_KEY)),
                            jo.optString(getString(R.string.STEPS_VIDEO_URL_KEY)),
                            jo.optString(getString(R.string.STEPS_THUMBNAIL_URL_KEY)));
                    stepList.add(steps);
                }
                stepsAdapter = new StepsAdapter(stepList, getContext(), this);
                stepsRecyclerView.setAdapter(stepsAdapter);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(getContext(),
                getContext().getText(R.string.no_connectivity_error),
                Toast.LENGTH_LONG).show()
        );
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);


    }

    private void updateWidget(String recipeName, String recipeIngredients) {

        Activity activity = getActivity();
        SharedPreferences sharedPref = activity.getSharedPreferences(
                activity.getString(R.string.SHARED_PREFERENCE_KEY), Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.SHARED_PREFERENCE_RECIPE_NAME_KEY), recipeName);
        editor.putString(getString(R.string.SHARED_PREFERENCE_INGREDIENTS_KEY), recipeIngredients);

        editor.apply();

        Intent intent = new Intent(activity, RecipeIngredientsWidgetProvider.class);

        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");

        int appWidgetIds[] = AppWidgetManager.getInstance(activity.getApplication())
                .getAppWidgetIds(new ComponentName(activity.getApplication(), RecipeIngredientsWidgetProvider.class));

        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

        activity.sendBroadcast(intent);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {

        if(!mTwoPane) {

            Steps step = stepList.get(clickedItemIndex);

            Intent intent = new Intent(getContext(), StepDetailsActivity.class);
            intent.putExtra(getString(R.string.STEP_ID_INTENT_KEY), step.getId());
            intent.putExtra(getString(R.string.STEP_SHORT_DESCRIPTION_INTENT_KEY), step.getShortDescription());
            intent.putExtra(getString(R.string.STEP_FULL_DESCRIPTION_INTENT_KEY), step.getDescription());
            intent.putExtra(getString(R.string.STEP_THUMBNAIL_URL_INTENT_KEY), step.getThumbnailURL());
            intent.putExtra(getString(R.string.STEP_VIDEO_URL_INTENT_KEY), step.getVideoURLs());

            getContext().startActivity(intent);
        }
        else {

            Steps step = stepList.get(clickedItemIndex);

            Bundle bundle = new Bundle();
            bundle.putString(getString(R.string.STEP_ID_BUNDLES_KEY), step.getId());
            bundle.putString(getString(R.string.STEP_SHORT_DESCRIPTION_BUNDLES_KEY), step.getShortDescription());
            bundle.putString(getString(R.string.STEP_FULL_DESCRIPTION_BUNDLES_KEY), step.getDescription());
            bundle.putString(getString(R.string.STEP_VIDEO_URL_BUNDLES_KEY), step.getVideoURLs());
            bundle.putString(getString(R.string.STEP_THUMBNAIL_URL_BUNDLES_KEY), step.getThumbnailURL());
            bundle.putBoolean(getString(R.string.TWO_PANE_SITUATION), mTwoPane);

            StepDetailsFragment stepsDetailFragment = new StepDetailsFragment();
            stepsDetailFragment.setArguments(bundle);

            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.stepDetailContainer, stepsDetailFragment).commit();

        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mListStateI != null) {
            //Restoring recycler view state
            layoutIngredientsManager.onRestoreInstanceState(mListStateI);
            layoutStepsManager.onRestoreInstanceState(mListStateS);
        }

    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        //storing recycler view state
        outState.putParcelable(I_RECYCLER_VIEW_STATE, layoutStepsManager.onSaveInstanceState());
        outState.putParcelable(S_RECYCLER_VIEW_STATE, layoutIngredientsManager.onSaveInstanceState());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        getActivity().getMenuInflater().inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        switch (itemId) {

            case R.id.action_refresh_widget:
                StringBuilder stringBuilder = new StringBuilder();

                for (int i = 0; i < ingredientList.size(); i++) {

                    String ingredientName = ingredientList.get(i).getIngredient();
                    stringBuilder.append(i + 1).append(" - ").append(ingredientName).append(".").append("\n");
                }

                updateWidget(recipeName, stringBuilder.toString());
                Toast.makeText(getContext(), getString(R.string.toast_confirm_adding_recipe_to_widget), Toast.LENGTH_SHORT).show();

                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

