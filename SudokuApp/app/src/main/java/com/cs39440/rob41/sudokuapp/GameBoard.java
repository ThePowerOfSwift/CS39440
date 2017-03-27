package com.cs39440.rob41.sudokuapp;

import android.graphics.Point;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;

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

    public Cell getCell(int x, int y){
        return gameBoard[x][y];
    }

    private GameBoard() {
        Log.d("board","CREATING GAME BOARD SINGLETON");
        int set = 0;
        //SAMPLE SUDOKU
        /*int [] sudoku ={
                8,0,0,5,0,0,3,2,0,
                7,0,0,1,0,0,4,0,0,
                1,2,0,0,0,9,0,0,8,
                6,5,0,0,9,3,0,8,0,
                0,9,0,0,0,0,0,1,0,
                0,8,0,6,2,0,0,9,3,
                2,0,0,4,0,0,0,6,7,
                0,0,0,0,0,8,1,0,2,
                0,7,4,0,0,6,0,0,5,0};*/
        int [] sudoku ={
                0,0,0,5,0,0,0,0,0,
                0,0,0,1,0,0,0,0,0,
                0,0,0,0,0,0,0,0,0,
                0,0,0,0,0,0,0,0,0,
                0,0,0,0,0,0,0,0,0,
                0,0,0,0,0,0,0,0,0,
                0,0,0,0,0,0,0,0,0,
                0,0,0,0,0,0,0,0,0,
                0,0,0,0,0,0,0,0,0};

        int tracker = 0;

        for(int y=0; y<gridSize; y++){
            for(int x=0; x<gridSize; x++){
                gameBoard[x][y] = new Cell(set,sudoku[tracker],x,y);
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

    //region Solving Algorithms
    public void constraintSolve(){
        boolean simpleSolveWorking = true;
        int loopcounter = 0;
        while (simpleSolveWorking){
            checkRow();
            checkColumn();
            checkSets();
            simpleSolveWorking = isSolved();
            loopcounter++;
            //THIS NEEDS SORTING WTF PREVIOUS ME!
            if (simpleSolveWorking == false){
                Log.d("Not Solved ","Exiting");
            }
        }
        Log.d("solved in:",loopcounter+" EXITING SOLVE");
    }


    public boolean backtrackingSolve(Cell cell){
                //For each possible values of the cell
        for (Integer possvalue : cell.getPossValues()){
            //if validated successfully
            if(validValue(possvalue,cell) == true) {
                //assign Value to the answer value of the cell
                cell.setAnswerValue(possvalue);
                //Check if this completes the Sudoku
                if (isComplete()){
                    Log.d("backtracking ","complete");
                    return true;
                }
                //find next cell and recurse, if successful return True
                if (backtrackingSolve(findBestCell()) == true) {
                    return true;
                    //Reset answerValue and try next
                } else {
                    cell.setAnswerValue(0);
                }
            }
        }
        return false;
    }
    //endregion Solving Algorithms

    private boolean validValue(Integer possvalue, Cell cell) {
        boolean isValid = true;
        if(!checkColumnCells(cell.getY(),possvalue) || !checkRowCells(cell.getX(),possvalue) ||
                !checkSetCells(cell.getX(), cell.getY(), possvalue)){
            isValid = false;
        }
        return isValid;
    }

    //Finds the cell with the fewest possible values
    public Cell findBestCell(){
        Log.d("Finding Cell"," Start");
        Cell bestOption = null;
        //Loop over the entire board
        for (int row = 0; row < gridSize ; row++) {
            for (int column = 0; column < gridSize; column++) {
                int numValues = gameBoard[row][column].getPossValues().size();
                //Only check celsl we don't have an answer for
                if (gameBoard[row][column].getAnswerValue() == 0){
                    //This will be the first cell without an answer we find
                    if (bestOption == null){
                        bestOption = gameBoard[row][column];
                    }
                    //Check best so far with the current cell
                    if (numValues < bestOption.getPossValues().size()) {
                        bestOption = gameBoard[row][column];
                    }
                }
            }
        }
        if (bestOption != null) {
            Log.d("Finding Cell", "cell x:" + bestOption.getX() + " y:" + bestOption.getY());
        }
        return bestOption;
    }

    //Check every cell as an assigned answer value
    private boolean isComplete(){
        for (int row = 0; row < gridSize ; row++) {
            for (int column = 0; column < gridSize; column++) {
                if (gameBoard[row][column].getAnswerValue() == 0){
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkColumnCells(int y, int value ) {
        for (int row = 0; row < gridSize ; row++) {
            if (gameBoard[row][y].getAnswerValue() == value){
                return false;
            }
        }
        return true;
    }

    private boolean checkRowCells(int x, int value) {
        for (int column = 0; column < gridSize; column++){
            if (gameBoard[x][column].getAnswerValue() == value){
                return false;
            }
        }
        return true;
    }

    private boolean checkSetCells(int x, int y, int value) {
        //optimize so stops after finding 9 cells
        for (int row = 0; row < gridSize ; row++) {
            for (int column = 0; column < gridSize; column++){
                if (gameBoard[row][column].getRegion() == gameBoard[x][y].getRegion()){
                    if (gameBoard[row][column].getAnswerValue() == value){
                        return false;
                    }
                }
            }
        }
        return true;
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
    }

    private int updateExcludedValues(int row, int column){
        if (gameBoard[row][column].getAnswerValue() != 0) {
            return gameBoard[row][column].getAnswerValue();
        }
        return 0;
    }

    //This Method requires so that we dont have to loop over all cells to find one set
    //using the TopLeft point of each set recursively check that set then move on
    private void createOverlayOfSets(int startX, int startY, int add, int max) {
        int pointInSetNum = 0;
        for(int y = startY; y <= (max+startY); y = y + add) {
            for (int x = startX; x <= (max+startX); x = x + add) {
                //Check this specific set
                if(add != 3){
                    OverlayOfSets[setTracker][pointInSetNum] = new Points(x,y);
                    gameBoard[x][y].setRegion(setTracker);
                    pointInSetNum++;
                //Move on to next set
                }else{
                    createOverlayOfSets(x,y,1,2);
                    setTracker++;
                }
            }
        }

    }

    private boolean isSolved() {
        //check the arraylist of each cell and update
        boolean changeMade = false;
        for(int y=0; y<gridSize; y++){
            for(int x=0; x<gridSize; x++) {
                if( gameBoard[x][y].getPossValues().size() > 1){
                    Log.d("isSolved","Arraylist still has >1 values");

                }else if ( gameBoard[x][y].getPossValues().size() == 1){
                    //Set the Answer value to the remaining value in the arraylist

                    gameBoard[x][y].setAnswerValue(gameBoard[x][y].getPossValues().remove(0));
                    //gameBoard[x][y].setAnswerValue(gameBoard[x][y].getPossValues().get(0));
                    changeMade = true;
                }
            }
        }
        return changeMade;
    }
}
