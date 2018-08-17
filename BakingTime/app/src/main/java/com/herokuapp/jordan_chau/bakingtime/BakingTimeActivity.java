package com.herokuapp.jordan_chau.bakingtime;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.widget.LinearLayout;

import com.herokuapp.jordan_chau.bakingtime.adapter.RecipeCardAdapter;
import com.herokuapp.jordan_chau.bakingtime.model.Ingredient;
import com.herokuapp.jordan_chau.bakingtime.model.Recipe;
import com.herokuapp.jordan_chau.bakingtime.model.Step;
import com.herokuapp.jordan_chau.bakingtime.test.AsyncIdlingResource;
import com.herokuapp.jordan_chau.bakingtime.util.NetworkUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BakingTimeActivity extends AppCompatActivity implements RecipeCardAdapter.RecipeItemClickListener{
    @BindView(R.id.baking_time_linear_layout) LinearLayout mLayout;
    @BindView(R.id.rv_recipe_cards) RecyclerView mRecipeList;
    private RecipeCardAdapter mAdapter;

    private ArrayList<Recipe> mRecipes;

    @Nullable private AsyncIdlingResource mIdlingResource;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baking_time);

        //Butterknife binding
        ButterKnife.bind(this);

        //check device width
        if(isTablet(this)) {
            mRecipeList.setLayoutManager(new GridLayoutManager(this, 3));
        } else {
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            mRecipeList.setLayoutManager(layoutManager);
        }

        mRecipeList.setHasFixedSize(true);
        //testing
        getIdlingResource();

        if(savedInstanceState != null) {
            mRecipes = savedInstanceState.getParcelableArrayList("recipes");
            mAdapter = new RecipeCardAdapter(mRecipes, this);
            mRecipeList.setAdapter(mAdapter);
        } else {
            new GetOperation(this).execute();
        }
    }

    //when recipe card/item is clicked
    @Override
    public void onRecipeItemClicked(int clickedItemIndex) {
        Intent i = new Intent(BakingTimeActivity.this, RecipeStepActivity.class);
        i.putExtra("recipe", mRecipes.get(clickedItemIndex));
        i.putExtra("step", mRecipes.get(clickedItemIndex).getSteps());
        i.putExtra("ingredient", mRecipes.get(clickedItemIndex).getIngredients());
        startActivity(i);
    }

    /**
     * <h1>Get Operation</h1>
     * <p> This is an AsyncTask class to perform a GET operation in the background
     */
    private class GetOperation extends AsyncTask<String, Void, ArrayList<Recipe>> {
        private ProgressDialog progressDialog;
        private Context context;

        private GetOperation(Context c) {
            progressDialog = new ProgressDialog(c);
            context = c;
        }

        @Override
        protected void onPreExecute() {
            //checks for internet connection before proceeding
            if(!NetworkUtility.checkInternetConnection(context)) {
                this.cancel(true);
                NetworkUtility.showErrorMessage(mLayout);
            }
            else {
                super.onPreExecute();

                progressDialog.setTitle("Please wait ...");
                progressDialog.show();

                if (mIdlingResource != null) {
                    mIdlingResource.setIdleState(false);
                }
            }
        }

        @Override
        protected ArrayList<Recipe> doInBackground(String... params) {
            //if (params.length == 0) {
                //return null;
            //}
            
            URL recipeJSONUrl = NetworkUtility.buildURL();

            try {
                String jsonUserResponse = NetworkUtility.getHttpUrlResponse(recipeJSONUrl);

                return getRecipeStringsFromJson(jsonUserResponse);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Recipe> rData) {
            progressDialog.dismiss();

            if(rData != null) {
                mRecipes = rData;
                mAdapter = new RecipeCardAdapter(rData, (RecipeCardAdapter.RecipeItemClickListener) context);
                mRecipeList.setAdapter(mAdapter);
            } else {
                NetworkUtility.showErrorMessage(mLayout);
            }

            if (mIdlingResource != null) {
                mIdlingResource.setIdleState(true);
            }
        }
    }

    //parse recipe strings from json
    public static ArrayList<Recipe> getRecipeStringsFromJson(String json) throws JSONException {

        JSONArray recipes = new JSONArray(json);

        //all recipes
        ArrayList<Recipe> parsedRecipeData = new ArrayList<>();
        //for every recipe
        for(int i = 0; i < recipes.length(); ++i) {

            JSONObject currentRecipe = recipes.getJSONObject(i);

            int id = currentRecipe.getInt("id");
            String name = currentRecipe.getString("name");
            int servings = currentRecipe.getInt("servings");
            String image = currentRecipe.getString("image");

            //ingredient array --> objects
            JSONArray currentIngredients = currentRecipe.getJSONArray("ingredients");
            ArrayList<Ingredient> ingredients = new ArrayList<>();

            for(int j = 0; j < currentIngredients.length(); ++j) {
                JSONObject currentIngredient = currentIngredients.getJSONObject(j);

                int quantity = currentIngredient.getInt("quantity");
                String measure = currentIngredient.getString("measure");
                String ingredient = currentIngredient.getString("ingredient");

                ingredients.add(new Ingredient(quantity, measure, ingredient));
            }

            //steps array --> objects
            JSONArray currentSteps = currentRecipe.getJSONArray("steps");
            ArrayList<Step> steps = new ArrayList<>();

            for(int k = 0; k < currentSteps.length(); ++k) {
                JSONObject currentStep = currentSteps.getJSONObject(k);

                int stepID = currentStep.getInt("id");
                String shortDescription = currentStep.getString("shortDescription");
                String description = currentStep.getString("description");
                String videoURL = currentStep.getString("videoURL");
                String thumbnailURL = currentStep.getString("thumbnailURL");

                steps.add(new Step(stepID, shortDescription, description, videoURL, thumbnailURL));
            }

            parsedRecipeData.add(new Recipe(id, name, ingredients, steps, servings, image));
        }

        return parsedRecipeData;
    }

    //check if device is a tablet
    public static Boolean isTablet(Context context) {
        Display display = ((Activity)context).getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);

        double width = displayMetrics.widthPixels / (double)displayMetrics.densityDpi;
        double height = displayMetrics.heightPixels / (double)displayMetrics.densityDpi;

        double screenDiagonal = Math.sqrt(Math.pow(width, 2) + Math.pow(height, 2));

        Log.i("BTA: ", "screenDiagonal = " + screenDiagonal);
        return (screenDiagonal >= 7.0);
    }

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new AsyncIdlingResource();
        }
        return mIdlingResource;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if(mRecipes != null)
            outState.putParcelableArrayList("recipes", mRecipes);
    }
}
