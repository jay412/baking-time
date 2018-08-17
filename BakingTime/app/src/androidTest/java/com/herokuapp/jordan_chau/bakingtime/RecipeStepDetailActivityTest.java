package com.herokuapp.jordan_chau.bakingtime;

import android.content.Intent;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

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
public class RecipeStepDetailActivityTest {
    private static final String DESCRIPTION = "Recipe Introduction";
    private static final String DESCRIPTION2 = "Test Description 2";
    private static final String DESCRIPTION3 = "Test Description 3";
    private static final String VIDEO_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd974_-intro-creampie/-intro-creampie.mp4";

    @Rule
    public ActivityTestRule<RecipeStepDetailActivity> mActivityRule = new ActivityTestRule<>(RecipeStepDetailActivity.class, false, false);

    @Before
    public void setUpIntent() {
        ArrayList<Step> fakeSteps = new ArrayList<>();
        fakeSteps.add(new Step(0, DESCRIPTION, DESCRIPTION, VIDEO_URL, ""));
        fakeSteps.add(new Step(1, DESCRIPTION2, DESCRIPTION2, VIDEO_URL, ""));
        fakeSteps.add(new Step(2, DESCRIPTION3, DESCRIPTION3, VIDEO_URL, ""));

        Intent i = new Intent();
        i.putExtra("option", "steps");
        i.putExtra("steps", fakeSteps);
        i.putExtra("position", 1);

        mActivityRule.launchActivity(i);
    }

    @Test
    public void clickPreviousButton_checkDescription() {
        onView(withId(R.id.btn_previous_step))
                .perform(click());
        onView(withId(R.id.tv_long_description)).check(matches(withText(DESCRIPTION)));
    }

    @Test
    public void clickNextButton_checkDescription() {
        onView(withId(R.id.btn_next_step))
                .perform(click());
        onView(withId(R.id.tv_long_description)).check(matches(withText(DESCRIPTION3)));
    }
}
