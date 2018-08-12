package com.herokuapp.jordan_chau.bakingtime;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.herokuapp.jordan_chau.bakingtime.model.Ingredient;
import com.herokuapp.jordan_chau.bakingtime.model.Step;

import java.lang.reflect.Array;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_detail);

        //Creates the back arrow on the top left corner to return to MainActivity, DELETE PARENT?
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

            //bundle.putString("option", "ingredients");
            bundle.putParcelableArrayList("ingredients", mIngredients);
        } else {
            ArrayList<Step> mSteps = intent.getParcelableArrayListExtra("steps");
            int position = intent.getIntExtra("position", 0);

            //bundle.putString("option", "steps");
            bundle.putParcelableArrayList("steps", mSteps);
            bundle.putInt("position", position);
        }

        //send arguments to fragment
        RecipeStepDetailFragment detailFragment = new RecipeStepDetailFragment();
        detailFragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction().add(R.id.recipe_step_detail_container, detailFragment).commit();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //if(mExoPlayer != null)
            //releasePlayer();
    }
}
