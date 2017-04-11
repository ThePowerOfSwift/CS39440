package com.cs39440.rob41.sudokuapp;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ActivitiesInstrumentedTest {

    private static final String PACKAGE_NAME = "com.cs39440.rob41.sudokuapp";
    private static final int timer = 1000;
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals(PACKAGE_NAME, appContext.getPackageName());
    }

    @Rule
    //Create the MainMenuActivity
    public ActivityTestRule mActivityRule = new ActivityTestRule<>(
            MainMenuActivity.class);


    @Test
    //check the menu has the correct buttons
    public void testMenu(){

    }

    @Test
    //Check the information page displays
    public void informationTab(){
        //Setup a monitor to check activity has started
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(
                InformationActivity.class.getName(), null, false);

        //Click the information button
        onView(withId(R.id.information_button)).perform(click());
        //Wait (using timer) for a activity to load and Check that the activity has started
        Activity currentActivity = getInstrumentation().waitForMonitorWithTimeout(activityMonitor, timer);
        assertNotNull(currentActivity);

        //Click the back button
        onView(withId(R.id.information_back_button)).perform(click());
        //Wait (using timer) for the activity to close and Check that the activity has finished
        currentActivity = getInstrumentation().waitForMonitorWithTimeout(activityMonitor, timer);
        assertNull(currentActivity);
    }

    public void createSudokuTab(){
        //Setup a monitor to check activity has started
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(
                PlaySudokuActivity.class.getName(), null, false);

        //Click the Create Sudoku button
        onView(withId(R.id.createSudoku_button)).perform(click());
        //Wait (using timer) for a activity to load and Check that the activity has started
        Activity currentActivity = getInstrumentation().waitForMonitorWithTimeout(activityMonitor, timer);
        assertNotNull(currentActivity);

        //Click the back button
        onView(withId(R.id.play_back_button)).perform(click());
        //Wait (using timer) for the activity to close and Check that the activity has finished
        currentActivity = getInstrumentation().waitForMonitorWithTimeout(activityMonitor, timer);
        assertNull(currentActivity);
    }
}
