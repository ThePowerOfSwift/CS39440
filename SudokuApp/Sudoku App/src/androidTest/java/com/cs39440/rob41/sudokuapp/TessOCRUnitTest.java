package com.cs39440.rob41.sudokuapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.InputStream;
import static org.junit.Assert.*;

/**
 * Created by Robert Bayliss on 09/04/2016
 */
@RunWith(AndroidJUnit4.class)
public class TessOCRUnitTest {
    @Rule
    //Create the MainMenuActivity
    public ActivityTestRule mActivityRule = new ActivityTestRule<>(
            MainMenuActivity.class);
    Context context = InstrumentationRegistry.getContext();
    TessOCR tessOCR = new TessOCR(context);

    @Test
    public void createTessOCR() throws Exception {
        assertNotNull("Datapath doesnt exist",tessOCR.getDatapath());
    }
    @Test
    public void readCharOneTessOCR() throws Exception {
        //Load test image from test assets folder
        InputStream testImg = context.getAssets().open("one_print.jpg");
        Bitmap one = BitmapFactory.decodeStream(testImg);
        String value = tessOCR.getOCRResult(one, 50);

        assertEquals("Should find 1","1",value);
    }

    @Test
    public void readCharTwoTessOCR() throws Exception {
        //Load test image from test assets folder
        InputStream testImg = context.getAssets().open("two_print.jpg");
        Bitmap two = BitmapFactory.decodeStream(testImg);
        String value = tessOCR.getOCRResult(two, 50);
        two.recycle();
        testImg.close();

        assertEquals("Should find 2","2",value);
    }

    @Test
    public void readCharThreeTessOCR() throws Exception {
        //Load test image from test assets folder
        InputStream testImg = context.getAssets().open("three_print.jpg");
        Bitmap three = BitmapFactory.decodeStream(testImg);
        String value = tessOCR.getOCRResult(three, 50);
        three.recycle();
        testImg.close();

        assertEquals("Should find 3","3",value);
    }

    @Test
    public void readCharFourTessOCR() throws Exception {
        //Load test image from test assets folder
        InputStream testImg = context.getAssets().open("four_print.jpg");
        Bitmap four = BitmapFactory.decodeStream(testImg);
        String value = tessOCR.getOCRResult(four, 50);
        four.recycle();
        testImg.close();

        assertEquals("Should find 4","4",value);
    }

    @Test
    public void readCharFiveTessOCR() throws Exception {
        //Load test image from test assets folder
        InputStream testImg = context.getAssets().open("five_print.jpg");
        Bitmap five = BitmapFactory.decodeStream(testImg);
        String value = tessOCR.getOCRResult(five, 50);
        five.recycle();
        testImg.close();

        assertEquals("Should find 5","5",value);
    }

    @Test
    public void readCharSixTessOCR() throws Exception {
        //Load test image from test assets folder
        InputStream testImg = context.getAssets().open("six_print.jpg");
        Bitmap six = BitmapFactory.decodeStream(testImg);
        String value = tessOCR.getOCRResult(six, 50);
        six.recycle();
        testImg.close();

        assertEquals("Should find 6","6",value);
    }

    @Test
    public void readCharSevenTessOCR() throws Exception {
        //Load test image from test assets folder
        InputStream testImg = context.getAssets().open("seven_print.jpg");
        Bitmap seven = BitmapFactory.decodeStream(testImg);
        String value = tessOCR.getOCRResult(seven, 50);
        seven.recycle();
        testImg.close();

        assertEquals("Should find 7","7",value);
    }

    @Test
    public void readCharEightTessOCR() throws Exception {
        //Load test image from test assets folder
        InputStream testImg = context.getAssets().open("eight_print.jpg");
        Bitmap eight = BitmapFactory.decodeStream(testImg);
        String value = tessOCR.getOCRResult(eight, 50);
        eight.recycle();
        testImg.close();

        assertEquals("Should find 8","8",value);
    }

    @Test
    public void readCharNineTessOCR() throws Exception {
        //Load test image from test assets folder
        InputStream testImg = context.getAssets().open("nine_print.jpg");
        Bitmap nine = BitmapFactory.decodeStream(testImg);
        String value = tessOCR.getOCRResult(nine, 50);
        nine.recycle();
        testImg.close();

        assertEquals("Should find 9","9",value);
    }

    @Test
    public void readCharPrint2OneTessOCR() throws Exception {
        //Load test image from test assets folder
        InputStream testImg = context.getAssets().open("one_print2.jpg");
        Bitmap one = BitmapFactory.decodeStream(testImg);
        String value = tessOCR.getOCRResult(one, 50);
        one.recycle();
        testImg.close();

        assertEquals("Should find 1","1",value);
    }

    @Test
    public void readCharPrint2TwoTessOCR() throws Exception {
        //Load test image from test assets folder
        InputStream testImg = context.getAssets().open("two_print2.jpg");
        Bitmap two = BitmapFactory.decodeStream(testImg);
        String value = tessOCR.getOCRResult(two, 50);
        two.recycle();
        testImg.close();

        assertEquals("Should find 2","2",value);
    }

    @Test
    public void readCharPrint2ThreeTessOCR() throws Exception {
        //Load test image from test assets folder
        InputStream testImg = context.getAssets().open("three_print2.jpg");
        Bitmap three = BitmapFactory.decodeStream(testImg);
        String value = tessOCR.getOCRResult(three, 50);
        three.recycle();
        testImg.close();

        assertEquals("Should find 3","3",value);
    }

    @Test
    public void readCharPrint2FourTessOCR() throws Exception {
        //Load test image from test assets folder
        InputStream testImg = context.getAssets().open("four_print2.jpg");
        Bitmap four = BitmapFactory.decodeStream(testImg);
        String value = tessOCR.getOCRResult(four, 50);
        four.recycle();
        testImg.close();

        assertEquals("Should find 4","4",value);
    }

    @Test
    public void readCharPrint2FiveTessOCR() throws Exception {
        //Load test image from test assets folder
        InputStream testImg = context.getAssets().open("five_print2.jpg");
        Bitmap five = BitmapFactory.decodeStream(testImg);
        String value = tessOCR.getOCRResult(five, 50);
        five.recycle();
        testImg.close();

        assertEquals("Should find 5","5",value);
    }

    @Test
    public void readCharPrint2SixTessOCR() throws Exception {
        //Load test image from test assets folder
        InputStream testImg = context.getAssets().open("six_print2.jpg");
        Bitmap six = BitmapFactory.decodeStream(testImg);
        String value = tessOCR.getOCRResult(six, 50);
        six.recycle();
        testImg.close();

        assertEquals("Should find 6","6",value);
    }

    @Test
    public void readCharPrint2SevenTessOCR() throws Exception {
        //Load test image from test assets folder
        InputStream testImg = context.getAssets().open("seven_print2.jpg");
        Bitmap seven = BitmapFactory.decodeStream(testImg);
        String value = tessOCR.getOCRResult(seven, 50);
        seven.recycle();
        testImg.close();

        assertEquals("Should find 7","7",value);
    }

    @Test
    public void readCharPrint2EightTessOCR() throws Exception {
        //Load test image from test assets folder
        InputStream testImg = context.getAssets().open("eight_print2.jpg");
        Bitmap eight = BitmapFactory.decodeStream(testImg);
        String value = tessOCR.getOCRResult(eight, 50);
        eight.recycle();
        testImg.close();

        assertEquals("Should find 8","8",value);
    }

    @Test
    public void readCharPrint2NineTessOCR() throws Exception {
        //Load test image from test assets folder
        InputStream testImg = context.getAssets().open("nine_print2.jpg");
        Bitmap nine = BitmapFactory.decodeStream(testImg);
        String value = tessOCR.getOCRResult(nine, 50);
        nine.recycle();
        testImg.close();

        assertEquals("Should find 9","9",value);
    }

    @Test
    public void readCharHandOneTessOCR() throws Exception {
        //Load test image from test assets folder
        InputStream testImg = context.getAssets().open("one_hand.jpg");
        Bitmap one = BitmapFactory.decodeStream(testImg);
        String value = tessOCR.getOCRResult(one, 50);
        one.recycle();
        testImg.close();

        assertEquals("Hand written should find 1","1",value);
    }

    @Test
    public void readCharHandTwoTessOCR() throws Exception {
        //Load test image from test assets folder
        InputStream testImg = context.getAssets().open("two_hand.jpg");
        Bitmap two = BitmapFactory.decodeStream(testImg);
        String value = tessOCR.getOCRResult(two, 50);
        two.recycle();
        testImg.close();

        assertEquals("Hand written should find 2","2",value);
    }

    @Test
    public void readCharHandThreeTessOCR() throws Exception {
        //Load test image from test assets folder
        InputStream testImg = context.getAssets().open("three_hand.jpg");
        Bitmap three = BitmapFactory.decodeStream(testImg);
        String value = tessOCR.getOCRResult(three, 50);
        three.recycle();
        testImg.close();

        assertEquals("Hand written should find 3","3",value);
    }

    @Test
    public void readCharHandFourTessOCR() throws Exception {
        //Load test image from test assets folder
        InputStream testImg = context.getAssets().open("four_hand.jpg");
        Bitmap four = BitmapFactory.decodeStream(testImg);
        String value = tessOCR.getOCRResult(four, 50);
        four.recycle();
        testImg.close();

        assertEquals("Hand written should find 4","4",value);
    }

    @Test
    public void readCharHandFiveTessOCR() throws Exception {
        //Load test image from test assets folder
        InputStream testImg = context.getAssets().open("five_hand.jpg");
        Bitmap five = BitmapFactory.decodeStream(testImg);
        String value = tessOCR.getOCRResult(five, 50);
        five.recycle();
        testImg.close();

        assertEquals("Hand written should find 5","5",value);
    }

    @Test
    public void readCharHandSixTessOCR() throws Exception {
        //Load test image from test assets folder
        InputStream testImg = context.getAssets().open("six_hand.jpg");
        Bitmap six = BitmapFactory.decodeStream(testImg);
        String value = tessOCR.getOCRResult(six, 50);
        six.recycle();
        testImg.close();

        assertEquals("Hand written should find 6","6",value);
    }

    @Test
    public void readCharHandSevenTessOCR() throws Exception {
        //Load test image from test assets folder
        InputStream testImg = context.getAssets().open("seven_hand.jpg");
        Bitmap seven = BitmapFactory.decodeStream(testImg);
        String value = tessOCR.getOCRResult(seven, 50);
        seven.recycle();
        testImg.close();

        assertEquals("Hand written should find 7","7",value);
    }

    @Test
    public void readCharHandEightTessOCR() throws Exception {
        //Load test image from test assets folder
        InputStream testImg = context.getAssets().open("eight_hand.jpg");
        Bitmap eight = BitmapFactory.decodeStream(testImg);
        String value = tessOCR.getOCRResult(eight, 50);
        eight.recycle();
        testImg.close();

        assertEquals("Hand written should find 8","8",value);
    }

    @Test
    public void readCharHandNineTessOCR() throws Exception {
        //Load test image from test assets folder
        InputStream testImg = context.getAssets().open("nine_hand.jpg");
        Bitmap nine = BitmapFactory.decodeStream(testImg);
        String value = tessOCR.getOCRResult(nine, 50);
        nine.recycle();
        testImg.close();

        assertEquals("Hand written should find 9", "9", value);
    }

    @Test
    public void readCharHand2OneTessOCR() throws Exception {
        //Load test image from test assets folder
        InputStream testImg = context.getAssets().open("one_hand2.jpg");
        Bitmap one = BitmapFactory.decodeStream(testImg);
        String value = tessOCR.getOCRResult(one, 50);
        one.recycle();
        testImg.close();

        assertEquals("Hand written2 should find 1","1",value);
    }

    @Test
    public void readCharHand2TwoTessOCR() throws Exception {
        //Load test image from test assets folder
        InputStream testImg = context.getAssets().open("two_hand2.jpg");
        Bitmap two = BitmapFactory.decodeStream(testImg);
        String value = tessOCR.getOCRResult(two, 50);
        two.recycle();
        testImg.close();

        assertEquals("Hand written should find 2","2",value);
    }

    @Test
    public void readCharHand2ThreeTessOCR() throws Exception {
        //Load test image from test assets folder
        InputStream testImg = context.getAssets().open("three_hand2.jpg");
        Bitmap three = BitmapFactory.decodeStream(testImg);
        String value = tessOCR.getOCRResult(three, 50);
        three.recycle();
        testImg.close();

        assertEquals("Hand written2 should find 3","3",value);
    }

    @Test
    public void readCharHand2FourTessOCR() throws Exception {
        //Load test image from test assets folder
        InputStream testImg = context.getAssets().open("four_hand2.jpg");
        Bitmap four = BitmapFactory.decodeStream(testImg);
        String value = tessOCR.getOCRResult(four, 50);
        four.recycle();
        testImg.close();

        assertEquals("Hand written2 should find 4","4",value);
    }

    @Test
    public void readCharHand2FiveTessOCR() throws Exception {
        //Load test image from test assets folder
        InputStream testImg = context.getAssets().open("five_hand2.jpg");
        Bitmap five = BitmapFactory.decodeStream(testImg);
        String value = tessOCR.getOCRResult(five, 50);
        five.recycle();
        testImg.close();

        assertEquals("Hand written2 should find 5","5",value);
    }

    @Test
    public void readCharHand2SixTessOCR() throws Exception {
        //Load test image from test assets folder
        InputStream testImg = context.getAssets().open("six_hand2.jpg");
        Bitmap six = BitmapFactory.decodeStream(testImg);
        String value = tessOCR.getOCRResult(six, 50);
        six.recycle();
        testImg.close();

        assertEquals("Hand written2 should find 6","6",value);
    }

    @Test
    public void readCharHand2SevenTessOCR() throws Exception {
        //Load test image from test assets folder
        InputStream testImg = context.getAssets().open("seven_hand2.jpg");
        Bitmap seven = BitmapFactory.decodeStream(testImg);
        String value = tessOCR.getOCRResult(seven, 50);
        seven.recycle();
        testImg.close();

        assertEquals("Hand written2 should find 7","7",value);
    }

    @Test
    public void readCharHand2EightTessOCR() throws Exception {
        //Load test image from test assets folder
        InputStream testImg = context.getAssets().open("eight_hand2.jpg");
        Bitmap eight = BitmapFactory.decodeStream(testImg);
        String value = tessOCR.getOCRResult(eight, 50);
        eight.recycle();
        testImg.close();

        assertEquals("Hand written 2 should find 8","8",value);
    }

    @Test
    public void readCharHand2NineTessOCR() throws Exception {
        //Load test image from test assets folder
        InputStream testImg = context.getAssets().open("nine_hand2.jpg");
        Bitmap nine = BitmapFactory.decodeStream(testImg);
        String value = tessOCR.getOCRResult(nine, 50);
        nine.recycle();
        testImg.close();

        assertEquals("Hand written 2 should find 9", "9", value);
    }

    @Test
    public void readCharNoneTessOCR() throws Exception {
        //Load test image from test assets folder
        InputStream testImg = context.getAssets().open("none.jpg");
        Bitmap none = BitmapFactory.decodeStream(testImg);
        String value = tessOCR.getOCRResult(none, 50);
        none.recycle();
        testImg.close();

        assertEquals("Should find none and return 0","0",value);
    }
}
