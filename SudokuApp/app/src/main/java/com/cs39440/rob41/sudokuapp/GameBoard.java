package com.cs39440.rob41.sudokuapp;

import android.graphics.Point;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Prommy on 06/03/2017.
 */
public class GameBoard {
    private static GameBoard ourInstance = new GameBoard();
    private int gridSize = 9;
    private Cell [][] gameBoard = new Cell [gridSize][gridSize];
    private Points [][] OverlayOfSets = new Points [gridSize][gridSize];
    private int setTracker = 0;

    public static GameBoard getInstance() {
        return ourInstance;
    }

    private GameBoard() {
        Log.d("board","CREATING GAME BOARD SINGLETON");
        int set = 0;
        //SAMPLE SUDOKU
        int [] sudoku ={
                8,0,0,5,0,0,3,2,0,
                7,0,3,1,0,0,4,0,0,
                1,2,0,0,0,9,0,0,8,
                6,5,0,0,9,3,0,8,0,
                0,9,0,0,0,0,0,1,0,
                0,8,0,6,2,0,0,9,3,
                2,0,0,4,0,0,0,6,7,
                0,0,6,0,0,8,1,0,2,
                0,7,4,0,0,6,0,0,5,0};
        int tracker = 0;

        for(int y=0; y<gridSize; y++){
            for(int x=0; x<gridSize; x++){
                gameBoard[x][y] = new Cell(set,sudoku[tracker]);
                tracker++;
                
                //Log.d("boardfor","loop: "+x+"; set: "+set);
                if (x == 2 || x == 5){
                    set++;
                }
                if (x==8){
                    set = set-2;
                }
            }
            if (y == 2 || y == 5){
                set=set+3;
            }
        }
        createOverlayOfSets(0,0,3,6);
        Log.d("board","Finished GAME BOARD SINGLETON");

    }

    public void solve(){
        boolean solutionFound = false;
        int loopcounter = 0;
        while (!solutionFound){
            checkRow();
            checkColumn();
            checkSets();
            solutionFound = isSolved();
            loopcounter++;
            if (loopcounter == 10){
                solutionFound = true;
                Log.d("Not Solved","Exit - 10 loops");
            }
        }
        Log.d("solved in:",loopcounter+" EXITING SOLVE");
    }



    private void checkColumn() {
        Log.d("column","checkColumn");
        ArrayList<Integer> excludedValues = new ArrayList<>();
        for (int column = 0; column < gridSize ; column++) {
            excludedValues.clear();
            for (int counter = 0; counter < gridSize; counter++) {
                excludedValues.add(updateExcludedValues(counter,column));
            }
            for (int counter = 0; counter < gridSize; counter++) {
                if (gameBoard[counter][column].getStartValue() == 0) {
                    gameBoard[counter][column].updatePossValues(excludedValues);
                }
            }
        }
    }

    private int updateExcludedValues(int row, int column){
        if (gameBoard[row][column].getAnswerValue() != 0) {
            return gameBoard[row][column].getAnswerValue();
        }
        return 0;
    }


    private void checkRow() {
        Log.d("row","checkRow");
        ArrayList<Integer> excludedValues = new ArrayList<>();
        for (int row = 0; row < gridSize ; row++) {
            excludedValues.clear();
            for (int counter = 0; counter < gridSize; counter++) {
                excludedValues.add(updateExcludedValues(row,counter));
            }
            for (int counter = 0; counter < gridSize; counter++) {
                if (gameBoard[row][counter].getStartValue() == 0) {
                    gameBoard[row][counter].updatePossValues(excludedValues);
                }
            }
        }
    }

    private void checkSets() {
        Log.d("set","checkSets");
        ArrayList<Integer> excludedValues = new ArrayList<>();
        for (int setCounter = 0; setCounter < gridSize ; setCounter++){
            for (int pntCounter = 0; pntCounter < gridSize ; pntCounter++){
                int x = OverlayOfSets[setCounter][pntCounter].getX();
                int y = OverlayOfSets[setCounter][pntCounter].getY();
                excludedValues.add(updateExcludedValues(x,y));
            }
            for (int pntCounter = 0; pntCounter < gridSize ; pntCounter++){
                int x = OverlayOfSets[setCounter][pntCounter].getX();
                int y = OverlayOfSets[setCounter][pntCounter].getY();
                if (gameBoard[x][y].getStartValue() == 0){
                    gameBoard[x][y].updatePossValues(excludedValues);
                }
            }
            excludedValues.clear();
        }
        Log.d("set","EXITcheckSets");
    }

    //using the TopLeft point of each set recursively check that set then move on
    private void createOverlayOfSets(int startX, int startY, int add, int max) {
        int pointInSetNum = 0;

        for(int y = startY; y <= (max+startY); y = y + add) {
            for (int x = startX; x <= (max+startX); x = x + add) {
                //Check this specific set
                if(add != 3){
                    //Log.d("set",String.valueOf(gameBoard[x][y].getRegion()));

                    OverlayOfSets[setTracker][pointInSetNum] = new Points(x,y);
                    pointInSetNum++;

                    //gameBoard[x][y].getStartValue();
                //Move on to next set
                }else{
                    //Log.d("ELSE","ENTER  y:"+ String.valueOf(y)+"  x:"+String.valueOf(x));
                    createOverlayOfSets(x,y,1,2);
                    setTracker++;
                    //Log.d("ELSE","EXIT");
                }
            }
        }
    }

    private boolean isSolved() {
        //check the arraylist of each cell and update
        boolean solutionFound = true;
        for(int y=0; y<gridSize; y++){
            for(int x=0; x<gridSize; x++) {
                if( gameBoard[x][y].getPossValues().size() > 1){
                    Log.d("isSolved","Arraylist still has >1 values");
                    solutionFound = false;
                }else if ( gameBoard[x][y].getPossValues().size() == 1){
                    //Set the Answer value to the remaining value in the arraylist
                    gameBoard[x][y].setAnswerValue(gameBoard[x][y].getPossValues().get(0));

                    //THIS LINE NEEDS TO BE REMOVED AND "startValue" is cell should be final
                    //gameBoard[x][y].setStartValue(gameBoard[x][y].getPossValues().get(0));
                }
            }
        }
        return solutionFound;
    }

    public Cell getCell(int x, int y){
        return gameBoard[x][y];
    }
}
