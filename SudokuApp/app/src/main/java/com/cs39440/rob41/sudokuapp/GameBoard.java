package com.cs39440.rob41.sudokuapp;

import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Prommy on 06/03/2017.
 */
public class GameBoard {
    private int gridSize = 9;
    private static GameBoard ourInstance = new GameBoard();
    private Cell [][] gameCells = new Cell [gridSize][gridSize];
    private Points [][] OverlayOfSets = new Points [gridSize][gridSize];
    private int setTracker = 0;

    public static GameBoard getInstance() {
        return ourInstance;
    }

    private GameBoard() {
        Log.d("board","CREATING GAME BOARD SINGLETON");
    }

    public void createCells(int[] cellValues) {
        int set = 0;
        int tracker = 0;

        for(int y=0; y<gridSize; y++){
            for(int x=0; x<gridSize; x++){
                gameCells[x][y] = new Cell(set,cellValues[tracker],x,y);
                tracker++;

                //Log.d("boardfor","loop: "+x+"; set: "+set);
                if (x == 2 || x == 5){set++;}
                if (x==8){set = set-2;}
            }
            if (y == 2 || y == 5){set = set+3;}
        }
        //createOverlayOfSets(0,0,3,6);
    }

    public void solve() {
        //constraintSolve();
        backtrackingSolve(findBestCell());
    }

    //Sets what the user will see in the display
    public void setVisibleCells(){
        int x = 0;
        int y = 1;
        int goalNumVisibleCells;
        int numVisibleCells = 9;
        int middle = 4;
        Random random = new Random();

        //Create a random Number between 30 - 36
        double randNumCells = random.nextInt(8)+30;
        //Round it to a even num
        randNumCells = Math.round(randNumCells/2)*2;
        System.out.println(randNumCells);
        goalNumVisibleCells = (int) randNumCells;
        Boolean firstloop = true;
        while(numVisibleCells <= goalNumVisibleCells ) {
            //if start value of either cell is !0 set other to visible and numVisibleCells++
            if (firstloop) {
                if (gameCells[middle - x][middle - y].getStartValue() != 0 ||
                        gameCells[middle + x][middle + y].getStartValue() != 0) {
                    gameCells[middle - x][middle - y].setStartValToAnsVal();
                    gameCells[middle + x][middle + y].setStartValToAnsVal();
                    //System.out.println("New cells (1) @x"+(middle - x)+",y"+(middle - y)+" AND @x"+(middle + x)+",y"+(middle + y));
                    numVisibleCells++;
                }
            }else{
            //if 10% chance
                random = new Random();
                randNumCells = random.nextInt(10);
                if (randNumCells == 0){
                    //System.out.println("New cells (2) @x"+(middle - x)+",y"+(middle - y)+" AND @x"+(middle + x)+",y"+(middle + y));
                    gameCells[middle - x][middle - y].setStartValToAnsVal();
                    gameCells[middle + x][middle + y].setStartValToAnsVal();
                    numVisibleCells = numVisibleCells+2;
                }
            }

            //Move to next cell
            y++;
            //to move to the next row (Starts on row 5, middle column)
            if(y == 5){
                y = -4;
                x++;
            }
            //To prevent out of bounds
            if (x == 5){
                x = 0;
                y = 1;
                firstloop = false;
            }
        }
    }

    private boolean backtrackingSolve(Cell cell){
        //For each possible values of the cell
        if (isComplete()){
            Log.d("backtracking ","complete");
            return true;
        }
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

    //Check that this Value has not conflicts in - Row / Column / Region
    private boolean validValue(Integer possvalue, Cell cell) {
        boolean isValid = true;
        if(!checkColumnCells(cell.getY(),possvalue) || !checkRowCells(cell.getX(),possvalue) ||
                !checkRegionCells(cell.getX(), cell.getY(), possvalue)){
            isValid = false;
        }
        return isValid;
    }

    //Finds the cell with the fewest possible values
    private Cell findBestCell(){
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
        /*
        if (bestOption != null) {
            Log.d("Finding Cell", " x:" + bestOption.getX() + " y:" + bestOption.getY()+ " size:" + bestOption.getPossValues().size());
        }
        */
        return bestOption;
    }

    //Check every cell as an assigned answer value
    private boolean isComplete(){
        for (int row = 0; row < gridSize ; row++) {
            for (int column = 0; column < gridSize; column++) {
                if (gameCells[row][column].getAnswerValue() == 0){
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkColumnCells(int y, int value) {
        for (int row = 0; row < gridSize ; row++) {
            if (gameCells[row][y].getAnswerValue() == value){
                return false;
            }
        }
        return true;
    }

    private boolean checkRowCells(int x, int value) {
        for (int column = 0; column < gridSize; column++){
            if (gameCells[x][column].getAnswerValue() == value){
                return false;
            }
        }
        return true;
    }

    private boolean checkRegionCells(int x, int y, int value) {
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



/*
    private void constraintSolve(){
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

    private int updateExcludedValues(int row, int column){
        if (gameCells[row][column].getAnswerValue() != 0) {
            return gameCells[row][column].getAnswerValue();
        }
        return 0;
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
                if (gameCells[counter][column].getStartValue() == 0) {
                    gameCells[counter][column].updatePossValues(excludedValues);
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
                if (gameCells[row][counter].getStartValue() == 0) {
                    gameCells[row][counter].updatePossValues(excludedValues);
                }
            }
        }
    }

    private void checkRegions() {
        Log.d("set","checkRegions");
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
                if (gameCells[x][y].getStartValue() == 0){
                    gameCells[x][y].updatePossValues(excludedValues);
                }
            }
            excludedValues.clear();
        }
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
        boolean changeMade = false;
        for(int y=0; y<gridSize; y++){
            for(int x=0; x<gridSize; x++) {
                if( gameCells[x][y].getPossValues().size() > 1){
                    //Log.d("isSolved","Arraylist still has >1 values");

                }else if ( gameCells[x][y].getPossValues().size() == 1){
                    //Set the Answer value to the remaining value in the arraylist

                    gameCells[x][y].setAnswerValue(gameCells[x][y].getPossValues().remove(0));
                    changeMade = true;
                }
            }
        }
        return changeMade;
    }
*/
    public Cell getCell(int x, int y){
        return gameCells[x][y];
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
