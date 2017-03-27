package com.cs39440.rob41.sudokuapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Random;

public class LoadingActivity extends Activity {
    final int gridSize = 9;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        Log.d("test1","LOADINGACTIVITY");
        //Retrive the additional data added to the intent
        Intent passedIntent = getIntent();
        boolean fromImage = passedIntent.getBooleanExtra("fromImage",false);
        GameBoard board = null;
        if (fromImage){
            Log.d("fromImage:",String.valueOf(fromImage));
            //GameBoard board = new GameBoard(createFromImage(),gridSize);
        }else{
            Log.d("fromImage:",String.valueOf(fromImage));
            board = new GameBoard(createFromAlgorithm(),gridSize);
        }
        board.solve();
        board.consolesPrint();




        Intent intent = new Intent(this, CreateSudokuActivity.class);
        intent.putExtra("fromImage",true);
        startActivity (intent);
        Log.d("test2","LOADINGACTIVITY - - - - CLOSING");
        finish();
    }

    private int[] createFromAlgorithm() {
        int numCells = gridSize*gridSize;
        int answers []  = new int [numCells];
        Arrays.fill(answers,0);

        Random random = new Random();
        for(int counter = 1; counter < 10; counter++){
            boolean numPlaced = false;
            while(!numPlaced){
                int randomNum = random.nextInt(numCells);
                if (answers[randomNum] == 0){
                    answers[randomNum] = counter;
                    numPlaced = true;
                }
            }
        }
        return answers;
    }

    private void createFromImage() {
    }
}
