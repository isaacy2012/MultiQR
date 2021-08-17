package com.innerCat.multiQR.activities


import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.innerCat.multiQR.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivitySettingsTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun mainActivitySettingsTest() {
        val actionMenuItemView = onView(
allOf(withId(R.id.action_add_manually), withContentDescription("Add Manually"),
childAtPosition(
childAtPosition(
withId(R.id.toolbar),
1),
0),
isDisplayed()))
        actionMenuItemView.perform(click())

        val appCompatEditText = onView(
allOf(withId(R.id.editText),
childAtPosition(
childAtPosition(
withId(R.id.custom),
0),
0),
isDisplayed()))
        appCompatEditText.perform(replaceText("30000"), closeSoftKeyboard())

        val materialButton = onView(
allOf(withId(android.R.id.button1), withText("Ok"),
childAtPosition(
childAtPosition(
withId(R.id.buttonPanel),
0),
3)))
        materialButton.perform(scrollTo(), click())

        val actionMenuItemView2 = onView(
allOf(withId(R.id.action_add_manually), withContentDescription("Add Manually"),
childAtPosition(
childAtPosition(
withId(R.id.toolbar),
1),
0),
isDisplayed()))
        actionMenuItemView2.perform(click())

        val materialButton2 = onView(
allOf(withId(android.R.id.button2), withText("Cancel"),
childAtPosition(
childAtPosition(
withId(R.id.buttonPanel),
0),
2)))
        materialButton2.perform(scrollTo(), click())

        val overflowMenuButton2 = onView(
allOf(withContentDescription("More options"),
    childAtPosition(
        childAtPosition(
            withId(R.id.toolbar),
            3),
        0),
isDisplayed()))
        overflowMenuButton2.perform(click())

        val materialTextView = onView(
allOf(withId(R.id.title), withText("Settings"),
childAtPosition(
childAtPosition(
withId(R.id.content),
0),
0),
isDisplayed()))
        materialTextView.perform(click())

        val recyclerView = onView(
allOf(withId(R.id.recycler_view),
childAtPosition(
withId(android.R.id.list_container),
0)))
        recyclerView.perform(actionOnItemAtPosition<ViewHolder>(4, click()))

        val recyclerView2 = onView(
allOf(withId(R.id.recycler_view),
childAtPosition(
withId(android.R.id.list_container),
0)))
        recyclerView2.perform(actionOnItemAtPosition<ViewHolder>(5, click()))

        val appCompatEditText2 = onView(
allOf(withId(android.R.id.edit),
childAtPosition(
childAtPosition(
withClassName(`is`("android.widget.ScrollView")),
0),
1)))
        appCompatEditText2.perform(scrollTo(), replaceText("3"), closeSoftKeyboard())

        val materialButton3 = onView(
allOf(withId(android.R.id.button1), withText("OK"),
childAtPosition(
childAtPosition(
withId(R.id.buttonPanel),
0),
3)))
        materialButton3.perform(scrollTo(), click())

        pressBack()

        val overflowMenuButton3 = onView(
allOf(withContentDescription("More options"),
childAtPosition(
childAtPosition(
withId(R.id.toolbar),
1),
2),
isDisplayed()))
        overflowMenuButton3.perform(click())

        val materialTextView2 = onView(
allOf(withId(R.id.title), withText("Settings"),
childAtPosition(
childAtPosition(
withId(R.id.content),
0),
0),
isDisplayed()))
        materialTextView2.perform(click())

        val textView = onView(
allOf(withId(android.R.id.summary), withText("3"),
withParent(withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java))),
isDisplayed()))
        textView.check(matches(withText("3")))

        val switch_ = onView(
allOf(withId(R.id.switchWidget),
withParent(allOf(withId(android.R.id.widget_frame),
withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java)))),
isDisplayed()))
        switch_.check(matches(isDisplayed()))

        val recyclerView3 = onView(
allOf(withId(R.id.recycler_view),
childAtPosition(
withId(android.R.id.list_container),
0)))
        recyclerView3.perform(actionOnItemAtPosition<ViewHolder>(1, click()))

        val appCompatEditText3 = onView(
allOf(withId(android.R.id.edit),
childAtPosition(
childAtPosition(
withClassName(`is`("android.widget.ScrollView")),
0),
1)))
        appCompatEditText3.perform(scrollTo(), replaceText("jason@jason"), closeSoftKeyboard())

        val appCompatEditText4 = onView(
allOf(withId(android.R.id.edit), withText("jason@jason"),
childAtPosition(
childAtPosition(
withClassName(`is`("android.widget.ScrollView")),
0),
1)))
        appCompatEditText4.perform(scrollTo(), click())

        val appCompatEditText5 = onView(
allOf(withId(android.R.id.edit), withText("jason@jason"),
childAtPosition(
childAtPosition(
withClassName(`is`("android.widget.ScrollView")),
0),
1)))
        appCompatEditText5.perform(scrollTo(), click())

        val appCompatEditText6 = onView(
allOf(withId(android.R.id.edit), withText("jason@jason"),
childAtPosition(
childAtPosition(
withClassName(`is`("android.widget.ScrollView")),
0),
1)))
        appCompatEditText6.perform(scrollTo(), click())

        val appCompatEditText7 = onView(
allOf(withId(android.R.id.edit), withText("jason@jason"),
childAtPosition(
childAtPosition(
withClassName(`is`("android.widget.ScrollView")),
0),
1)))
        appCompatEditText7.perform(scrollTo(), click())

        val appCompatEditText8 = onView(
allOf(withId(android.R.id.edit), withText("jason@jason"),
childAtPosition(
childAtPosition(
withClassName(`is`("android.widget.ScrollView")),
0),
1)))
        appCompatEditText8.perform(scrollTo(), replaceText("test@test"))

        val appCompatEditText9 = onView(
allOf(withId(android.R.id.edit), withText("test@test"),
childAtPosition(
childAtPosition(
withClassName(`is`("android.widget.ScrollView")),
0),
1),
isDisplayed()))
        appCompatEditText9.perform(closeSoftKeyboard())

        val materialButton4 = onView(
allOf(withId(android.R.id.button1), withText("OK"),
childAtPosition(
childAtPosition(
withId(R.id.buttonPanel),
0),
3)))
        materialButton4.perform(scrollTo(), click())

        pressBack()

        val overflowMenuButton4 = onView(
allOf(withContentDescription("More options"),
childAtPosition(
childAtPosition(
withId(R.id.toolbar),
1),
2),
isDisplayed()))
        overflowMenuButton4.perform(click())

        val materialTextView3 = onView(
allOf(withId(R.id.title), withText("Settings"),
childAtPosition(
childAtPosition(
withId(R.id.content),
0),
0),
isDisplayed()))
        materialTextView3.perform(click())

        val textView2 = onView(
allOf(withId(android.R.id.summary), withText("test@test"),
withParent(withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java))),
isDisplayed()))
        textView2.check(matches(withText("test@test")))

        pressBack()
        }

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
