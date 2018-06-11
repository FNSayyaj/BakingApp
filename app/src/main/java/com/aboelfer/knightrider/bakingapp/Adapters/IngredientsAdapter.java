package com.aboelfer.knightrider.bakingapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aboelfer.knightrider.bakingapp.Models.Ingredients;
import com.aboelfer.knightrider.bakingapp.R;

import java.util.List;

/**
 * Created by KNIGHT RIDER on 6/11/2018.
 */

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ViewHolder> {

    private final List<Ingredients> ingredients;
    private final Context context;

    public IngredientsAdapter(List<Ingredients> ingredients, Context context){

        this.ingredients = ingredients;
        this.context = context;
    }

    @NonNull
    @Override
    public IngredientsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycled_ingredient_item, parent, false);

        return new IngredientsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Ingredients ingredient = ingredients.get(position);
        StringBuilder stringBuilder = new StringBuilder();

        holder.ingredient_name.setText(ingredient.getIngredient());
        holder.quantity.setText(ingredient.getQuantity());

        String measure = ingredient.getMeasure();
        holder.measure.setText(stringBuilder.append(" ").append(measure));
    }


    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        final TextView ingredient_name;
        final TextView quantity;
        final TextView measure;
        final RelativeLayout relativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            ingredient_name = itemView.findViewById(R.id.ingredientNameEv);
            quantity = itemView.findViewById(R.id.quantityEv);
            measure = itemView.findViewById(R.id.measureEv);
            relativeLayout = itemView.findViewById(R.id.ingredientRelativeLayout);
        }


    }
}
