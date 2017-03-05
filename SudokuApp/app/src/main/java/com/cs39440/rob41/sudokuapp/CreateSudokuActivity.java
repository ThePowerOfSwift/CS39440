package com.cs39440.rob41.sudokuapp;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

public class CreateSudokuActivity extends Activity {
    //GridView gridView;
    GridLayout gridLayout;
    boolean drawNums = true;
    int [][] gameData = new int [9][9];

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



//        gridView = (GridView)findViewById(R.id.sudokuGrid);
//        gridView.setAdapter(new SudokuGrid(this,basic));
//
//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getApplicationContext(),
//                        ((TextView) view.findViewById(R.id.cellText)
//                        ).getText(), Toast.LENGTH_SHORT).show();
//            }
//        });
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

                TextView cellText = new TextView(this);
                cellText.setTextSize(TypedValue.COMPLEX_UNIT_SP,24);
                cellText.setTypeface(null,Typeface.BOLD);
                cellText.setText(String.valueOf(gameData[xPos][yPos]));
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


}
