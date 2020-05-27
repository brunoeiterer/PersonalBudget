package com.example.personalbudget;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.UiDevice;

import org.junit.Rule;
import org.junit.Test;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.assertion.ViewAssertions.matches;

import static org.hamcrest.Matchers.allOf;

public class BudgetFragmentTest {
    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void AddBudgetWindowPopupTest() {
        onView(allOf(withId(R.id.AddBudgetItemButton), isDisplayed())).perform(click());

        onView(withId(R.id.addBudgetItemWindowDateTextView)).check(matches(isDisplayed()));
        onView(withId(R.id.addBudgetItemWindowDateEditText)).check(matches(isDisplayed()));
        onView(withId(R.id.addBudgetItemWindowValueTextView)).check(matches(isDisplayed()));
        onView(withId(R.id.addBudgetItemWindowValueEditText)).check(matches(isDisplayed()));
        onView(withId(R.id.addBudgetItemWindowDoneButton)).check(matches(isDisplayed()));
        onView(withId(R.id.addBudgetItemWindowCancelButton)).check(matches(isDisplayed()));
    }

    @Test
    public void AddBudgetWindowDismissTest() {
        onView(allOf(withId(R.id.AddBudgetItemButton), isDisplayed())).perform(click());

        /* pressBack to dismiss the window */
        UiDevice mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        mDevice.pressBack();

        onView(withId(R.id.addBudgetItemWindowDateTextView)).check(doesNotExist());
        onView(withId(R.id.addBudgetItemWindowDateEditText)).check(doesNotExist());
        onView(withId(R.id.addBudgetItemWindowValueTextView)).check(doesNotExist());
        onView(withId(R.id.addBudgetItemWindowValueEditText)).check(doesNotExist());
        onView(withId(R.id.addBudgetItemWindowDoneButton)).check(doesNotExist());
        onView(withId(R.id.addBudgetItemWindowCancelButton)).check(doesNotExist());
    }
}