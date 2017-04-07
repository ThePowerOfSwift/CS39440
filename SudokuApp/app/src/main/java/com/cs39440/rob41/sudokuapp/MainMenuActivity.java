package com.cs39440.rob41.sudokuapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import org.opencv.android.OpenCVLoader;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MainMenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        if (!OpenCVLoader.initDebug()) {
            Log.d(this.getClass().getSimpleName(), "  OpenCVLoader.initDebug(), not working.");
        } else {
            Log.d(this.getClass().getSimpleName(), "  OpenCVLoader.initDebug(), working.");
        }
    }

    /*
    *My Code - this will handle the actions to follow once buttons have been clicked
    */

    public void openCamera(View View){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 1);
        }
    }

    //code to run once a picture has been taken
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //If the user took a picture direct to OCR element otherwise fall through to main menu
        if(resultCode == RESULT_OK) {
            Intent loadingIntent = new Intent(this, LoadingActivity.class);
            loadingIntent.putExtra("fromImage",true);
            //http://stackoverflow.com/questions/8035008/how-to-pass-image-captured-by-camera-to-next-screen-in-android
            //Bundle extras = data.getExtras();
            //Bitmap imageBitmap = (Bitmap) extras.get("data");
            //mImageView.setImageBitmap(imageBitmap);
            startActivity(loadingIntent);
        }
    }

    public void createSudoku(View View){
        Intent loadingIntent = new Intent(this, LoadingActivity.class);
        startActivity (loadingIntent);
    }

    public void openInformation(View View){
        Intent intent = new Intent(this, InformationActivity.class);
        startActivity (intent);
    }

    public void exit(View View){
        //if the API version is 21 or greater
        if (Build.VERSION.SDK_INT >= 21){
            finishAndRemoveTask();
        // If the API version is lower than 21
        }else{
            this.finishAffinity();
        }
    }
}
