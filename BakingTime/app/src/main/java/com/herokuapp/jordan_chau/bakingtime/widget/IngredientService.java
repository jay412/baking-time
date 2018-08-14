package com.herokuapp.jordan_chau.bakingtime.widget;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.herokuapp.jordan_chau.bakingtime.model.Ingredient;

public class IngredientService extends IntentService{

    public static final String ACTION_SHOW_INGREDIENTS = "com.herokuapp.jordan_chau.bakingtime.action.show_ingredients";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public IngredientService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent != null) {
            final String action = intent.getAction();
            if(ACTION_SHOW_INGREDIENTS.equals(action)) {
                handleActionShowIngredients();
            }
        }
    }

    public static void startActionShowIngredients(Context context) {
        Intent intent = new Intent(context, Ingredient.class);
        intent.setAction(ACTION_SHOW_INGREDIENTS);
        context.startService(intent);
    }

    private void handleActionShowIngredients() {
        //retrieve ingredient information here
    }
}
