package com.herokuapp.jordan_chau.bakingtime;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.herokuapp.jordan_chau.bakingtime.adapter.RecipeStepAdapter;
import com.herokuapp.jordan_chau.bakingtime.model.Ingredient;
import com.herokuapp.jordan_chau.bakingtime.model.Recipe;
import com.herokuapp.jordan_chau.bakingtime.model.Step;

import java.util.ArrayList;

public class RecipeStepActivity extends AppCompatActivity implements RecipeStepAdapter.RecipeStepClickListener{
    private LinearLayout mLayout;
    private Recipe mRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step);

        //Creates the back arrow on the top left corner to return to MainActivity, DELETE PARENT?
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mLayout = findViewById(R.id.recipe_step_linear_layout);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        mRecipe = intent.getParcelableExtra("recipe");
        mRecipe.setSteps(intent.<Step>getParcelableArrayListExtra("step"));
        mRecipe.setIngredients(intent.<Ingredient>getParcelableArrayListExtra("ingredient"));

        //set title
        setTitle(mRecipe.getName());
        //get steps
        ArrayList<Step> steps = mRecipe.getSteps();
        //send arguments to fragment
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("steps", steps);

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
    public void onRecipeStepClicked(int clickedItemIndex) {
        //TODO
    }
}
