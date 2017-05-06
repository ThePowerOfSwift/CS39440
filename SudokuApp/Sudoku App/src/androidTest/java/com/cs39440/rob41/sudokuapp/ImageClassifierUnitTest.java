package com.cs39440.rob41.sudokuapp;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.InputStream;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Created by Robert Bayliss on 08/04/17.
 */

public class ImageClassifierUnitTest {
    @Rule
    //Create the MainMenuActivity
    public ActivityTestRule mActivityRule = new ActivityTestRule<>(
            MainMenuActivity.class);
    Context context = InstrumentationRegistry.getContext();

    @Test
    //Basic PNG image
    public void test0ImageClassifier() throws Exception {
        //Load test sudoku image from test assets folder
        InputStream testImg = context.getAssets().open("atest0.png");
        Bitmap test0 = BitmapFactory.decodeStream(testImg);
        test0.recycle();
        testImg.close();
        ImageClassifier imgClass = new ImageClassifier(test0,9,context);
        int answer [] =
                {7, 8, 0, 4, 0, 0, 1, 2, 0,
                        6, 0, 0, 0, 7, 5, 0, 0, 9,
                        0, 0, 0, 6, 0, 1, 0, 7, 8,
                        0, 0, 7, 0, 4, 0, 2, 6, 0,
                        0, 0, 1, 0, 5, 0, 9, 3, 0,
                        9, 0, 4, 0, 6, 0, 0, 0, 5,
                        0, 7, 0, 3, 0, 0, 0, 1, 2,
                        1, 2, 0, 0, 0, 7, 4, 0, 0,
                        0, 4, 9, 2, 0, 6, 0, 0, 7};
        int actual [] = imgClass.getCellValues();
        int incorrect = 0;
        //assertArrayEquals("Test0 Cell values incorrect" ,answer ,actual);
        for (int count = 0 ; count <answer.length ; count++){
            if(answer[count] != actual[count]) {
                incorrect++;
            }
        }
        assertEquals("Test0 total number incorrect" ,0 ,incorrect);
    }

    @Test
    //Basic JPG image
    public void test1ImageClassifier() throws Exception {
        //Load test sudoku image from test assets folder
        InputStream testImg = context.getAssets().open("atest1.jpg");
        Bitmap test1 = BitmapFactory.decodeStream(testImg);
        test1.recycle();
        testImg.close();
        ImageClassifier imgClass = new ImageClassifier(test1,9,context);
        int answer [] =
                {7, 8, 0, 4, 0, 0, 1, 2, 0,
                        6, 0, 0, 0, 7, 5, 0, 0, 9,
                        0, 0, 0, 6, 0, 1, 0, 7, 8,
                        0, 0, 7, 0, 4, 0, 2, 6, 0,
                        0, 0, 1, 0, 5, 0, 9, 3, 0,
                        9, 0, 4, 0, 6, 0, 0, 0, 5,
                        0, 7, 0, 3, 0, 0, 0, 1, 2,
                        1, 2, 0, 0, 0, 7, 4, 0, 0,
                        0, 4, 9, 2, 0, 6, 0, 0, 7};
        int actual [] = imgClass.getCellValues();
        int incorrect = 0;
        //assertArrayEquals("Test1 Cell values incorrect" ,answer ,actual);
        for (int count = 0 ; count <answer.length ; count++){
            if(answer[count] != actual[count]) {
                incorrect++;
            }
        }
        assertEquals("Test1 total number incorrect" ,0 ,incorrect);
    }

    @Test
    //Basic photo - uncluttered and minimal rotation and Skewing
    public void test2ImageClassifier() throws Exception {
        //Load test sudoku image from test assets folder
        InputStream testImg = context.getAssets().open("atest2.jpg");
        Bitmap test2 = BitmapFactory.decodeStream(testImg);
        test2.recycle();
        testImg.close();
        ImageClassifier imgClass = new ImageClassifier(test2,9,context);
        int answer [] ={1, 0, 0, 0, 0, 9, 0, 0, 8,
                        0, 0, 7, 0, 0, 0, 3, 9, 0,
                        0, 0, 4, 0, 7, 0, 0, 0, 0,
                        0, 0, 5, 2, 0, 0, 0, 4, 0,
                        9, 0, 0, 0, 0, 0, 0, 0, 5,
                        0, 2, 0, 0, 0, 8, 6, 0, 0,
                        0, 0, 0, 0, 1, 0, 2, 0, 0,
                        0, 5, 9, 0, 0, 0, 7, 0, 0,
                        6, 0, 0, 8, 0, 0, 0, 0, 9};
        int actual [] = imgClass.getCellValues();
        int incorrect = 0;
        //assertArrayEquals("Test1 Cell values incorrect" ,answer ,actual);
        for (int count = 0 ; count <answer.length ; count++){
            if(answer[count] != actual[count]) {
                incorrect++;
            }
        }
        assertEquals("Test2 total number incorrect" ,0 ,incorrect);
    }

    @Test
    //uncluttered, slightly bowed
    public void test3ImageClassifier() throws Exception {
        //Load test sudoku image from test assets folder
        InputStream testImg = context.getAssets().open("atest3.jpg");
        Bitmap test3 = BitmapFactory.decodeStream(testImg);
        test3.recycle();
        testImg.close();
        ImageClassifier imgClass = new ImageClassifier(test3,9,context);
        int answer [] ={3, 6, 1, 0, 0, 4, 0, 0, 0,
                        0, 0, 0, 3, 0, 0, 0, 0, 6,
                        0, 7, 0, 0, 0, 0, 0, 0, 9,
                        0, 0, 2, 0, 1, 0, 0, 5, 0,
                        0, 0, 9, 0, 0, 0, 6, 0, 0,
                        0, 5, 0, 0, 2, 0, 8, 0, 0,
                        6, 0, 0, 0, 0, 0, 0, 1, 0,
                        8, 0, 0, 0, 0, 7, 0, 0, 0,
                        0, 0, 0, 9, 0, 0, 5, 6, 4};
        /*      [3, 6, 1, 0, 0, 4, 0, 0, 0,
                0, 0, 0, 3, 0, 0, 0, 1, 6,
                0, 7, 0, 0, 0, 0, 0, 0, 9,
                0, 0,0_2,0,0_1,0, 0, 5, 0,
                0, 0, 9, 0, 0, 0, 6, 0, 0,
                0, 5, 0, 0,1_2,0,2_8,0, 0,
                6, 0, 0, 0, 0, 2, 0, 1, 0,
                8, 0, 0, 0, 0, 7, 0, 0, 0,
                0, 0, 0, 9, 0, 0, 5, 6, 4]*/
        int actual [] = imgClass.getCellValues();
        int incorrect = 0;
        //assertArrayEquals("Test1 Cell values incorrect" ,answer ,actual);
        for (int count = 0 ; count <answer.length ; count++){
            if(answer[count] != actual[count]) {
                incorrect++;
            }
        }
        Log.d("TEST VALUJE ",Arrays.toString(actual));
        assertEquals("Test3 total number incorrect" ,0 ,incorrect);//4 errors
    }

    @Test
    //uncluttered, slightly bowed
    public void test4ImageClassifier() throws Exception {
        //Load test sudoku image from test assets folder
        InputStream testImg = context.getAssets().open("atest4.jpg");
        Bitmap test4 = BitmapFactory.decodeStream(testImg);
        test4.recycle();
        testImg.close();
        ImageClassifier imgClass = new ImageClassifier(test4,9,context);
        int answer [] ={0, 6, 0, 3, 0, 9, 0, 1, 0,
                0, 0, 3, 0, 7, 0, 8, 0, 0,
                0, 0, 0, 4, 0, 5, 0, 0, 0,
                7, 0, 2, 0, 0, 0, 5, 0, 9,
                0, 5, 0, 0, 0, 0, 0, 6, 0,
                6, 0, 1, 0, 0, 0, 2, 0, 7,
                0, 0, 0, 2, 0, 6, 0, 0, 0,
                0, 0, 8, 0, 1, 0, 6, 0, 0,
                0, 4, 0, 8, 0, 3, 0, 2, 0};
        int actual [] = imgClass.getCellValues();
        int incorrect = 0;
        //assertArrayEquals("Test1 Cell values incorrect" ,answer ,actual);
        for (int count = 0 ; count <answer.length ; count++){
            if(answer[count] != actual[count]) {
                incorrect++;
            }
        }
        Log.d("TEST VALUJE ",Arrays.toString(actual));
        assertEquals("Test3 total number incorrect" ,0 ,incorrect);
    }

    //Slight skew, handwritten, completed
    @Test
    public void test10ImageClassifier() throws Exception {
        //Load test sudoku image from test assets folder
        InputStream testImg = context.getAssets().open("atest10.jpg");
        Bitmap test10 = BitmapFactory.decodeStream(testImg);
        test10.recycle();
        testImg.close();
        ImageClassifier imgClass = new ImageClassifier(test10,9,context);
        int answer [] ={5, 2, 4, 1, 7, 9, 8, 3, 6,
                        3, 9, 1, 8, 5, 6, 4, 2, 7,
                        6, 8, 7, 3, 4, 2, 5, 9, 1,
                        7, 4, 9, 6, 2, 3, 1, 8, 5,
                        1, 6, 2, 7, 8, 5, 9, 4, 3,
                        8, 5, 3, 4, 9, 1, 7, 6, 2,
                        9, 1, 6, 5, 3, 4, 2, 7, 8,
                        4, 3, 8, 2, 1, 7, 6, 5, 9,
                        2, 7, 5, 9, 6, 8, 3, 1, 4};
        int actual [] = imgClass.getCellValues();
        int incorrect = 0;
        //assertArrayEquals("Test1 Cell values incorrect" ,answer ,actual);
        for (int count = 0 ; count <answer.length ; count++){
            if(answer[count] != actual[count]) {
                incorrect++;
            }
        }
        Log.d("TEST VALUJE ",Arrays.toString(actual));
        assertEquals("Test10 total number incorrect" ,0 ,incorrect);//15 errors
    }

    //SAME AS TEST10 WITH HAND WRITTING REMOVED
    @Test
    public void test11ImageClassifier() throws Exception {
        //Load test sudoku image from test assets folder
        InputStream testImg = context.getAssets().open("atest11.jpg");
        Bitmap test11 = BitmapFactory.decodeStream(testImg);
        test11.recycle();
        testImg.close();
        ImageClassifier imgClass = new ImageClassifier(test11,9,context);
        int answer [] ={5, 2, 0, 0, 0, 0, 0, 3, 6,
                0, 0, 1, 8, 0, 6, 4, 0, 0,
                0, 0, 7, 0, 4, 0, 5, 0, 0,
                0, 4, 0, 6, 0, 3, 0, 8, 0,
                0, 0, 2, 0, 0, 0, 9, 0, 0,
                0, 5, 0, 4, 0, 1, 0, 6, 0,
                0, 0, 6, 0, 3, 0, 2, 0, 0,
                0, 0, 8, 2, 0, 7, 6, 0, 0,
                2, 7, 0, 0, 0, 0, 0, 1, 4};
        int actual [] = imgClass.getCellValues();
        int incorrect = 0;
        //assertArrayEquals("Test1 Cell values incorrect" ,answer ,actual);
        for (int count = 0 ; count <answer.length ; count++){
            if(answer[count] != actual[count]) {
                incorrect++;
            }
        }
        Log.d("TEST VALUJE ",Arrays.toString(actual));
        assertEquals("Test11 total number incorrect" ,0 ,incorrect);
    }
}
