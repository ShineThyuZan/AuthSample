package com.galaxytechno.chat

import androidx.core.widget.TextViewCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerViewAccessibilityDelegate
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.galaxytechno.chat.presentation.single_activity.MainActivity

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @get:Rule()
    val activity = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.galaxytechno.chat", appContext.packageName)
    }

    @Test
    fun registerTest() {
        Thread.sleep(3000)
       onView(withId(R.id.btn_region_select)).perform(click())
        onView(withId(R.id.bs_country)).perform(click())
        onView(withId(R.id.et_phone)).perform(typeText("789987848"))
        pressBack()
        onView(withId(R.id.btn_login_to_sign_in)).perform(click())
        Thread.sleep(1000)
        onView(withId(R.id.et_signup_name)).perform(typeText("David Lay Thaw"))
        onView(withId(R.id.et_password)).perform(typeText("22222222"))
        Thread.sleep(1000)
        onView(withId(R.id.et_confirm_password)).perform(typeText("22222222"))
        pressBack()
        onView(withId(R.id.btn_next)).perform(click())
        Thread.sleep(1000)
        pressBack()
        Thread.sleep(2000)
        onView(withId(R.id.btn_verify_otp)).perform(click())
//        onView(withId(R.id.btn_question1)).perform(click())
//        Thread.sleep(1000)
//        onView(ViewMatchers.withId(R.id.bs_security_question)).perform(click())
//        Thread.sleep(1000)
//        onView(withId(R.id.et_answer1)).perform(typeText("Htilin Shwe Myo Taw"))
//        pressBack()
//        onView(withId(R.id.btn_question2)).perform(click())
//        Thread.sleep(1000)
//        onView(ViewMatchers.withId(R.id.bs_security_question)).perform(click())
//        Thread.sleep(1000)
//        onView(withId(R.id.et_answer2)).perform(typeText("BE(IST)"))
//        pressBack()
//        onView(withId(R.id.btn_question3)).perform(click())
//        Thread.sleep(1000)
//        onView(ViewMatchers.withId(R.id.bs_security_question)).perform(click())
//        Thread.sleep(1000)
//        onView(withId(R.id.et_answer3)).perform(typeText("Ballon d' Or in 2022"))
//        pressBack()
//        Thread.sleep(1000)
        onView(withId(R.id.cb_terms_and_service)).perform(click())
        Thread.sleep(80000)
//        onView(withId(R.id.btn_start)).perform(click())
//        Thread.sleep(7000)
//        onView(withId(R.id.dest_top_setting)).perform(click())
//        Thread.sleep(1000)
//        onView(ViewMatchers.withText("Logout")).perform(click())
//        Thread.sleep(7000)
    }

    @Test
    fun forgotPasswordOtpTest() {
        Thread.sleep(3000)
        onView(withId(R.id.btn_region_select)).perform(click())
        Thread.sleep(1000)
        onView(withId(R.id.bs_country)).perform(click())
        onView(withId(R.id.et_phone)).perform(typeText("789987664"))
        onView(withId(R.id.tvForgetPassword)).perform(click())
        Thread.sleep(3000)
        onView(withId(R.id.btnForgetPwdContinue)).perform(click())
        Thread.sleep(3000)
        onView(withId(R.id.rb_otp)).perform(click())
        onView(withId(R.id.btn_next)).perform(click())
        Thread.sleep(3000)
        pressBack()
        onView(withId(R.id.btn_verify_otp)).perform(click())
        onView(withId(R.id.et_pwd_new)).perform(typeText("22222222"))
        onView(withId(R.id.et_pwd_confirm)).perform(typeText("22222222"))
        pressBack()
        onView(withId(R.id.btnChange)).perform(click())
        Thread.sleep(5000)
    }

    @Test
    fun forgotPasswordQuestionTest() {
        Thread.sleep(3000)
        onView(withId(R.id.btn_region_select)).perform(click())
        Thread.sleep(1000)
        onView(withId(R.id.bs_country)).perform(click())
        onView(withId(R.id.et_phone)).perform(typeText("789987664"))
        onView(withId(R.id.tvForgetPassword)).perform(click())
        Thread.sleep(3000)
        onView(withId(R.id.btnForgetPwdContinue)).perform(click())
        Thread.sleep(3000)
        onView(withId(R.id.rb_question)).perform(click())
        onView(withId(R.id.btn_next)).perform(click())
        Thread.sleep(2000)
        onView(withId(R.id.etAnsOne)).perform(typeText("go Pin Kauk"))
        onView(withId(R.id.etAnsTwo)).perform(typeText("Mr Handsome"))
        onView(withId(R.id.etAnsThree)).perform(typeText("BE(IST)"))
        Thread.sleep(5000)
        pressBack()
        Thread.sleep(2000)
        onView(withId(R.id.btnQuestionNext)).perform(click())
        onView(withId(R.id.et_pwd_new)).perform(typeText("22222222"))
        onView(withId(R.id.et_pwd_confirm)).perform(typeText("22222222"))
        pressBack()
        onView(withId(R.id.btnChange)).perform(click())
        Thread.sleep(5000)
    }
}