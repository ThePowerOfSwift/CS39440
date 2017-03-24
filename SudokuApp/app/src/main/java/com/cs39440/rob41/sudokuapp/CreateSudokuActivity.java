package com.cs39440.rob41.sudokuapp;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CreateSudokuActivity extends Activity {
    GridLayout gridLayout;
    boolean drawNums = true;
    int [][] gameData = new int [9][9];
    List<TextView> textCells = new ArrayList<TextView>();
    View oldFocus = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_sudoku);
        gridLayout = (GridLayout)findViewById(R.id.sudokuGrid);

        /*int count = 0;
        for(int y=0; y<9; y++){
            for(int x=0; x<9; x++){
                gameData[x][y] = count;
                count++;
            }
        }*/
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
        Log.d("gridSize",String.valueOf(gridSize));
        int numOfCol = gridLayout.getColumnCount();
        int numOfRow = gridLayout.getRowCount();
        int cellsize = gridSize/9;
        int count = 0;

        for(int yPos=0; yPos<numOfRow; yPos++){
            for(int xPos=0; xPos<numOfCol; xPos++){

                //int setValue = GameBoard.getInstance().getCell(xPos,yPos).getSet();
                int gridValue = GameBoard.getInstance().getCell(xPos,yPos).getStartValue();

                EditText cellText = new EditText(this);
                cellText.setFilters(new InputFilter[] {new InputFilter.LengthFilter(2)});
                cellText.setInputType(InputType.TYPE_CLASS_NUMBER);
                cellText.setCursorVisible(false);
                cellText.setLongClickable(false);
                //Required for the text to center correctly
                textCells.add(cellText);

                String cellId = String.valueOf(yPos)+String.valueOf(xPos);
                Log.d("cellValue",String.valueOf(count));
                cellText.setId(count);

                cellText.setTextSize(TypedValue.COMPLEX_UNIT_SP,24);
                cellText.setTypeface(null,Typeface.BOLD);

                //If it already has a set value e.g. unchangeable
                if (gridValue != 0) {
                    cellText.setText(String.valueOf(gridValue));
                    //Colour to Grey
                    cellText.setTextColor(Color.parseColor("#777777"));
                    cellText.setFocusable(false);
                }else{
                    cellText.setText(String.valueOf(" "));
                }
                cellText.setBackgroundColor(0x07000000);
                cellText.setWidth(cellsize);
                cellText.setHeight(cellsize);
                cellText.setGravity(Gravity.CENTER);

                cellText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) {
                            v.setBackground(getResources().getDrawable(R.drawable.cellshape));
                        }
                        if (oldFocus != null){
                            oldFocus.setBackgroundColor(Color.TRANSPARENT);
                        }
                        oldFocus = v;
                    }

                });

                gridLayout.addView(cellText);
                count++;
            }
        }
    }

    //Returns the width of the grid image
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
        //Colour to Green
        //cellText.setTextColor(Color.parseColor("#105e07"));
        //Colour to Red
        //cellText.setTextColor(Color.parseColor("#8c1500"));
    }

    public void displaySolution(){
        for(int i=0; i < textCells.size(); i++){
            textCells.get(i).getId();
            Log.d("cellValue",String.valueOf(textCells.get(i).getId()));
            //string[i] = allEds.get(i).getText().toString();
        }
    }
}
