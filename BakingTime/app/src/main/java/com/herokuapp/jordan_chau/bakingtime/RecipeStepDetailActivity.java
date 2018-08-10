package com.herokuapp.jordan_chau.bakingtime;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.herokuapp.jordan_chau.bakingtime.model.Ingredient;
import com.herokuapp.jordan_chau.bakingtime.model.Step;

import java.util.ArrayList;

public class RecipeStepDetailActivity extends AppCompatActivity {
    private TextView mDescription;
    private Button mPrevious, mNext;
    private ArrayList<Step> mSteps;
    private ArrayList<Ingredient> mIngredients;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_detail);

        //Creates the back arrow on the top left corner to return to MainActivity, DELETE PARENT?
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mDescription = findViewById(R.id.tv_long_description);
        mPrevious = findViewById(R.id.btn_previous_step);
        mNext = findViewById(R.id.btn_next_step);

        Intent intent = getIntent();
        if (intent == null) {
            Log.d("RSDA: ", "intent error");
        }

        setTitle(intent.getStringExtra("recipe_name"));

        if(intent.getParcelableArrayListExtra("ingredients") != null) {
            mIngredients = intent.getParcelableArrayListExtra("ingredients");

            String ingredientsList = "";
            for(int x = 0; x < mIngredients.size(); ++x) {
                Ingredient currentIngredient = mIngredients.get(x);

                ingredientsList +=  x + 1 + ". " +
                        currentIngredient.getQuantity() + " " +
                        currentIngredient.getMeasure() + " " +
                        currentIngredient.getIngredient() + "\n\n";
            }

            mDescription.setText(ingredientsList);
            mPrevious.setVisibility(View.INVISIBLE);
            mNext.setVisibility(View.INVISIBLE);
        } else if (intent.getParcelableArrayListExtra("steps") != null) {
            mSteps = intent.getParcelableArrayListExtra("steps");
            position = intent.getIntExtra("position", 0);

            mDescription.setText(mSteps.get(position).getDescription());
            //set up buttons for instructions?
            setUpButtons();
        }
    }

    private void previousStep() {
        try {
            Step prevStep = mSteps.get(position - 1);

            position--;
            mDescription.setText(prevStep.getDescription());
        } catch (Exception e) {
            Log.d("RSDA: ", "This is the first step, can't go back!");
        }
    }

    private void nextStep() {
        try {
            Step nextStep = mSteps.get(position + 1);

            position++;
            mDescription.setText(nextStep.getDescription());
        } catch (Exception e) {
            Log.d("RSDA: ", "This is the last step, can't go next!");
        }
    }

    private void setUpButtons() {
        mPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousStep();
            }
        });
        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextStep();
            }
        });
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
