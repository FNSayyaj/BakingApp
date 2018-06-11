package com.aboelfer.knightrider.bakingapp.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aboelfer.knightrider.bakingapp.Activities.IngredientsStepsActivity;
import com.aboelfer.knightrider.bakingapp.Activities.RecipesActivity;
import com.aboelfer.knightrider.bakingapp.Adapters.RecipesAdapter;
import com.aboelfer.knightrider.bakingapp.IdlingResources.SimpleIdlingResource;
import com.aboelfer.knightrider.bakingapp.Models.Recipes;
import com.aboelfer.knightrider.bakingapp.R;
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


public class RecipesFragment extends Fragment implements RecipesAdapter.ListRecipeClickListener{


    private RecipesAdapter adapter;
    private List<Recipes> recipeList;
    private final String URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    SimpleIdlingResource idlingResource;

    @BindView(R.id.recipeFragmentRecyeclerview)
    RecyclerView recipesRecyclerView;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    public RecipesFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_recipes, container, false);

        ButterKnife.bind(this, rootView);

        progressBar.setVisibility(View.VISIBLE);
        idlingResource = (SimpleIdlingResource) ((RecipesActivity) getActivity()).getIdlingResource();

        if (idlingResource != null) {
            idlingResource.setIdleState(false);
        }

        if (rootView.findViewById(R.id.largeScreen) == null) {

            LinearLayoutManager layoutManager;
            layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            recipesRecyclerView.setLayoutManager(layoutManager);

            recipesRecyclerView.setHasFixedSize(true);
            recipeList = new ArrayList<>();

            loadRecipesData(URL);

            return rootView;

        } else {

            GridLayoutManager layoutManager;
            layoutManager = new GridLayoutManager(getContext(), 3, LinearLayoutManager.VERTICAL, false);
            recipesRecyclerView.setLayoutManager(layoutManager);

            recipesRecyclerView.setHasFixedSize(true);
            recipeList = new ArrayList<>();

            loadRecipesData(URL);

            return rootView;
        }
    }

    private void loadRecipesData(String url) {

        StringRequest stringRequest = new StringRequest(GET, url, response -> {

            try {
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jo = jsonArray.optJSONObject(i);
                    Recipes recipe = new Recipes(
                            jo.optString(getString(R.string.RECIPE_ID_KEY)),
                            jo.optString(getString(R.string.RECIPE_NAME_KEY)),
                            jo.getString(getString(R.string.RECIPE_INGREDIENTS_KEY)),
                            jo.optString(getString(R.string.RECIPE_STEPS_KEY)),
                            jo.optString(getString(R.string.RECIPE_SERVINGS_KEY)),
                            jo.optString(getString(R.string.RECIPE_IMAGES_KEY)));
                    recipeList.add(recipe);
                }
                adapter = new RecipesAdapter(recipeList, getContext(), this);
                recipesRecyclerView.setAdapter(adapter);
                idlingResource.setIdleState(true);
                progressBar.setVisibility(View.INVISIBLE);

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

    @Override
    public void onListItemClick(int clickedItemIndex) {

        Recipes recipe = recipeList.get(clickedItemIndex);
        Intent intent = new Intent(getContext(), IngredientsStepsActivity.class);
        intent.putExtra(getString(R.string.RECIPE_ID_INTENT_KEY), recipe.getId());
        intent.putExtra(getString(R.string.RECIPE_NAME_INTENT_KEY), recipe.getName());
        getContext().startActivity(intent);
    }
}
