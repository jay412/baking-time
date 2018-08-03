package com.herokuapp.jordan_chau.bakingtime;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.herokuapp.jordan_chau.bakingtime.adapter.RecipeCardAdapter;
import com.herokuapp.jordan_chau.bakingtime.adapter.RecipeStepAdapter;
import com.herokuapp.jordan_chau.bakingtime.model.Step;

import java.util.ArrayList;

public class RecipeStepFragment extends Fragment implements RecipeStepAdapter.RecipeStepClickListener{

    private RecyclerView mRecipeStepList;
    private RecipeStepAdapter mAdapter;

    private ArrayList<Step> mSteps;
    public RecipeStepFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_step, container, false);

        //recycler view for recipe step
        mRecipeStepList = rootView.findViewById(R.id.rv_recipe_steps);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecipeStepList.setLayoutManager(layoutManager);
        mRecipeStepList.setHasFixedSize(true);

        Bundle b = getArguments();
        if(b == null) {
            Log.d("RSFrag: ", "bundle is null");
        }
        else {
            //step arraylist
            mSteps = b.getParcelableArrayList("steps");

            Button ingredients = rootView.findViewById(R.id.recipe_step_ingredients);
            //TODO: add click listener to ingredient button

            if (mSteps == null) {
                Log.d("RSFrag: ", "mSteps is null");
            }
        }

        mAdapter = new RecipeStepAdapter(mSteps, this);
        mRecipeStepList.setAdapter(mAdapter);

        return rootView;
    }

    @Override
    public void onRecipeStepClicked(int clickedItemIndex) {
        //TODO
        Toast.makeText(getContext(), "test", Toast.LENGTH_SHORT).show();
    }
}
