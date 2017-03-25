package com.cs39440.rob41.sudokuapp;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class CreateSudokuActivity extends Activity implements View.OnFocusChangeListener {
    GridLayout gridLayout;
    boolean drawNums = true;
    int [][] gameData = new int [9][9];
    Map <Integer, Points> cellLookUpTable = new HashMap<Integer, Points>();
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
        int cellSize = gridSize/9;
        int count = 0;

        for(int yPos=0; yPos<numOfRow; yPos++){
            for(int xPos=0; xPos<numOfCol; xPos++){
                int gridValue = GameBoard.getInstance().getCell(xPos,yPos).getStartValue();

                EditText cellText = new EditText(this);
                cellText.setFilters(new InputFilter[] {new InputFilter.LengthFilter(1)});
                cellText.setInputType(InputType.TYPE_CLASS_NUMBER);
                //Disable the cursor and PointIcon as causes issues
                cellText.setCursorVisible(false);
                cellText.setLongClickable(false);
                //Required for the text to center correctly
                cellText.setPadding(0,0,0,0);
                //Set the ID
                ///Log.d("cellValue",String.valueOf(count));
                cellText.setId(count);
                //Formatting of the text
                cellText.setTextSize(TypedValue.COMPLEX_UNIT_SP,24);
                cellText.setTypeface(null,Typeface.BOLD);
                cellText.setGravity(Gravity.CENTER);
                //Formatting of the cell
                cellText.setBackgroundColor(Color.TRANSPARENT);
                cellText.setWidth(cellSize);
                cellText.setHeight(cellSize);

                //If it already has a set value make it unchangeable / Different color
                if (gridValue != 0) {
                    cellText.setText(String.valueOf(gridValue));
                    //Colour to Grey
                    cellText.setTextColor(Color.parseColor("#111111"));
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

                cellLookUpTable.put(count,new Points(xPos,yPos));
                gridLayout.addView(cellText);
                count++;
            }
        }
    }

    private void validateInput(View editTextCell){
        EditText cellText = (EditText) editTextCell;
        if (!cellText.getText().toString().isEmpty()){
            int cellValue = Integer.parseInt(cellText.getText().toString());
            if(cellValue >= 1 && cellValue <=9){
                //using ID lookup the XY coordinates
                Points cellRef= cellLookUpTable.get(cellText.getId());
                int x = cellRef.getX();
                int y = cellRef.getY();
                //update gameboard cell with uservalue
                GameBoard.getInstance().getCell(x, y).setUserAssignedValue(cellValue);
            }else{
                //Error toast and reset to ""
                Toast.makeText(getApplicationContext(),"Please use numbers between 1-9" ,
                        Toast.LENGTH_SHORT).show();
                cellText.setText("");
            }
        }
    }

    private OnClickListener cellValueChanged = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d("OnClick ","CALLED");
            validateInput(v);
        }
    };

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            v.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.cellshapeinner,null));
        }
        if (oldFocus != null){
            oldFocus.setBackgroundColor(Color.TRANSPARENT);
            validateInput(oldFocus);
        }
        oldFocus = v;
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
        for(int count = 0; count < cellLookUpTable.size(); count++){
            Points cellRef= cellLookUpTable.get(count);
            int x = cellRef.getX();
            int y = cellRef.getY();
            int answerValue = GameBoard.getInstance().getCell(x, y).getAnswerValue();
            int userValue = GameBoard.getInstance().getCell(x, y).getUserAssignedValue();
            int cellId = getResources().getIdentifier(String.valueOf(count), "id", getPackageName());
            EditText cellText = (EditText)findViewById(cellId);

            //Log.d("celltextVal",String.valueOf(cellText.getText())+" ID:"+cellId);
            cellText.setText(String.valueOf(answerValue));
            if (answerValue == userValue){
                cellText.setTextColor(Color.parseColor("#105e07"));
            }else if (answerValue != userValue && userValue != 0){
                cellText.setTextColor(Color.parseColor("#8c1500"));
            }
        }
    }
}
