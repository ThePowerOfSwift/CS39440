package com.cs39440.rob41.sudokuapp;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.content.FileProvider;
import android.util.Log;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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
public class CameraActivityTest {

    @Rule
    //Create the MainMenuActivity
    public ActivityTestRule mActivityRule = new ActivityTestRule<>(
            MainMenuActivity.class);

    @Test
    public void cameraTest(){
        //Generate a bitmap
        Bitmap photo = BitmapFactory.decodeResource
                (InstrumentationRegistry.getTargetContext().getResources(),R.drawable.test0);

        //Create file to save bitmap too
        /*try {

            File photoFile = super.createImageFile();
            //set file to the bitmap
            FileOutputStream outputStream = null;
            outputStream = new FileOutputStream(photoFile);
            photo.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.close();
            Log.d("Dummy photo creation","successful");

        } catch (IOException e) {
            e.printStackTrace();
        }*/

        //set file to the bitmap
        //call activity result for camera (will req activity result
    }
}
