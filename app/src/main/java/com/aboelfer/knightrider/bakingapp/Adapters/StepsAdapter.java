package com.aboelfer.knightrider.bakingapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.aboelfer.knightrider.bakingapp.Models.Steps;

import com.aboelfer.knightrider.bakingapp.R;

import java.util.List;

/**
 * Created by KNIGHT RIDER on 6/11/2018.
 */

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.ViewHolder>  {

private final List<Steps> Steps;
private final Context context;
private final StepsAdapter.ListStepClickListener mOnClickListener;

public interface ListStepClickListener {

    void onListItemClick(int clickedItemIndex);

}

    public StepsAdapter(List<Steps> Steps, Context context, StepsAdapter.ListStepClickListener listener ){

        this.Steps = Steps;
        this.context = context;
        this.mOnClickListener = listener;

    }

    @NonNull
    @Override
    public StepsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycled_steps_item, parent, false);

        return new StepsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Steps step = Steps.get(position);
        holder.stepShortDescription.setText(step.getShortDescription());

    }

    @Override
    public int getItemCount() {
        return Steps.size();
    }

public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    final TextView stepShortDescription;
    final RelativeLayout relativeLayout;

    ViewHolder(View itemView) {
        super(itemView);
        stepShortDescription = itemView.findViewById(R.id.stepShortDescriptionEv);
        relativeLayout = itemView.findViewById(R.id.stepRelativeLayout);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        int clickedPosition = getAdapterPosition();
        mOnClickListener.onListItemClick(clickedPosition);
    }
}
}