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

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepFragment extends Fragment implements RecipeStepAdapter.RecipeStepClickListener{

    @BindView(R.id.rv_recipe_steps) RecyclerView mRecipeStepList;
    @BindView(R.id.btn_recipe_step_ingredients) Button mIngredients;

    private OnStepClickListener mCallback;
    private Button currentButton;

    public interface OnStepClickListener {
        void onStepSelected(int position);
        void onIngredientSelected();
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

    public RecipeStepFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_step, container, false);

        ButterKnife.bind(this, rootView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecipeStepList.setLayoutManager(layoutManager);
        mRecipeStepList.setHasFixedSize(true);

        Bundle b = getArguments();
        if(b == null) {
            Log.d("RSFrag: ", "bundle is null");
        }
        else {
            //step arraylist
            ArrayList<Step> mSteps = b.getParcelableArrayList("steps");

            mIngredients.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCallback.onIngredientSelected();
                    switchActiveButton(mIngredients);
                }
            });

            RecipeStepAdapter mAdapter = new RecipeStepAdapter(mSteps, this);
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

    public void switchActiveButtonByPos(int position) {
        //View view = mRecipeStepList.getChildAt(position);

        RecyclerView.ViewHolder holder = mRecipeStepList.findViewHolderForAdapterPosition(position);
        if(holder != null) {
            Button newButton = holder.itemView.findViewById(R.id.btn_short_description);
            switchActiveButton(newButton);
        }
    }
}
