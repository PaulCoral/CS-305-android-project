package com.github.lepaincestbon.bootcamp;

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.Matchers
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @Rule
    fun testRule() = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testMethod() {
        Intents.init()
        val TEST_NAME = "Test name"

        Espresso.onView(ViewMatchers.withId(R.id.mainName)).perform(replaceText(TEST_NAME))
        closeSoftKeyboard()
        Espresso.onView(ViewMatchers.withId(R.id.mainGoButton)).perform(click())
        Intents.intended(
            Matchers.allOf(
                IntentMatchers.hasComponent(GreetingActivityTest::class.java.name),
                IntentMatchers.hasExtra(
                    EXTRA_MESSAGE, TEST_NAME
                )
            )
        )


        Intents.release()
    }

}
