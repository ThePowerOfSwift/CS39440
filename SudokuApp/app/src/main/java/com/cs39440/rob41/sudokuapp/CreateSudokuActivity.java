package com.cs39440.rob41.sudokuapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class CreateSudokuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_sudoku);
    }

    public void closeActivity(View view) {
        finish();
    }
}
