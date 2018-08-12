package com.herokuapp.jordan_chau.bakingtime.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.herokuapp.jordan_chau.bakingtime.R;
import com.herokuapp.jordan_chau.bakingtime.model.Step;

import java.util.ArrayList;

public class RecipeStepAdapter extends RecyclerView.Adapter<RecipeStepAdapter.StepViewHolder>{

    private ArrayList<Step> mSteps;
    private int mNumberItems;
    final private RecipeStepClickListener mOnClickListener;

    public RecipeStepAdapter(ArrayList<Step> steps, RecipeStepClickListener listener) {
        mSteps = steps;
        mNumberItems = steps.size();
        mOnClickListener = listener;
    }

    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int layoutForRecipeItem = R.layout.recipe_step_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutForRecipeItem, parent, shouldAttachToParentImmediately);
        StepViewHolder viewHolder = new StepViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeStepAdapter.StepViewHolder holder, int position) {
        String currentShortDescription;

        if(position != 0)
            currentShortDescription = (position) + ". " + mSteps.get(position).getShortDescription();
        else
            currentShortDescription = mSteps.get(position).getShortDescription();

        holder.bind(currentShortDescription);
    }

    @Override
    public int getItemCount() {
        return mNumberItems;
    }

    class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        Button shortDescriptionBtn;

        public StepViewHolder(View itemView) {
            super(itemView);

            shortDescriptionBtn = itemView.findViewById(R.id.btn_short_description);
            shortDescriptionBtn.setOnClickListener(this);
        }

        void bind(String shortDescription) {
            shortDescriptionBtn.setText(shortDescription);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onRecipeStepClicked(clickedPosition, shortDescriptionBtn);
        }
    }

    public interface RecipeStepClickListener {
        void onRecipeStepClicked(int clickedItemIndex, Button stepButton);
    }
}
