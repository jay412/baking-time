package com.herokuapp.jordan_chau.bakingtime.fragment;

import android.content.Context;
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

import com.herokuapp.jordan_chau.bakingtime.R;
import com.herokuapp.jordan_chau.bakingtime.adapter.RecipeStepAdapter;
import com.herokuapp.jordan_chau.bakingtime.model.Step;

import java.util.ArrayList;

public class RecipeStepFragment extends Fragment implements RecipeStepAdapter.RecipeStepClickListener{
    
    private RecyclerView mRecipeStepList;
    private RecipeStepAdapter mAdapter;
    OnStepClickListener mCallback;
    private Button currentButton;

    public interface OnStepClickListener {
        void onStepSelected(int position);
        void onInstructionSelected();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (OnStepClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnStepClickListener");
        }
    }

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
            //TODO display error message
            Log.d("RSFrag: ", "bundle is null");
        }
        else {
            //step arraylist
            mSteps = b.getParcelableArrayList("steps");

            final Button mIngredients = rootView.findViewById(R.id.btn_recipe_step_ingredients);
            mIngredients.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCallback.onInstructionSelected();
                    switchActiveButton(mIngredients);
                }
            });

        mAdapter = new RecipeStepAdapter(mSteps, this);
        mRecipeStepList.setAdapter(mAdapter);
        //some issues with recipes w/ 10+ steps
        currentButton = mIngredients;
        }

        return rootView;
    }

    @Override
    public void onRecipeStepClicked(int clickedItemIndex, Button stepButton) {
        switchActiveButton(stepButton);
        //communicate with recipe step activity
        mCallback.onStepSelected(clickedItemIndex);
    }

    private void switchActiveButton(Button newActive){
        if(currentButton != null) {
            currentButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            currentButton.setTextColor(getResources().getColor(R.color.colorAccent));
        }
        newActive.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        newActive.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        currentButton = newActive;
    }
}
