package com.herokuapp.jordan_chau.bakingtime;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.herokuapp.jordan_chau.bakingtime.model.Step;

import java.util.ArrayList;

public class RecipeStepDetailActivity extends AppCompatActivity {
    private TextView mDescription;
    private Button mPrevious, mNext;
    //private String description;
    private ArrayList<Step> mSteps;
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

        mSteps = intent.getParcelableArrayListExtra("steps");
        position = intent.getIntExtra("position", 0);

        mDescription.setText(mSteps.get(position).getDescription());
        setUpButtons();
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
}
