package com.aboelfer.knightrider.bakingapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aboelfer.knightrider.bakingapp.Models.Recipes;
import com.aboelfer.knightrider.bakingapp.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by KNIGHT RIDER on 6/11/2018.
 */

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.ViewHolder>  {

    private final List<Recipes> recipes;
    private final Context context;
    private final ListRecipeClickListener mOnClickListener;

    public interface ListRecipeClickListener {

        void onListItemClick(int clickedItemIndex);

    }

    public RecipesAdapter(List<Recipes> recipes, Context context, ListRecipeClickListener listener ){

        this.recipes = recipes;
        this.context = context;
        this.mOnClickListener = listener;

    }

    @NonNull
    @Override
    public RecipesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycled_recipes_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipesAdapter.ViewHolder holder, int position) {

        holder.bind(position);

    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        final TextView recipe_name;
        final RelativeLayout relativeLayout;
        final ImageView recipe_image;

        public ViewHolder(View itemView) {
            super(itemView);
            recipe_name = itemView.findViewById(R.id.recipeName);
            relativeLayout = itemView.findViewById(R.id.recipeRelativeLayout);
            recipe_image = itemView.findViewById(R.id.recipeImage);
            itemView.setOnClickListener(this);
        }

        void bind(int position) {

            final Recipes recipe = recipes.get(position);

            if (TextUtils.isEmpty(recipe.getImage())){
                recipe_name.setText(recipe.getName());

            } else {

                Picasso.with(context).load(recipe.getImage()).into(recipe_image,
                        new Callback.EmptyCallback() {

                            @Override
                            public void onError() {

                                recipe_image.setImageResource(R.drawable.no_image);
                            }
                        });
            }
        }

        @Override
        public void onClick(View v) {

            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }
}