package com.herokuapp.jordan_chau.bakingtime;

import android.content.Intent;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.herokuapp.jordan_chau.bakingtime.model.Ingredient;
import com.herokuapp.jordan_chau.bakingtime.model.Recipe;
import com.herokuapp.jordan_chau.bakingtime.model.Step;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class RecipeStepActivityTest {
    private static final int POSITION = 0;
    //fake data strings
    private static final String DESCRIPTION = "Recipe Introduction";
    private static final String VIDEO_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd974_-intro-creampie/-intro-creampie.mp4";

    @Rule
    //third parameter is set to false which means the activity is not started automatically
    public ActivityTestRule<RecipeStepActivity> mActivityRule = new ActivityTestRule<>(RecipeStepActivity.class, false, false);

    @Before
    //set up intent before starting activity
    public void setUpIntent() {

        //create fake recipe
        ArrayList<Ingredient> fakeIngredients = new ArrayList<>();
        fakeIngredients.add(new Ingredient(200, "CUP", "Water"));

        ArrayList<Step> fakeSteps = new ArrayList<>();
        fakeSteps.add(new Step(0, DESCRIPTION, DESCRIPTION, VIDEO_URL, ""));

        Recipe mRecipe = new Recipe(0, "Testcake", fakeIngredients, fakeSteps, 8, "");

        //add data to intents
        Intent i = new Intent();
        i.putExtra("recipe", mRecipe);
        i.putExtra("step", mRecipe.getSteps());
        i.putExtra("ingredient", mRecipe.getIngredients());

        //launch activity
        mActivityRule.launchActivity(i);
    }

    @Test
    public void clickIngredientButton_checkIngredientText() {
        // Click ingredients button
        onView(withId(R.id.btn_recipe_step_ingredients))
                .perform(click());

        // When RecipeStepDetailActivity is displayed,
        // Check if description text view displays the correct ingredient text (1. 200 CUPS WATER + "\n\n")
        String itemElementText = mActivityRule.getActivity().getResources().getString(R.string.test_ingredient_string) + "\n\n";

        onView(withId(R.id.tv_long_description)).check(matches(withText(itemElementText)));
    }

    @Test
    public void scrollToFirstStepItem_click_checkStepDescription() {
        // First scroll to the position that needs to be matched and click on it.
        onView(withId(R.id.rv_recipe_steps))
                .perform(RecyclerViewActions.actionOnItemAtPosition(POSITION, click()));

        // When RecipeStepDetailActivity is displayed,
        // Check if first description text view displays the correct step description (Recipe Introduction)
        String itemElementText = mActivityRule.getActivity().getResources().getString(R.string.test_step_string);

        onView(withId(R.id.tv_long_description)).check(matches(withText(itemElementText)));
    }

     /* No need to stub intents because intent is required at activity creation
    @Before
    //public void stubAllExternalIntents() {
    //need to stub intents before test run, external intents will be blocked
        //intending(not(isInternal())).respondWith(new ActivityResult(Activity.RESULT_OK, null));
    }

    //receive intent stub from baking time activity with fake recipe data
        //intending(hasComponent(hasShortClassName(".BakingTimeActivity")))
                //.respondWith(new ActivityResult(Activity.RESULT_OK,
                        //BakingTimeActivity.createResultData(mRecipes, POSITION)));
    */
}
