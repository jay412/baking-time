package com.herokuapp.jordan_chau.bakingtime;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.herokuapp.jordan_chau.bakingtime.fragment.RecipeStepDetailFragment;
import com.herokuapp.jordan_chau.bakingtime.fragment.RecipeStepFragment;
import com.herokuapp.jordan_chau.bakingtime.model.Ingredient;
import com.herokuapp.jordan_chau.bakingtime.model.Recipe;
import com.herokuapp.jordan_chau.bakingtime.model.Step;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

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

        //check if two pane layout
        if(findViewById(R.id.recipe_step_detail_container) != null) {
            mTwoPane = true;

            //create new recipe step detail fragment if no saved instance
            if(savedInstanceState == null) {
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("ingredients", mIngredients);

                RecipeStepDetailFragment detailFragment = new RecipeStepDetailFragment();
                detailFragment.setArguments(bundle);
                detailFragment.hideButtons();

                FragmentManager fragmentManager = getSupportFragmentManager();

                fragmentManager.beginTransaction().add(R.id.recipe_step_detail_container, detailFragment).commit();
            }
        } else {
            mTwoPane = false;
        }

        //create new recipe step fragment if no saved instance
        if(savedInstanceState == null) {

            //get information from last activity first
            Intent intent = getIntent();
            if (intent == null) {
                closeOnError();
            }

            //store in recipe object
            mRecipe = intent.getParcelableExtra("recipe");
            mRecipe.setSteps(intent.<Step>getParcelableArrayListExtra("step"));
            mRecipe.setIngredients(intent.<Ingredient>getParcelableArrayListExtra("ingredient"));

            //get steps and ingredients
            mSteps = mRecipe.getSteps();
            mIngredients = mRecipe.getIngredients();

            //set title
            setTitle(mRecipe.getName());

            //send arguments to fragment
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("steps", mSteps);

            RecipeStepFragment stepFragment = new RecipeStepFragment();
            stepFragment.setArguments(bundle);

            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction().add(R.id.recipe_step_container, stepFragment).commit();
        }
        else {
            setTitle(savedInstanceState.getString("name"));
            mRecipe = savedInstanceState.getParcelable("recipe");
            mSteps = savedInstanceState.getParcelableArrayList("steps");
            mIngredients = savedInstanceState.getParcelableArrayList("ingredients");

            mRecipe.setSteps(mSteps);
            mRecipe.setIngredients(mIngredients);
        }
    }

    //handle back button
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

    //show error message
    private void closeOnError() {
        finish();
        Snackbar snackbar = Snackbar
                .make(mLayout, R.string.detail_error_message, Snackbar.LENGTH_LONG);

        snackbar.show();
    }

    //if ingredient button is selected
    @Override
    public void onIngredientSelected() {
        if(mTwoPane) {
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("ingredients", mIngredients);

            RecipeStepDetailFragment newFragment = new RecipeStepDetailFragment();
            newFragment.setArguments(bundle);
            newFragment.hideButtons();
            getSupportFragmentManager().beginTransaction().replace(R.id.recipe_step_detail_container, newFragment).commit();
        } else {
            //open recipe step detail activity
            Intent i = new Intent(RecipeStepActivity.this, RecipeStepDetailActivity.class);
            i.putExtra("option", "ingredients");
            i.putExtra("recipe_name", mRecipe.getName());
            i.putExtra("ingredients", mIngredients);
            startActivity(i);
        }
    }

    //if step button is clicked
    @Override
    public void onStepSelected(int position) {
        if(mTwoPane) {
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("steps", mSteps);
            bundle.putInt("position", position);

            RecipeStepDetailFragment newFragment = new RecipeStepDetailFragment();
            newFragment.setArguments(bundle);
            newFragment.hideButtons();
            getSupportFragmentManager().beginTransaction().replace(R.id.recipe_step_detail_container, newFragment).commit();
        }
        else {
            Intent i = new Intent(RecipeStepActivity.this, RecipeStepDetailActivity.class);
            i.putExtra("option", "steps");
            i.putExtra("steps", mSteps);
            i.putExtra("position", position);
            i.putExtra("recipe_name", mRecipe.getName());

            startActivity(i);
        }
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1) {
            int position = data.getIntExtra("position", 0);
            //Log.i("RSA: ", "position = " + position);
            stepFragment.switchActiveButtonByPos(position);
        }
    } */

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("name", mRecipe.getName());
        outState.putParcelable("recipe", mRecipe);
        outState.putParcelableArrayList("steps", mRecipe.getSteps());
        outState.putParcelableArrayList("ingredients", mRecipe.getIngredients());
    }
}
