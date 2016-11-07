package com.it.mougang.gasmyr.takecare;


import android.support.annotation.NonNull;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @NonNull
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void mainActivityTest() {
        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("TakeCare"),
                        withParent(withId(R.id.toolbar)),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction switch_ = onView(
                allOf(withId(R.id.mySwitch), isDisplayed()));
        switch_.perform(click());

        ViewInteraction switch_2 = onView(
                allOf(withId(R.id.mySwitch), isDisplayed()));
        switch_2.perform(click());

        ViewInteraction switch_3 = onView(
                allOf(withId(R.id.mySwitch), isDisplayed()));
        switch_3.perform(click());

        ViewInteraction switch_4 = onView(
                allOf(withId(R.id.mySwitch), isDisplayed()));
        switch_4.perform(click());

        ViewInteraction switch_5 = onView(
                allOf(withId(R.id.mySwitch), isDisplayed()));
        switch_5.perform(click());

        ViewInteraction switch_6 = onView(
                allOf(withId(R.id.mySwitch), isDisplayed()));
        switch_6.perform(click());

        ViewInteraction switch_7 = onView(
                allOf(withId(R.id.mySwitch), isDisplayed()));
        switch_7.perform(click());

        ViewInteraction switch_8 = onView(
                allOf(withId(R.id.mySwitch), isDisplayed()));
        switch_8.perform(click());

        ViewInteraction switch_9 = onView(
                allOf(withId(R.id.mySwitch), isDisplayed()));
        switch_9.perform(click());

        ViewInteraction switch_10 = onView(
                allOf(withId(R.id.mySwitch), isDisplayed()));
        switch_10.perform(click());

        ViewInteraction switch_11 = onView(
                allOf(withId(R.id.mySwitch), isDisplayed()));
        switch_11.perform(click());

        ViewInteraction switch_12 = onView(
                allOf(withId(R.id.mySwitch), isDisplayed()));
        switch_12.perform(click());

        ViewInteraction appCompatImageView = onView(
                allOf(withId(R.id.userImage), isDisplayed()));
        appCompatImageView.perform(click());

        ViewInteraction cardView = onView(
                allOf(withId(R.id.card_view), isDisplayed()));
        cardView.perform(click());

        ViewInteraction appCompatTextView = onView(
                allOf(withId(R.id.profileEmail), withText("unknown@gmail.com"), isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction textView = onView(
                allOf(withText("Todos"), isDisplayed()));
        textView.perform(click());

        ViewInteraction textView2 = onView(
                allOf(withText("Birthdays"), isDisplayed()));
        textView2.perform(click());

        ViewInteraction textView3 = onView(
                allOf(withText("Said hello"), isDisplayed()));
        textView3.perform(click());

        ViewInteraction textView4 = onView(
                allOf(withText("Alarms"), isDisplayed()));
        textView4.perform(click());

        ViewInteraction textView5 = onView(
                allOf(withText("Recordings"), isDisplayed()));
        textView5.perform(click());

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        ViewInteraction appCompatTextView2 = onView(
                allOf(withId(R.id.title), withText("Settings"), isDisplayed()));
        appCompatTextView2.perform(click());

        ViewInteraction linearLayout = onView(
                allOf(childAtPosition(
                        allOf(withId(android.R.id.list),
                                withParent(withClassName(is("android.widget.LinearLayout")))),
                        0),
                        isDisplayed()));
        linearLayout.perform(click());

        ViewInteraction linearLayout2 = onView(
                allOf(childAtPosition(
                        withId(android.R.id.list),
                        0),
                        isDisplayed()));
        linearLayout2.perform(click());

        ViewInteraction linearLayout3 = onView(
                allOf(childAtPosition(
                        withId(android.R.id.list),
                        0),
                        isDisplayed()));
        linearLayout3.perform(click());

    }

    @NonNull
    private static Matcher<View> childAtPosition(
            @NonNull final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(@NonNull Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(@NonNull View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
