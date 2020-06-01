package com.example.personalbudget;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.UiDevice;

import org.junit.Rule;
import org.junit.Test;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.util.Random;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
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
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;

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

        /* write value to addBudgetItemWindowValueEditText */
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

    @Test
    public void AddBudgetItemInvalidDateTest() {
        /* click on AddBudgetItemButton to display the AddBudgetWindow */
        onView(allOf(withId(R.id.AddBudgetItemButton), isDisplayed())).perform(click());

        /* write date to addBudgetItemWindowDateEditText */
        String date = "Invalid Date";
        onView(withId(R.id.addBudgetItemWindowDateEditText)).perform(typeText(date));

        /* click on addBudgetItemWindowDoneButton */
        onView(allOf(withId(R.id.addBudgetItemWindowDoneButton), isDisplayed())).perform(click());

        /* check if the error dialogue is displayed */
        onView(withText(R.string.add_budget_item_dialog_invalid_date_message)).check(matches(isDisplayed()));
    }

    @Test
    public void RemoveBudgetItemTest() {
        /* click on AddBudgetItemButton to display the AddBudgetWindow */
        onView(allOf(withId(R.id.AddBudgetItemButton), isDisplayed())).perform(click());

        /* write date to addBudgetItemWindowDateEditText */
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        onView(withId(R.id.addBudgetItemWindowDateEditText)).perform(typeText(date));

        /* write value to addBudgetItemWindowValueEditText */
        Random random = new Random();
        DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMaximumFractionDigits(2);
        String value = decimalFormat.format(random.nextFloat() * 500);
        onView(withId(R.id.addBudgetItemWindowValueEditText)).perform(typeText(value));

        /* click on addBudgetItemWindowDoneButton */
        onView(allOf(withId(R.id.addBudgetItemWindowDoneButton), isDisplayed())).perform(click());

        /* select the added budgetItem */
        onView(allOf(withId(R.id.budgetRecyclerView), isDisplayed())).perform(RecyclerViewActions.scrollTo
                (allOf(hasDescendant(withText(date)), hasDescendant(withText(containsString(value))))));
        onView(allOf(withId(R.id.budgetRecyclerView), isDisplayed())).perform(RecyclerViewActions.actionOnItem(allOf(hasDescendant(withText(date)),
                hasDescendant(withText(containsString(value)))), click()));

        /* click on the remove button */
        onView(allOf(withId(R.id.RemoveBudgetItemButton), isDisplayed())).perform(click());

        /* check if budgetItem was removed */
        onView(allOf(withId(R.id.budgetRecyclerView), isDisplayed())).check(matches(not(allOf(hasDescendant(withText(date)),
                hasDescendant(withText(containsString(value)))))));
    }

    @Test
    public void EditBudgetItemTest() {
        /* click on AddBudgetItemButton to display the AddBudgetWindow */
        onView(allOf(withId(R.id.AddBudgetItemButton), isDisplayed())).perform(click());

        /* write date to addBudgetItemWindowDateEditText */
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        onView(withId(R.id.addBudgetItemWindowDateEditText)).perform(typeText(date));

        /* write value to addBudgetItemWindowValueEditText */
        Random random = new Random();
        DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMaximumFractionDigits(2);
        String value = decimalFormat.format(random.nextFloat() * 500);
        onView(withId(R.id.addBudgetItemWindowValueEditText)).perform(typeText(value));

        /* click on addBudgetItemWindowDoneButton */
        onView(allOf(withId(R.id.addBudgetItemWindowDoneButton), isDisplayed())).perform(click());

        /* select the added budgetItem */
        onView(allOf(withId(R.id.budgetRecyclerView), isDisplayed())).perform(RecyclerViewActions.scrollTo
                (allOf(hasDescendant(withText(date)), hasDescendant(withText(containsString(value))))));
        onView(allOf(withId(R.id.budgetRecyclerView), isDisplayed())).perform(RecyclerViewActions.actionOnItem(allOf(hasDescendant(withText(date)),
                hasDescendant(withText(containsString(value)))), click()));

        /* click on the EditBudgetItemButton */
        onView(allOf(withId(R.id.EditBudgetItemButton), isDisplayed())).perform(click());

        /* write new date to addBudgetItemWindowDateEditText */
        String newDate = LocalDate.now().minus(1, ChronoUnit.DAYS).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        onView(withId(R.id.addBudgetItemWindowDateEditText)).perform(clearText(), typeText(newDate));

        /* write new value to addBudgetItemWindowValueEditText */
        decimalFormat.setMaximumFractionDigits(2);
        String newValue = decimalFormat.format(random.nextFloat() * 500);
        onView(withId(R.id.addBudgetItemWindowValueEditText)).perform(clearText(), typeText(newValue));

        /* click on addBudgetItemWindowDoneButton */
        onView(allOf(withId(R.id.addBudgetItemWindowDoneButton), isDisplayed())).perform(click());

        /* check if old date and value are gone */
        onView(allOf(withId(R.id.budgetRecyclerView), isDisplayed())).check(matches(not(allOf(hasDescendant(withText(date)),
                hasDescendant(withText(containsString(value)))))));

        /* check if new date and value are showing */
        onView(allOf(withId(R.id.budgetRecyclerView), isDisplayed())).check(matches(allOf(hasDescendant(withText(newDate)),
                hasDescendant(withText(containsString(newValue))))));
    }
}