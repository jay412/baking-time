package com.herokuapp.jordan_chau.bakingtime;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.espresso.contrib.RecyclerViewActions;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class RecyclerViewRecipeCardTest {
    private static final int LAST_ITEM = 4;

    @Rule
    public ActivityTestRule<BakingTimeActivity> mActivityRule = new ActivityTestRule<>(BakingTimeActivity.class);

    private IdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityRule.getActivity().getIdlingResource();
        // To prove that the test fails, omit this call:
        Espresso.registerIdlingResources(mIdlingResource);
    }

    @Test
    public void scrollToLastItem_checkText() {
        // First scroll to the position that needs to be matched and click on it.
        onView(ViewMatchers.withId(R.id.rv_recipe_cards))
                .perform(RecyclerViewActions.actionOnItemAtPosition(LAST_ITEM, click()));

        // Match the text in an item below the fold and check that it's displayed.
        //todo change to check intent
        String itemElementText = mActivityRule.getActivity().getResources().getString(
                R.string.test_rv_string) + String.valueOf(LAST_ITEM);
        onView(withText(itemElementText)).check(matches(isDisplayed()));
    }

    //@After
    //public void unregisterIdlingResource() {
        //if (mIdlingResource != null) {
            //Espresso.unregisterIdlingResources(mIdlingResource);
        //}
    //}
}
