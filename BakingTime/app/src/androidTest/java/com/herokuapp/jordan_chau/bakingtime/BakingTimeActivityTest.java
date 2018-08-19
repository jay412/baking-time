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

    private static final int LAST_ITEM = 3;

    @Rule
    public ActivityTestRule<BakingTimeActivity> mActivityRule = new ActivityTestRule<>(BakingTimeActivity.class);

    @Before
    public void registerIdlingResource() {
        IdlingResource mIdlingResource = mActivityRule.getActivity().getIdlingResource();
        // check if idling resource needs to be registered
        // To prove that the test fails, omit this call: --> test still passes
       // Espresso.registerIdlingResources(mIdlingResource);
    }

    @Test
    public void scrollToLastItem_click_checkButtonDisplay() {
        // First scroll to the position that needs to be matched and click on it.
        onView(withId(R.id.rv_recipe_cards))
                .perform(RecyclerViewActions.actionOnItemAtPosition(LAST_ITEM, click()));

        //String itemElementText = mActivityRule.getActivity().getResources().getString(R.string.test_rv_string);

        onView(withId(R.id.btn_recipe_step_ingredients)).check(matches(isDisplayed()));
    }
}
