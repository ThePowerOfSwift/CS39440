package com.cs39440.rob41.sudokuapp;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Prommy on 06/03/2017.
 */
public class GameBoard {
    private static int gridSize;
    private static Cell [][] gameCells;
    private static Points [][] OverlayOfRegions;
    private int setTracker = 0;

    public static Cell getCell(int x, int y){
        return gameCells[x][y];
    }

    public GameBoard(int startingValues[], int passedGridSize) {
        Log.d("board","CREATING GAME BOARD");
        gridSize = passedGridSize;
        gameCells = new Cell [gridSize][gridSize];
        OverlayOfRegions = new Points [gridSize][gridSize];
        int region = 0;
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
                0,7,4,0,0,6,0,0,5,0};
        int [] sudoku ={
                0,0,0,5,0,0,0,0,0,
                0,0,0,1,0,0,0,0,0,
                0,0,0,0,0,0,0,0,0,
                0,0,0,0,0,0,0,0,0,
                0,0,0,0,0,0,0,0,0,
                0,0,0,0,0,0,0,0,0,
                0,0,0,0,0,0,0,0,0,
                0,0,0,0,0,0,0,0,0,
                0,0,0,0,0,0,0,0,0};*/

        int tracker = 0;
        for(int y=0; y<gridSize; y++){
            for(int x=0; x<gridSize; x++){
                gameCells[x][y] = new Cell(region,startingValues[tracker],x,y);
                tracker++;
                
                Log.d("boardfor","loop: "+x+","+y+"; region: "+region);
                if (x == 2 || x == 5){
                    region++;
                }
                if (x==8){
                    region = region-2;
                }
            }
            if (y == 2 || y == 5){
                region = region +3;
            }
        }
        createOverlayOfRegions(0,0,3,6);
        Log.d("board","Finished creating GAME BOARD");

    }

    //region Solving Algorithms

    public static void solve() {
        constraintSolve();
        backtrackingSolve(GameBoard.findBestCell());
    }

    public static void constraintSolve(){
        boolean simpleSolveWorking = true;
        int loopcounter = 0;
        while (simpleSolveWorking){
            checkRow();
            checkColumn();
            checkRegions();
            simpleSolveWorking = isSolved();
            loopcounter++;
            if (simpleSolveWorking == false){
                Log.d("Not Solved ","Needs Backtracking");
            }
        }
        Log.d("solved in:",loopcounter+" EXITING SOLVE");
    }


    public static boolean backtrackingSolve(Cell cell){
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

    private static boolean validValue(Integer possvalue, Cell cell) {
        boolean isValid = true;
        if(!checkColumnCells(cell.getY(),possvalue) || !checkRowCells(cell.getX(),possvalue) ||
                !checkRegionCells(cell.getX(), cell.getY(), possvalue)){
            isValid = false;
        }
        return isValid;
    }

    //Finds the cell with the fewest possible values
    public static Cell findBestCell(){
        //Log.d("Finding Cell"," Start");
        Cell bestOption = null;
        //Loop over the entire board
        for (int row = 0; row < gridSize ; row++) {
            for (int column = 0; column < gridSize; column++) {
                int numValues = gameCells[row][column].getPossValues().size();
                //Only check celsl we don't have an answer for
                if (gameCells[row][column].getAnswerValue() == 0){
                    //This will be the first cell without an answer we find
                    if (bestOption == null){
                        bestOption = gameCells[row][column];
                    }
                    //Check best so far with the current cell
                    if (numValues < bestOption.getPossValues().size()) {
                        bestOption = gameCells[row][column];
                    }
                }
            }
        }
        if (bestOption != null) {
            Log.d("Finding Cell", " x:" + bestOption.getX() + " y:" + bestOption.getY()+ " size:" + bestOption.getPossValues().size());
        }
        return bestOption;
    }

    //Check every cell as an assigned answer value
    private static boolean isComplete(){
        for (int row = 0; row < gridSize ; row++) {
            for (int column = 0; column < gridSize; column++) {
                if (gameCells[row][column].getAnswerValue() == 0){
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean checkColumnCells(int y, int value) {
        for (int row = 0; row < gridSize ; row++) {
            if (gameCells[row][y].getAnswerValue() == value){
                return false;
            }
        }
        return true;
    }

    private static boolean checkRowCells(int x, int value) {
        for (int column = 0; column < gridSize; column++){
            if (gameCells[x][column].getAnswerValue() == value){
                return false;
            }
        }
        return true;
    }

    private static boolean checkRegionCells(int x, int y, int value) {
        //optimize so stops after finding 9 cells
        for (int row = 0; row < gridSize ; row++) {
            for (int column = 0; column < gridSize; column++){
                if (gameCells[row][column].getRegion() == gameCells[x][y].getRegion()){
                    if (gameCells[row][column].getAnswerValue() == value){
                        return false;
                    }
                }
            }
        }
        return true;
    }


    private static void checkColumn() {
        Log.d("column","checkColumn");
        ArrayList<Integer> excludedValues = new ArrayList<>();
        for (int column = 0; column < gridSize ; column++) {
            excludedValues.clear();
            for (int counter = 0; counter < gridSize; counter++) {
                excludedValues.add(updateExcludedValues(counter,column));
            }
            for (int counter = 0; counter < gridSize; counter++) {
                if (gameCells[counter][column].getStartValue() == 0) {
                    gameCells[counter][column].updatePossValues(excludedValues);
                }
            }
        }
    }

    private static void checkRow() {
        Log.d("row","checkRow");
        ArrayList<Integer> excludedValues = new ArrayList<>();
        for (int row = 0; row < gridSize ; row++) {
            excludedValues.clear();
            for (int counter = 0; counter < gridSize; counter++) {
                excludedValues.add(updateExcludedValues(row,counter));
            }
            for (int counter = 0; counter < gridSize; counter++) {
                if (gameCells[row][counter].getStartValue() == 0) {
                    gameCells[row][counter].updatePossValues(excludedValues);
                }
            }
        }
    }

    private static void checkRegions() {
        Log.d("set","checkRegions");
        ArrayList<Integer> excludedValues = new ArrayList<>();
        for (int regionCounter = 0; regionCounter < gridSize ; regionCounter++){
            for (int pntCounter = 0; pntCounter < gridSize ; pntCounter++){
                int x = OverlayOfRegions[regionCounter][pntCounter].getX();
                int y = OverlayOfRegions[regionCounter][pntCounter].getY();
                excludedValues.add(updateExcludedValues(x,y));
            }
            for (int pntCounter = 0; pntCounter < gridSize ; pntCounter++){
                int x = OverlayOfRegions[regionCounter][pntCounter].getX();
                int y = OverlayOfRegions[regionCounter][pntCounter].getY();
                if (gameCells[x][y].getStartValue() == 0){
                    gameCells[x][y].updatePossValues(excludedValues);
                }
            }
            excludedValues.clear();
        }
    }

    private static int updateExcludedValues(int row, int column){
        if (gameCells[row][column].getAnswerValue() != 0) {
            return gameCells[row][column].getAnswerValue();
        }
        return 0;
    }

    //This Method requires so that we dont have to loop over all cells to find one set
    //using the TopLeft point of each set recursively check that set then move on
    private void createOverlayOfRegions(int startX, int startY, int add, int max) {
        int pointInSetNum = 0;
        for(int y = startY; y <= (max+startY); y = y + add) {
            for (int x = startX; x <= (max+startX); x = x + add) {
                //Check this specific set
                if(add != 3){
                    OverlayOfRegions[setTracker][pointInSetNum] = new Points(x,y);
                    gameCells[x][y].setRegion(setTracker);
                    pointInSetNum++;
                //Move on to next set
                }else{
                    createOverlayOfRegions(x,y,1,2);
                    setTracker++;
                }
            }
        }

    }

    private static boolean isSolved() {
        //check the arraylist of each cell and update
        boolean changeMade = false;
        for(int y=0; y<gridSize; y++){
            for(int x=0; x<gridSize; x++) {
                if( gameCells[x][y].getPossValues().size() > 1){
                    Log.d("isSolved","Arraylist still has >1 values");

                }else if ( gameCells[x][y].getPossValues().size() == 1){
                    //Set the Answer value to the remaining value in the arraylist

                    gameCells[x][y].setAnswerValue(gameCells[x][y].getPossValues().remove(0));
                    //gameCells[x][y].setAnswerValue(gameCells[x][y].getPossValues().get(0));
                    changeMade = true;
                }
            }
        }
        return changeMade;
    }

    public void consolesPrint() {
        System.out.println("----------------------");
        for(int y=0; y<gridSize; y++){
            System.out.print("|");
            for(int x=0; x<gridSize; x++) {
                System.out.print(gameCells[x][y].getAnswerValue() + " ");
                if (x==2|| x==5||x==8){
                    System.out.print("|");
                }
            }
            if (y==2|| y==5||y==8){
                System.out.println("");
                System.out.println("----------------------");
            }else{
                System.out.println("");
            }

        }
    }
}
