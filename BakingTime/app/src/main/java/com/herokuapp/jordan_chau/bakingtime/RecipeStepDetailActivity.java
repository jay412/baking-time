package com.herokuapp.jordan_chau.bakingtime;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.herokuapp.jordan_chau.bakingtime.fragment.RecipeStepDetailFragment;
import com.herokuapp.jordan_chau.bakingtime.model.Ingredient;
import com.herokuapp.jordan_chau.bakingtime.model.Step;

import java.util.ArrayList;

public class RecipeStepDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_detail);

        //Creates the back arrow on the top left corner to return to MainActivity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        if (intent == null) {
            Log.d("RSDA: ", "intent error");
        }

        setTitle(intent.getStringExtra("recipe_name"));

        Bundle bundle = new Bundle();
        if(intent.getStringExtra("option").equals("ingredients")) {
            ArrayList<Ingredient> mIngredients = intent.getParcelableArrayListExtra("ingredients");

            bundle.putParcelableArrayList("ingredients", mIngredients);
        } else {
            ArrayList<Step> mSteps = intent.getParcelableArrayListExtra("steps");
            int position = intent.getIntExtra("position", 0);

            bundle.putParcelableArrayList("steps", mSteps);
            bundle.putInt("position", position);

        }

        if(savedInstanceState == null) {
            //send arguments to fragment
            RecipeStepDetailFragment detailFragment = new RecipeStepDetailFragment();
            detailFragment.setArguments(bundle);

            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction().add(R.id.recipe_step_detail_container, detailFragment).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

                default:
                    return super.onOptionsItemSelected(item);
        }
    }

}
