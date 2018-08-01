package com.herokuapp.jordan_chau.bakingtime.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.herokuapp.jordan_chau.bakingtime.R;
import com.herokuapp.jordan_chau.bakingtime.model.Recipe;

import java.util.ArrayList;

public class RecipeCardAdapter extends RecyclerView.Adapter<RecipeCardAdapter.CardViewHolder>{
    private static final String TAG = RecipeCardAdapter.class.getSimpleName();

    private int mNumberItems;
    private ArrayList<Recipe> mRecipes;
    final private RecipeItemClickListener mOnClickListener;

    public RecipeCardAdapter(ArrayList<Recipe> recipes, RecipeItemClickListener listener) {
        mRecipes = recipes;
        mNumberItems = recipes.size();
        mOnClickListener = listener;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int layoutForRecipeItem = R.layout.fragment_recipe_card;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutForRecipeItem, parent, shouldAttachToParentImmediately);
        CardViewHolder viewHolder = new CardViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        String currentRecipeName = mRecipes.get(position).getName();
        int currentRecipeServings = mRecipes.get(position).getServings();

        holder.bind(currentRecipeName, Integer.toString(currentRecipeServings));
    }

    @Override
    public int getItemCount() {
        return mNumberItems;
    }


    class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView recipeNameView, servingsView;

        public CardViewHolder(View itemView) {
            super(itemView);

            recipeNameView = itemView.findViewById(R.id.tv_recipe_name);
            servingsView = itemView.findViewById(R.id.tv_servings);
            itemView.setOnClickListener(this);
        }

        void bind(String recipeName, String servings) {
            recipeNameView.setText(recipeName);
            servingsView.setText("Servings: " + servings);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onRecipeItemClicked(clickedPosition);
        }
    }

    public interface RecipeItemClickListener {
        void onRecipeItemClicked(int clickedItemIndex);
    }
}
