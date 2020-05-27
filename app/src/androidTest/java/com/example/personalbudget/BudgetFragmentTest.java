package com.example.personalbudget;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.UiDevice;

import org.junit.Rule;
import org.junit.Test;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import androidx.test.espresso.contrib.RecyclerViewActions;

import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;

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

    @Test
    public void AddBudgetWindowCancelTest() {
        /* click on AddBudgetItemButton to display the AddBudgetWindow */
        onView(allOf(withId(R.id.AddBudgetItemButton), isDisplayed())).perform(click());

        /* click on the addBudgetItemWindowCancelButton to dismiss the AddBudgetWindow */
        onView(withId(R.id.addBudgetItemWindowCancelButton)).perform(click());

        /* check if all the views in AddBudgetWindow are gone */
        onView(withId(R.id.addBudgetItemWindowDateTextView)).check(doesNotExist());
        onView(withId(R.id.addBudgetItemWindowDateEditText)).check(doesNotExist());
        onView(withId(R.id.addBudgetItemWindowValueTextView)).check(doesNotExist());
        onView(withId(R.id.addBudgetItemWindowValueEditText)).check(doesNotExist());
        onView(withId(R.id.addBudgetItemWindowDoneButton)).check(doesNotExist());
        onView(withId(R.id.addBudgetItemWindowCancelButton)).check(doesNotExist());
    }

    @Test
    public void AddBudgetItemTest() {
        /* click on AddBudgetItemButton to display the AddBudgetWindow */
        onView(allOf(withId(R.id.AddBudgetItemButton), isDisplayed())).perform(click());

        /* write date to addBudgetItemWindowDateEditText */
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        onView(withId(R.id.addBudgetItemWindowDateEditText)).perform(typeText(date));

        /* write value to */
        Random random = new Random();
        DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMaximumFractionDigits(2);
        String value = decimalFormat.format(random.nextFloat() * 500);
        onView(withId(R.id.addBudgetItemWindowValueEditText)).perform(typeText(value));

        /* click on addBudgetItemWindowDoneButton */
        onView(allOf(withId(R.id.addBudgetItemWindowDoneButton), isDisplayed())).perform(click());

        /* check if the added item is displayed */
        onView(allOf(withId(R.id.budgetRecyclerView), isDisplayed())).perform(RecyclerViewActions.scrollTo
                (allOf(hasDescendant(withText(date)), hasDescendant(withText(containsString(value))))));
        onView(allOf(withId(R.id.budgetRecyclerView), isDisplayed())).check(matches(allOf(hasDescendant(withText(date)),
                hasDescendant(withText(containsString(value))))));
    }
}