package com.github.lepaincestbon.bootcamp

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.Matchers
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GreetingActivityTest {
    @Test
    fun testMethod() {
        val intent = Intent(getApplicationContext(), GreetingActivity::class.java).putExtra(
            EXTRA_MESSAGE,
            TEST_NAME
        )

        val scenario = ActivityScenario.launch<GreetingActivity>(intent)
        scenario.use {
            Espresso.onView(ViewMatchers.withId(R.id.greetingMessage)).check(
                ViewAssertions.matches(
                    ViewMatchers.withText(
                        Matchers.containsString(TEST_NAME)
                    )
                )
            )
        }
    }

    companion object {
        const val TEST_NAME = "Test name"
    }

}
