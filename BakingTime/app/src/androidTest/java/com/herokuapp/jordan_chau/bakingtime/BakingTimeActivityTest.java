package com.herokuapp.jordan_chau.bakingtime;

import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class BakingTimeActivityTest {

    /*TODO tests to make:
        1) DONE - BakingTimeActivity - recyclerview --> click go to next activity
        2) DONE RecipeStepActivity - ingredient button --> show ingredients
        3) RecipeStepActivity - step button --> show Exoplayer, DONE show step description
        4) RecipeStepActivity - previous & next button --> check step description
     */

    private static final int LAST_ITEM = 3;

    @Rule
    public ActivityTestRule<BakingTimeActivity> mActivityRule = new ActivityTestRule<>(BakingTimeActivity.class);

    private IdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityRule.getActivity().getIdlingResource();
        // check if idling resource needs to be registered
        // To prove that the test fails, omit this call: --> test still passes
       // Espresso.registerIdlingResources(mIdlingResource);
    }

    //@Before
    //public void stubAllExternalIntents() {
        //need to stub intents before test run, external intents will be blocked
        //intending(not(isInternal())).respondWith(new ActivityResult(Activity.RESULT_OK, null));
    //}

    @Test
    public void scrollToLastItem_click_checkButtonDisplay() {
        // First scroll to the position that needs to be matched and click on it.
        onView(withId(R.id.rv_recipe_cards))
                .perform(RecyclerViewActions.actionOnItemAtPosition(LAST_ITEM, click()));

        //String itemElementText = mActivityRule.getActivity().getResources().getString(R.string.test_rv_string);

        onView(withId(R.id.btn_recipe_step_ingredients)).check(matches(isDisplayed()));

        /*intended(allOf(
                hasAction(),
                toPackage()
        )); */
    }

    //@After
    //public void unregisterIdlingResource() {
        //if (mIdlingResource != null) {
            //Espresso.unregisterIdlingResources(mIdlingResource);
        //}
    //}
}
