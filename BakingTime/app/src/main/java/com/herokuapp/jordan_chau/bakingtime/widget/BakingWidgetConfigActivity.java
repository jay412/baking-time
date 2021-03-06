package com.herokuapp.jordan_chau.bakingtime.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.herokuapp.jordan_chau.bakingtime.BakingTimeActivity;
import com.herokuapp.jordan_chau.bakingtime.R;
import com.herokuapp.jordan_chau.bakingtime.adapter.RecipeCardAdapter;
import com.herokuapp.jordan_chau.bakingtime.model.Ingredient;
import com.herokuapp.jordan_chau.bakingtime.model.Recipe;
import com.herokuapp.jordan_chau.bakingtime.util.NetworkUtility;

import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BakingWidgetConfigActivity extends Activity implements RecipeCardAdapter.RecipeItemClickListener{
    @BindView(R.id.rv_widget_cards) RecyclerView mRecipeList;

    private ArrayList<Recipe> mRecipes;
    private SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.widget_config_activity);
        setResult(RESULT_CANCELED);
        ButterKnife.bind(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecipeList.setLayoutManager(layoutManager);
        mRecipeList.setHasFixedSize(true);

        setUpSharedPreferences();

        new GetOperation(this).execute();
    }

    @Override
    public void onRecipeItemClicked(int clickedItemIndex) {
        showAppWidget(clickedItemIndex);
    }

    private int appWidgetId;
    private void showAppWidget(int position) {
        appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

        //Retrieve widget ID from intent that launched the Activity
        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        if(bundle != null) {
            appWidgetId = bundle.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

            if(appWidgetId ==  AppWidgetManager.INVALID_APPWIDGET_ID) {
                finish();
            }

            //Retrieve data from clicked button
            String recipeName = mRecipes.get(position).getName();
            ArrayList<Ingredient> mIngredients = mRecipes.get(position).getIngredients();

            //Convert ingredients arraylist to string
            String ingredientsList = "";
            for (int x = 0; x < mIngredients.size(); ++x) {
                Ingredient currentIngredient = mIngredients.get(x);

                ingredientsList += x + 1 + ". " +
                        currentIngredient.getQuantity() + " " +
                        currentIngredient.getMeasure() + " " +
                        currentIngredient.getIngredient() + "\n\n";
            }

            //put strings into shared preferences
            if(mEditor != null) {
                mEditor.putString("name", recipeName);
                mEditor.putString("ingredients", ingredientsList);
                mEditor.commit();
            }

            //Perform the configuration and get an instance of the AppWidgetManager
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            BakingWidgetProvider.updateAppWidget(this, appWidgetManager, appWidgetId);

            Intent returnIntent = new Intent();
            returnIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

            setResult(RESULT_OK, returnIntent);

            finish();
        }
    }

    private void setUpSharedPreferences() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("widget_pref", MODE_PRIVATE);
        mEditor = pref.edit();
    }

    private class GetOperation extends AsyncTask<String, Void, ArrayList<Recipe>> {
        private Context context;

        private GetOperation(Context c) {
            context = c;
        }

        @Override
        protected void onPreExecute() {
            //checks for internet connection before proceeding
                super.onPreExecute();
        }

        @Override
        protected ArrayList<Recipe> doInBackground(String... params) {

            URL recipeJSONUrl = NetworkUtility.buildURL();

            try {
                String jsonUserResponse = NetworkUtility.getHttpUrlResponse(recipeJSONUrl);

                return BakingTimeActivity.getRecipeStringsFromJson(jsonUserResponse);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Recipe> rData) {

            if(rData != null) {
                mRecipes = rData;
                RecipeCardAdapter mAdapter = new RecipeCardAdapter(rData, (RecipeCardAdapter.RecipeItemClickListener) context);
                mRecipeList.setAdapter(mAdapter);
            } else {
                //NetworkUtility.showErrorMessage(mLayout);
            }
        }
    }
}
