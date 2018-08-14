package com.herokuapp.jordan_chau.bakingtime.widget;

import android.app.Activity;
import android.app.ProgressDialog;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
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
    private RecipeCardAdapter mAdapter;
    private ArrayList<Recipe> mRecipes;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.widget_config_activity);
        setResult(RESULT_CANCELED);
        ButterKnife.bind(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecipeList.setLayoutManager(layoutManager);
        mRecipeList.setHasFixedSize(true);

        new GetOperation(this).execute();
    }

    @Override
    public void onRecipeItemClicked(int clickedItemIndex) {
        //do something
        showAppWidget(clickedItemIndex);
    }

    int appWidgetId;
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

            String recipeName = mRecipes.get(position).getName();
            ArrayList<Ingredient> mIngredients = mRecipes.get(position).getIngredients();

            Bundle b = new Bundle();
            b.putString("name", recipeName);
            b.putParcelableArrayList("ingredients", mIngredients);

            //TODO Perform the configuration and get an instance of the AppWidgetManager
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            BakingWidgetProvider.updateAppWidget(this, appWidgetManager, appWidgetId);

            //TODO USE SharedPreferences

            Intent returnIntent = new Intent();

            returnIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            //returnIntent.putExtra("name", recipeName);
            //returnIntent.putExtra("ingredients", mIngredients);

            setResult(RESULT_OK, returnIntent);

            finish();
        }
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
            //if (params.length == 0) {
              //  return null;
            //}

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
                mAdapter = new RecipeCardAdapter(rData, (RecipeCardAdapter.RecipeItemClickListener) context);
                mRecipeList.setAdapter(mAdapter);
            } else {
                //NetworkUtility.showErrorMessage(mLayout);
            }
        }
    }
}
