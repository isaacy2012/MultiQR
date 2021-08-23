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
import androidx.test.rule.GrantPermissionRule
import androidx.test.runner.AndroidJUnit4
import com.innerCat.multiQR.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityLaunchTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Rule
    @JvmField
    var mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.CAMERA")

    @Test
    fun mainActivityLaunchTest() {
        val recyclerView = onView(
allOf(withId(R.id.rvItems),
withParent(withParent(withId(android.R.id.content))),
isDisplayed()))
        recyclerView.check(matches(isDisplayed()))
        
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
        appCompatEditText.perform(click())
        
        val appCompatEditText2 = onView(
allOf(withId(R.id.editText),
childAtPosition(
childAtPosition(
withId(R.id.custom),
0),
0),
isDisplayed()))
        appCompatEditText2.perform(replaceText("3000454545656"), closeSoftKeyboard())
        
        val materialButton = onView(
allOf(withId(android.R.id.button1), withText("Ok"),
childAtPosition(
childAtPosition(
withId(R.id.buttonPanel),
0),
3)))
        materialButton.perform(scrollTo(), click())
        
        val textView = onView(
allOf(withId(R.id.textTV), withText("3000454545656"),
withParent(withParent(withId(R.id.cardView))),
isDisplayed()))
        textView.check(matches(withText("3000454545656")))
        
        val textView2 = onView(
allOf(withId(R.id.textTV), withText("3000454545656"),
withParent(withParent(withId(R.id.cardView))),
isDisplayed()))
        textView2.check(matches(withText("3000454545656")))
        
        val recyclerView2 = onView(
allOf(withId(R.id.rvItems),
childAtPosition(
withClassName(`is`("androidx.coordinatorlayout.widget.CoordinatorLayout")),
2)))
        recyclerView2.perform(actionOnItemAtPosition<ViewHolder>(0, click()))
        
        val materialButton2 = onView(
allOf(withId(android.R.id.button3), withText("Delete"),
childAtPosition(
childAtPosition(
withId(R.id.buttonPanel),
0),
0)))
        materialButton2.perform(scrollTo(), click())
        
        val actionMenuItemView2 = onView(
allOf(withId(R.id.action_add_manually), withContentDescription("Add Manually"),
childAtPosition(
childAtPosition(
withId(R.id.toolbar),
1),
0),
isDisplayed()))
        actionMenuItemView2.perform(click())
        
        val appCompatEditText3 = onView(
allOf(withId(R.id.editText),
childAtPosition(
childAtPosition(
withId(R.id.custom),
0),
0),
isDisplayed()))
        appCompatEditText3.perform(replaceText("10000000"), closeSoftKeyboard())
        
        val materialButton3 = onView(
allOf(withId(android.R.id.button1), withText("Ok"),
childAtPosition(
childAtPosition(
withId(R.id.buttonPanel),
0),
3)))
        materialButton3.perform(scrollTo(), click())
        
        val textView3 = onView(
allOf(withId(R.id.textTV), withText("10000000"),
withParent(withParent(withId(R.id.cardView))),
isDisplayed()))
        textView3.check(matches(withText("10000000")))
        
        val floatingActionButton = onView(
allOf(withId(R.id.fab),
childAtPosition(
childAtPosition(
withId(android.R.id.content),
0),
1),
isDisplayed()))
        floatingActionButton.perform(click())
        
        pressBack()
        
        val textView4 = onView(
allOf(withId(R.id.textTV), withText("10000000"),
withParent(withParent(withId(R.id.cardView))),
isDisplayed()))
        textView4.check(matches(withText("10000000")))
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
