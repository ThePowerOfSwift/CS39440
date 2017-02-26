package com.cs39440.rob41.sudokuapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MainMenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_menu);
    }

    /*
    *My Code - this will handle the actions to follow once buttons have been clicked
    */

    public void openCamera(View View){
        //Intent intent = new Intent(this, camera.class);
        //startActivity (intent);
    }

    public void createSudoku(View View){
        Intent intent = new Intent(this, CreateSudokuActivity.class);
        startActivity (intent);
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
