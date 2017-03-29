package com.cs39440.rob41.sudokuapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.Arrays;
import java.util.Random;

public class LoadingActivity extends Activity {
    private final int gridSize = 9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        Log.d("test1","LOADINGACTIVITY");
        //Retrive the additional data added to the intent
        Intent passedIntent = getIntent();
        boolean fromImage = passedIntent.getBooleanExtra("fromImage",false);

        if (fromImage){
            Log.d("fromImage:",String.valueOf(fromImage));
            //GameBoard board = new GameBoard(createFromImage(),gridSize);
        }else{
            Log.d("fromImage:",String.valueOf(fromImage));
            createFromAlgorithm();
        }
        GameBoard.getInstance().consolesPrint();

        Intent intent = new Intent(this, CreateSudokuActivity.class);
        intent.putExtra("fromImage",true);
        startActivity (intent);
        Log.d("test2","LOADINGACTIVITY - - - - CLOSING");
        finish();
    }

    private void createFromAlgorithm() {
        //SAMPLE SUDOKU
        /*int [] sudoku ={
                8,0,0,5,0,0,3,2,0,
                7,0,3,1,0,0,4,0,0,
                1,2,0,0,0,9,0,0,8,
                6,5,0,0,9,3,0,8,0,
                0,9,0,0,0,0,0,1,0,
                0,8,0,6,2,0,0,9,3,
                2,0,0,4,0,0,0,6,7,
                0,0,6,0,0,8,1,0,2,
                0,7,4,0,0,6,0,0,5,0};*/

        int numCells = gridSize*gridSize;
        int cellValues []  = new int [numCells];
        Arrays.fill(cellValues,0);

        Random random = new Random();
        for(int counter = 1; counter < 10; counter++){
            boolean numPlaced = false;
            while(!numPlaced){
                int randomNum = random.nextInt(numCells);
                if (cellValues[randomNum] == 0){
                    cellValues[randomNum] = counter;
                    numPlaced = true;
                }
            }
        }
        GameBoard.getInstance().createCells(cellValues);
        GameBoard.getInstance().solve();
        GameBoard.getInstance().addVisibleCells();
    }

    private void createFromImage() {
    }
}
