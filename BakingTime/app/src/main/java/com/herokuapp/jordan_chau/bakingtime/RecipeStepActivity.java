package com.herokuapp.jordan_chau.bakingtime;

import android.content.Intent;
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

public class RecipeStepActivity extends AppCompatActivity implements RecipeStepFragment.OnStepClickListener{
    private LinearLayout mLayout;
    private Recipe mRecipe;
    private ArrayList<Step> mSteps;
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step);

        //Creates the back arrow on the top left corner to return to MainActivity, DELETE PARENT?
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if(findViewById(R.id.recipe_step_detail_fragment) != null) {
            mTwoPane = true;

            Button prevButton = findViewById(R.id.btn_previous_step);
            Button nextButton = findViewById(R.id.btn_next_step);
            prevButton.setVisibility(View.GONE);
            nextButton.setVisibility(View.GONE);

            //create new recipe step detail fragment if no saved instance

        } else {
            mTwoPane = false;
        }

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
        mSteps = mRecipe.getSteps();
        //send arguments to fragment
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("steps", mSteps);

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
    public void onStepSelected(int position) {
        if(mTwoPane) {
            TextView mDescription = findViewById(R.id.tv_long_description);

            mDescription.setText(mSteps.get(position).getDescription());
        }
        else {
            Intent i = new Intent(RecipeStepActivity.this, RecipeStepDetailActivity.class);
            i.putExtra("steps", mSteps);
            i.putExtra("position", position);

            //Log.d("RSF: ", "video url = " + mSteps.get(position).getVideoURL());
            //Log.d("RSF: ", "description = " + mSteps.get(position).getDescription());

            startActivity(i);
        }
    }
}
