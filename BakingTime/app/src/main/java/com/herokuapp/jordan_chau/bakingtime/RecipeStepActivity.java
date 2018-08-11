package com.herokuapp.jordan_chau.bakingtime;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.herokuapp.jordan_chau.bakingtime.adapter.RecipeStepAdapter;
import com.herokuapp.jordan_chau.bakingtime.model.Ingredient;
import com.herokuapp.jordan_chau.bakingtime.model.Recipe;
import com.herokuapp.jordan_chau.bakingtime.model.Step;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Optional;

public class RecipeStepActivity extends AppCompatActivity implements RecipeStepFragment.OnStepClickListener{
    @Nullable @BindView(R.id.recipe_step_linear_layout) LinearLayout mLayout;
    @Nullable @BindView(R.id.btn_previous_step) Button mPrevious;
    @Nullable @BindView(R.id.btn_next_step) Button mNext;
    @Nullable @BindView(R.id.tv_long_description) TextView mDescription;
    private Recipe mRecipe;
    private ArrayList<Step> mSteps;
    private ArrayList<Ingredient> mIngredients;
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step);

        ButterKnife.bind(this);

        //Creates the back arrow on the top left corner to return to MainActivity, DELETE PARENT?
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if(findViewById(R.id.recipe_step_detail_fragment) != null) {
            mTwoPane = true;

            mPrevious.setVisibility(View.GONE);
            mNext.setVisibility(View.GONE);

            //create new recipe step detail fragment if no saved instance

        } else {
            mTwoPane = false;
        }

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        mRecipe = intent.getParcelableExtra("recipe");
        mRecipe.setSteps(intent.<Step>getParcelableArrayListExtra("step"));
        mRecipe.setIngredients(intent.<Ingredient>getParcelableArrayListExtra("ingredient"));

        //set title
        setTitle(mRecipe.getName());
        //get steps and ingredients
        mSteps = mRecipe.getSteps();
        mIngredients = mRecipe.getIngredients();
        //send arguments to fragment
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("steps", mSteps);
        bundle.putParcelableArrayList("ingredients", mIngredients);

        RecipeStepFragment stepFragment = new RecipeStepFragment();
        stepFragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction().add(R.id.recipe_step_container, stepFragment).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void closeOnError() {
        finish();
        Snackbar snackbar = Snackbar
                .make(mLayout, R.string.detail_error_message, Snackbar.LENGTH_LONG);

        snackbar.show();
    }

    @Override
    public void onInstructionSelected() {
        if(mTwoPane) {
            //set up ingredients text
            String ingredientsList = "";
            for(int x = 0; x < mIngredients.size(); ++x) {
                Ingredient currentIngredient = mIngredients.get(x);

                ingredientsList +=  x + 1 + ". " +
                                    currentIngredient.getQuantity() + " " +
                                    currentIngredient.getMeasure() + " " +
                                    currentIngredient.getIngredient() + "\n\n";
            }

            mDescription.setText(ingredientsList);
            //hide previous and next buttons
            mPrevious.setVisibility(View.INVISIBLE);
            mNext.setVisibility(View.INVISIBLE);
        } else {
            //open recipe step detail activity
            Intent i = new Intent(RecipeStepActivity.this, RecipeStepDetailActivity.class);
            i.putExtra("recipe_name", mRecipe.getName());
            i.putExtra("ingredients", mIngredients);
            startActivity(i);
        }
    }

    @Override
    public void onStepSelected(int position) {
        if(mTwoPane) {
            mDescription.setText(mSteps.get(position).getDescription());
        }
        else {
            Intent i = new Intent(RecipeStepActivity.this, RecipeStepDetailActivity.class);
            i.putExtra("steps", mSteps);
            i.putExtra("position", position);
            i.putExtra("recipe_name", mRecipe.getName());

            startActivity(i);
        }
    }
}
