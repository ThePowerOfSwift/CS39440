package com.cs39440.rob41.sudokuapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class LoadingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        Log.d("test1","LOADINGACTIVITY");
        //Retrive the additional data added to the intent
        Intent passedIntent = getIntent();
        boolean fromImage = passedIntent.getBooleanExtra("fromImage",false);
        if (fromImage){
            Log.d("fromImage",String.valueOf(fromImage));
        }else{
            Log.d("fromImage",String.valueOf(fromImage));
        }
        Intent intent = new Intent(this, CreateSudokuActivity.class);
        intent.putExtra("fromImage",true);
        startActivity (intent);
        Log.d("test2","LOADINGACTIVITY - - - - CLOSING");
        finish();
    }
}
