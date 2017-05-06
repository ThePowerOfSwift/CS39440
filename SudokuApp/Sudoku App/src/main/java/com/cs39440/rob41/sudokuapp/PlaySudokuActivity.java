package com.cs39440.rob41.sudokuapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.Toast;
import java.util.HashMap;
import java.util.Map;

public class PlaySudokuActivity extends Activity implements View.OnFocusChangeListener {
    private GridLayout gridLayout;
    private View oldFocus = null;
    //Look-up table Int is editText id's allow the retrieval of X & Y coordinates in the Gameboard
    Map <Integer, Points> cellLookUpTable = new HashMap<Integer, Points>();
    //Ensures the grid is drawn after onCreate
    private boolean drawGrid = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_sudoku);
        gridLayout = (GridLayout)findViewById(R.id.sudokuGrid);
    }

    //region Android Listeners
    //Used to drawn the sudoku grid so it scales to the size of the display
    @Override
    public void onWindowFocusChanged (boolean hasFocus){
        //Prevents multiple calls to redraw
        if (drawGrid){
            createGrid();
            drawGrid = false;
        }
    }

    private OnClickListener cellValueChanged = new OnClickListener() {
        @Override
        public void onClick(View v) {
            //Log.d("OnClick ","CALLED");
            validateCellInput(v);
        }
    };

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            v.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.cellhighlightgrey,null));
        }
        if (oldFocus != null){
            oldFocus.setBackgroundColor(Color.TRANSPARENT);
            validateCellInput(oldFocus);
        }
        oldFocus = v;
    }
    //endregion

    //Draw the grid of edittext cells the user interacts withs
    private void createGrid(){
        int gridSize = updateGridSize();
        gridLayout = (GridLayout)findViewById(R.id.sudokuGrid);
        int numOfCol = gridLayout.getColumnCount();
        int numOfRow = gridLayout.getRowCount();
        int cellSize = gridSize/9;

        for(int yPos=0; yPos<numOfRow; yPos++){
            for(int xPos=0; xPos<numOfCol; xPos++){
                int id = (yPos*9)+xPos;
                boolean isStartValue = GameBoard.getInstance().getCell(xPos,yPos).getStartValue();
                LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService
                        (LAYOUT_INFLATER_SERVICE);
                EditText cellText = (EditText) inflater.inflate(R.layout.cell_edit_text,null);
                //Set the ID
                cellText.setId(id);
                //Size of the cell
                cellText.setWidth(cellSize);
                cellText.setHeight(cellSize);
                //Limit input to 1
                cellText.setFilters(new InputFilter[] {new InputFilter.LengthFilter(1)});
                //If it already has a set value make it unchangeable / Different color
                if (isStartValue) {
                    cellText.setText(String.valueOf
                            (GameBoard.getInstance().getCell(xPos,yPos).getAnswerValue()));
                    cellText.setFocusable(false);
                }else{
                    cellText.setText(String.valueOf(""));
                    cellText.setTextColor(Color.parseColor("#555555"));
                }

                //Add Listeners..
                // ...so when clicked on it's highlighted
                cellText.setOnFocusChangeListener(this);
                // ...so when datas entered it's valided and passed to the gameboard
                cellText.setOnClickListener(cellValueChanged);

                cellLookUpTable.put(id,new Points(xPos,yPos));
                gridLayout.addView(cellText);
            }
        }
    }

    private void validateCellInput(View editTextCell){
        EditText cellText = (EditText) editTextCell;
        //If the cell contains text
        if (!cellText.getText().toString().isEmpty()){
            int cellValue = Integer.parseInt(cellText.getText().toString());
            //If it contains a number between 1-9
            if(cellValue >= 1 && cellValue <=9){
                //Using ID lookup the XY coordinates
                Points cellRef= cellLookUpTable.get(cellText.getId());
                int x = cellRef.getX();
                int y = cellRef.getY();
                //Update gameboard cell with uservalue
                GameBoard.getInstance().getCell(x, y).setUserAssignedValue(cellValue);
            //If it doesn't contain a number 1-9 display an error toast and reset
            }else{
                Toast.makeText(getApplicationContext(),"Please use numbers between 1-9" ,
                        Toast.LENGTH_SHORT).show();
                cellText.setText("");
            }
        }
    }

    //Returns the Width/Height of the grid image
    private int updateGridSize(){
        gridLayout = (GridLayout)findViewById(R.id.sudokuGrid);
        int gridWidth = gridLayout.getWidth();
        int gridHeight = gridLayout.getHeight();
        //Find which is the shortest and set both to that size to ensure it's square
        int newXY = Math.min(gridWidth,gridHeight);
        ViewGroup.LayoutParams layoutParams = gridLayout.getLayoutParams();
        layoutParams.height = newXY;
        layoutParams.width = newXY;
        gridLayout.setLayoutParams(layoutParams);
        return newXY;
    }

    public void solveSudoku(View view) {
        //For each edit text find the corresponding cell in the Gameboard
        for(int count = 0; count < cellLookUpTable.size(); count++){
            Points cellRef = cellLookUpTable.get(count);
            int x = cellRef.getX();
            int y = cellRef.getY();
            //Get the users answer and correct answer
            int answerValue = GameBoard.getInstance().getCell(x, y).getAnswerValue();
            int userValue = GameBoard.getInstance().getCell(x, y).getUserAssignedValue();
            EditText cellText = (EditText)findViewById(count);
            //Update the view depending on users answers
            if (answerValue != 0){
                cellText.setText(String.valueOf(answerValue));
                //Set answers to green if user is correct
                if (answerValue == userValue){
                    cellText.setTextColor(Color.parseColor("#105e07"));
                    //Set answer to red if incorrect or unanswered
                }else if (userValue != 0){
                    cellText.setTextColor(Color.parseColor("#8c1500"));
                }
            }
        }
    }

    public void closeActivity(View view) {
        finish();
    }
}
