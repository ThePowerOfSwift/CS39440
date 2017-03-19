package com.cs39440.rob41.sudokuapp;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CreateSudokuActivity extends Activity {
    GridLayout gridLayout;
    boolean drawNums = true;
    int [][] gameData = new int [9][9];
    List<TextView> textCells = new ArrayList<TextView>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_sudoku);
        gridLayout = (GridLayout)findViewById(R.id.sudokuGrid);

        int count = 0;
        for(int y=0; y<9; y++){
            for(int x=0; x<9; x++){
                gameData[x][y] = count;
                count++;
            }
        }
    }

    @Override
    public void onWindowFocusChanged (boolean hasFocus){
        //Prevents multiple calls to redraw
        if (drawNums){
            drawNumbers();
            drawNums = false;
        }
    }

    public void drawNumbers(){
        int gridSize = updateGridSize();
        gridLayout = (GridLayout)findViewById(R.id.sudokuGrid);

        int numOfCol = gridLayout.getColumnCount();
        int numOfRow = gridLayout.getRowCount();
        int cellsize = gridSize/9;

        for(int yPos=0; yPos<numOfRow; yPos++){
            for(int xPos=0; xPos<numOfCol; xPos++){

                //int setValue = GameBoard.getInstance().getCell(xPos,yPos).getSet();
                int gridValue = GameBoard.getInstance().getCell(xPos,yPos).getStartValue();

                TextView cellText = new TextView(this);
                textCells.add(cellText);

                String cellId = String.valueOf(yPos)+String.valueOf(xPos);
                Log.d("cellValue",String.valueOf(Integer.parseInt(cellId)));
                cellText.setId(Integer.parseInt(cellId));

                cellText.setTextSize(TypedValue.COMPLEX_UNIT_SP,24);
                cellText.setTypeface(null,Typeface.BOLD);
                if (gridValue != 0) {
                    cellText.setText(String.valueOf(gridValue));
                }else{
                    cellText.setText(String.valueOf(" "));
                }
                cellText.setBackgroundColor(0x07000000);
                cellText.setWidth(cellsize);
                cellText.setHeight(cellsize);
                cellText.setGravity(Gravity.CENTER);
                gridLayout.addView(cellText);
            }
        }
    }

    public int updateGridSize(){
        gridLayout = (GridLayout)findViewById(R.id.sudokuGrid);
        int gridWidth = gridLayout.getWidth();
        int gridHeight = gridLayout.getHeight();
        int newXY = Math.min(gridWidth,gridHeight);

        ViewGroup.LayoutParams layoutParams = gridLayout.getLayoutParams();
        layoutParams.height = newXY;
        layoutParams.width = newXY;
        gridLayout.setLayoutParams(layoutParams);
        return newXY;
    }
    public void closeActivity(View view) {
        finish();
    }

    public void SolveSudoku(View view) {
        GameBoard.getInstance().solve();
        displaySolution();
    }

    public void displaySolution(){
        for(int i=0; i < textCells.size(); i++){
            textCells.get(i).getId();
            Log.d("cellValue",String.valueOf(textCells.get(i).getId()));
            //string[i] = allEds.get(i).getText().toString();
        }
    }

}
