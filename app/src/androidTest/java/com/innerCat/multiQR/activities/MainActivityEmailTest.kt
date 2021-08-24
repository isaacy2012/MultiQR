package com.innerCat.multiQR.activities


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.innerCat.multiQR.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityEmailTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun mainActivityEmailTest() {
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
allOf(withId(R.id.edit),
childAtPosition(
childAtPosition(
withId(R.id.custom),
0),
0),
isDisplayed()))
        appCompatEditText.perform(replaceText("3000"), closeSoftKeyboard())
        
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
        
        val appCompatEditText2 = onView(
allOf(withId(R.id.edit),
childAtPosition(
childAtPosition(
withId(R.id.custom),
0),
0),
isDisplayed()))
        appCompatEditText2.perform(replaceText("20000"), closeSoftKeyboard())
        
        val materialButton2 = onView(
allOf(withId(android.R.id.button1), withText("Ok"),
childAtPosition(
childAtPosition(
withId(R.id.buttonPanel),
0),
3)))
        materialButton2.perform(scrollTo(), click())
        
        val actionMenuItemView3 = onView(
allOf(withId(R.id.action_add_manually), withContentDescription("Add Manually"),
childAtPosition(
childAtPosition(
withId(R.id.toolbar),
1),
0),
isDisplayed()))
        actionMenuItemView3.perform(click())
        
        val appCompatEditText3 = onView(
allOf(withId(R.id.edit),
childAtPosition(
childAtPosition(
withId(R.id.custom),
0),
0),
isDisplayed()))
        appCompatEditText3.perform(replaceText("100000"), closeSoftKeyboard())
        
        val materialButton3 = onView(
allOf(withId(android.R.id.button1), withText("Ok"),
childAtPosition(
childAtPosition(
withId(R.id.buttonPanel),
0),
3)))
        materialButton3.perform(scrollTo(), click())
        
        val actionMenuItemView4 = onView(
allOf(withId(R.id.action_send), withContentDescription("Send Email"),
childAtPosition(
childAtPosition(
withId(R.id.toolbar),
1),
1),
isDisplayed()))
        actionMenuItemView4.perform(click())
        /*
        val editText = onView(
allOf(withId(com.google.android.gm.R.id.subject), withText("[COMP103] IDScanner Participation - 18:17 2021-07-11"),
withParent(allOf(withId(com.google.android.gm.R.id.subject_content),
withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java)))),
isDisplayed()))
        editText.check(matches(withText("[COMP103] IDScanner Participation - 18:17 2021-07-11")))
        
        val webView = onView(
allOf(withId(com.google.android.gm.R.id.wc_body),
withParent(allOf(withId(com.google.android.gm.R.id.wc_body_layout),
withParent(withId(com.google.android.gm.R.id.body_wrapper)))),
isDisplayed()))
        webView.check(matches(isDisplayed())) */ //withText("100000,\n20000\n300")
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
