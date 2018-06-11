package com.aboelfer.knightrider.bakingapp.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import com.aboelfer.knightrider.bakingapp.Activities.RecipesActivity;
import com.aboelfer.knightrider.bakingapp.R;

/**
 * Created by KNIGHT RIDER on 6/11/2018.
 */

public class RecipeIngredientsWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        // Iterate over all widgets to update all instances
        for (int appWidgetId : appWidgetIds) {

            // Retrieve recipe name and ingredients from SharedPreferences
            SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.SHARED_PREFERENCE_KEY), Context.MODE_PRIVATE);

            //If no recipe is selected make widget display these values
            StringBuilder stringBuilder = new StringBuilder();

            String noRecipeSelectedName = context.getString(R.string.NO_RECIPE_NAME_AVAILABLE);
            stringBuilder.append(noRecipeSelectedName).append(" \uD83C\uDF82");

            String noRecipeSelectedIngredients = context.getString(R.string.NO_RECIPE_INGREDIENTS_AVAILABLE);

            String recipeName = sharedPref.getString(context.getString(R.string.SHARED_PREFERENCE_RECIPE_NAME_KEY), stringBuilder.toString());
            String ingredients = sharedPref.getString(context.getString(R.string.SHARED_PREFERENCE_INGREDIENTS_KEY), noRecipeSelectedIngredients);

            // Setting the recipe name and ingredients TextViews
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredient_widget);

            views.setTextViewText(R.id.widgetRecipeTextView, recipeName);
            views.setTextViewText(R.id.widgetIngredientTextView, ingredients);

            // When recipe name is clicked, RecipesActivity brought up
            Intent widgetIntent = new Intent(context, RecipesActivity.class);

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, widgetIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            views.setOnClickPendingIntent(R.id.widgetRecipeTextView, pendingIntent);

            // Update the widgets
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }

    }
}
