package com.herokuapp.jordan_chau.bakingtime.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.herokuapp.jordan_chau.bakingtime.BakingTimeActivity;
import com.herokuapp.jordan_chau.bakingtime.R;
import com.herokuapp.jordan_chau.bakingtime.model.Ingredient;

import java.util.ArrayList;

/**
 * Implementation of App Widget functionality.
 */
public class BakingWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        //CharSequence widgetText = context.getString(R.string.appwidget_text);
        // views.setTextViewText(R.id.appwidget_text, widgetText);

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_widget_provider);

        /*
        String recipeName = bundle.getString("name");
        ArrayList<Ingredient> mIngredients = bundle.getParcelableArrayList("ingredients");

        String ingredientsList = "";
        for (int x = 0; x < mIngredients.size(); ++x) {
            Ingredient currentIngredient = mIngredients.get(x);

            ingredientsList += x + 1 + ". " +
                    currentIngredient.getQuantity() + " " +
                    currentIngredient.getMeasure() + " " +
                    currentIngredient.getIngredient() + "\n\n";
        }

        views.setTextViewText(R.id.tv_widget_recipe_name, recipeName);
        views.setTextViewText(R.id.tv_widget_recipe_ingredients, ingredientsList); */

        //Intent intent = context.getIntent();
        //Intent intent = new Intent(context, BakingTimeActivity.class);
        //PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        //views.setOnClickPendingIntent(R.id.widget_baking_image, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

