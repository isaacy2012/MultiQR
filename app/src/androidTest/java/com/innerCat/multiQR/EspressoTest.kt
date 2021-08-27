package com.innerCat.multiQR

import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.innerCat.multiQR.activities.MainActivity
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.String

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class HelloWorldEspressoTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    /**
     * Testing that the menu is visible
     */
    @Test
    fun simpleLaunchTest() {
        onView(withText("ID Scanner")).check(matches(isDisplayed()))
    }

    /**
     * Testing that you can manually add entries
     */
    @Test
    fun simpleAddTest() {
        val appCompatEditText = addSimpleEntry("30000")

        appCompatEditText.check(matches(withText("30000")))
    }

    /**
     * Testing settings you can change
     */
    @Test
    fun simpleChangeSettings() {
        addSimpleEntry("30000")

        gotoSettings()

        // click on the edittext and insert email
        val appCompatEditText3 = onView(
            Matchers.allOf(
                ViewMatchers.withId(android.R.id.edit),
                childAtPosition(
                    childAtPosition(
                        ViewMatchers.withClassName(Matchers.`is`("android.widget.ScrollView")),
                        0
                    ),
                    1
                )
            )
        )
        appCompatEditText3.perform(
            ViewActions.scrollTo(),
            ViewActions.replaceText("test@test.com"),
            ViewActions.closeSoftKeyboard()
        )

        appCompatEditText3.check(matches(withText("test@test.com")))
    }



    /**
     * Util function for adding simple entry
     */
    private fun addSimpleEntry(entry : String) : ViewInteraction {
        // Open "Add Manually" Menu
        val actionMenuItemView2 = onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.action_add_manually),
                ViewMatchers.withContentDescription("Add Manually"),
                childAtPosition(
                    childAtPosition(
                        ViewMatchers.withId(R.id.toolbar),
                        1
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        actionMenuItemView2.perform(ViewActions.click())

        // Add item
        val appCompatEditText = onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.edit),
                childAtPosition(
                    childAtPosition(
                        ViewMatchers.withId(R.id.custom),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatEditText.perform(ViewActions.replaceText(entry), ViewActions.closeSoftKeyboard())

        return appCompatEditText;
    }

    /**
     * Util function for clicking on "More Options" > "Settings"
     */
    private fun gotoSettings() {
        val overflowMenuButton3 = onView(
            Matchers.allOf(
                ViewMatchers.withContentDescription("More options"),
                childAtPosition(
                    childAtPosition(
                        ViewMatchers.withId(R.id.toolbar),
                        1
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        overflowMenuButton3.perform(ViewActions.click())

        val materialTextView2 = onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.title), withText("Settings"),
                childAtPosition(
                    childAtPosition(
                        ViewMatchers.withId(R.id.content),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        materialTextView2.perform(ViewActions.click())
    }


    /**
     * Util function for finding a child in the view
     */
    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }

}
