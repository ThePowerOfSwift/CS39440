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
 * Created by Robert Bayliss on 15-Apr-17.
 * Instrumentation test, which will execute on an Android device.
 */
@RunWith(AndroidJUnit4.class)
public class ActivitiesNavigationTest {

    private static final String PACKAGE_NAME = "com.cs39440.rob41.sudokuapp";
    private static final int timer = 1000;
    @Rule
    //Create the MainMenuActivity
    public ActivityTestRule mActivityRule = new ActivityTestRule<>(
            MainMenuActivity.class);

    @Test
    //Default generated test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals(PACKAGE_NAME, appContext.getPackageName());
    }

    @Test
    //Check the menu exists and has the correct buttons
    public void menuTab(){

    }

    @Test
    //Check the information page displays
    public void informationTabLoads(){
        //Setup a monitor to check activity has started
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(
                InformationActivity.class.getName(), null, false);

        //Click the information button
        onView(withId(R.id.information_button)).perform(click());
        //Wait (using timer) for a activity to load and Check that the activity has started
        Activity informationActivity = getInstrumentation().waitForMonitorWithTimeout(activityMonitor, timer);
        assertNotNull(informationActivity);
    }

    @Test
    //Check the information page closes
    public void informationTabClose(){
        //Setup a monitor to check activity has started
        Instrumentation.ActivityMonitor activityMonitor = getInstrumentation().addMonitor(
                InformationActivity.class.getName(), null, false);

        //Click the information button
        onView(withId(R.id.information_button)).perform(click());
        Activity informationActivity = getInstrumentation().waitForMonitorWithTimeout(activityMonitor, timer);
        assertNotNull(informationActivity);

        //Click the back button
        onView(withId(R.id.information_back_button)).perform(click());
        //Wait (using timer) for the activity to close and Check that the activity has finished
        informationActivity = getInstrumentation().waitForMonitorWithTimeout(activityMonitor, timer);
        assertNull(informationActivity);
    }

    @Test
    //Check the Loading activity loads
    public void loadingTabLoads() {
        //Setup a monitor to check loading activity has started
        Instrumentation.ActivityMonitor loadActivityMonitor = getInstrumentation().addMonitor(
                LoadingActivity.class.getName(), null, false);

        //Click the Create Sudoku button
        onView(withId(R.id.createSudoku_button)).perform(click());
        Activity loadActivity = getInstrumentation().waitForMonitor(loadActivityMonitor);
        assertNotNull(loadActivity);
    }

    @Test
    //Check the loading page redirects to the Play Sudoku page and the Loading activity has closed
    public void loadingTabRedirectsToPlay() {
        ///Setup a monitor to check play activity has started
        Instrumentation.ActivityMonitor playActivityMonitor = getInstrumentation().addMonitor(
                PlaySudokuActivity.class.getName(), null, false);

        //Setup a monitor to check loading activity has started
        Instrumentation.ActivityMonitor loadActivityMonitor = getInstrumentation().addMonitor(
                LoadingActivity.class.getName(), null, false);

        //Click the Create Sudoku button
        onView(withId(R.id.createSudoku_button)).perform(click());
        Activity loadActivity = getInstrumentation().waitForMonitor(loadActivityMonitor);

        //Wait (using timer) for a activity to load and Check that the activity has started
        Activity playActivity = getInstrumentation().waitForMonitor(playActivityMonitor);
        assertNotNull(playActivity);
        //Check that the loading activity has closed
        loadActivity = getInstrumentation().waitForMonitorWithTimeout(loadActivityMonitor, timer);
        assertNull(loadActivity);
    }

    @Test
    //Check Play Sudoku page closes
    public void createSudokuTabLoad(){
        //Setup a monitor to check play activity has started
        Instrumentation.ActivityMonitor playActivityMonitor = getInstrumentation().addMonitor(
                PlaySudokuActivity.class.getName(), null, false);

        //Setup a monitor to check loading activity has started
        Instrumentation.ActivityMonitor loadActivityMonitor = getInstrumentation().addMonitor(
                LoadingActivity.class.getName(), null, false);

        //Click the Create Sudoku button
        onView(withId(R.id.createSudoku_button)).perform(click());
        Activity loadActivity = getInstrumentation().waitForMonitor(loadActivityMonitor);
        assertNotNull(loadActivity);

        //Wait (using timer) for a activity to load and Check that the activity has started
        Activity playActivity = getInstrumentation().waitForMonitorWithTimeout(playActivityMonitor, timer);
        assertNotNull(playActivity);
        //Check that the loading activity has closed
        loadActivity = getInstrumentation().waitForMonitorWithTimeout(loadActivityMonitor, timer);
        assertNull(loadActivity);

        //Click the back button
        onView(withId(R.id.play_back_button)).perform(click());
        //Wait (using timer) for the activity to close and Check that the activity has finished
        playActivity = getInstrumentation().waitForMonitorWithTimeout(playActivityMonitor, timer);
        assertNull(playActivity);
    }

    @Test
    //Check the menu exists and has the correct buttons
    public void cameraTab(){
        //load camera
        //load camera and quit
        //load camera and take photo
        //load camera, take photo and quit
        //load camera, take photo and play
        //load camera, take photo, play and quit
        //load camera, take photo, play and solve
    }
}
