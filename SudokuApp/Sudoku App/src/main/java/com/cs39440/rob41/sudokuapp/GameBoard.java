package com.cs39440.rob41.sudokuapp;

import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Robert Bayliss on 02/03/2017.
 */

public class GameBoard {
    private static GameBoard instance = new GameBoard();
    private int gridSize = 9;
    private Cell [][] gameCells = new Cell [gridSize][gridSize];
    int count = 0;

    //Empty constructor
    private GameBoard() {
        //Log.d("board","CREATING GAME BOARD SINGLETON");
    }

    //Return this instance of the GameBoard
    public static GameBoard getInstance() {
        return instance;
    }

    //Returns a cell object
    public Cell getCell(int x, int y){
        return gameCells[x][y];
    }

    //Create the game starting cells based of the array passed in
    public void createCells(int[] cellValues) {
        int set = 0;
        int tracker = 0;
        for(int y = 0; y < gridSize; y++){
            for(int x = 0; x < gridSize; x++){
                gameCells[x][y] = new Cell(set,cellValues[tracker],x,y);
                tracker++;
                //Log.d("boardfor","loop: "+x+"; set: "+set);
                if (x == 2 || x == 5){set++;}
                if (x==8){set = set-2;}
            }
            if (y == 2 || y == 5){set = set+3;}
        }
    }

    //Solves all sudokus using contrain solve first then if required backtracking
    public void solve() {
        Log.d("Solving","Started");
        constraintSolve();
        consolesPrint();
        //If not completed use backtracking
        if (!isComplete()) {
            Log.d("Solving","Backtracking started");
            long startTime = System.nanoTime();
            backtrackingSolve(findCellWithFewestPossVals());
            long endTime = System.nanoTime();
            //Gets time in ms
            long duration = (endTime - startTime)/1000000;
            Log.d("Solving","Completed in (ms):"+duration);
            consolesPrint();
        }
    }

    //Check every cell has an assigned answer value
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
        //System.out.println(randNumCells);
        goalNumVisibleCells = (int) randNumCells;
        Boolean firstloop = true;
        while(numVisibleCells <= goalNumVisibleCells ) {
            //if start value of either cell is true set other to true and numVisibleCells++
            if (firstloop) {
                if (gameCells[middle - x][middle - y].getStartValue() ||
                        gameCells[middle + x][middle + y].getStartValue()) {
                    gameCells[middle - x][middle - y].setStartValue(true);
                    gameCells[middle + x][middle + y].setStartValue(true);
                    //System.out.println("New cells (1) @x"+(middle - x)+",y"+(middle - y)+" AND @x"+(middle + x)+",y"+(middle + y));
                    numVisibleCells++;
                }
            }else{
            //if 10% chance
                random = new Random();
                randNumCells = random.nextInt(10);
                if (randNumCells == 0){
                    //System.out.println("New cells (2) @x"+(middle - x)+",y"+(middle - y)+" AND @x"+(middle + x)+",y"+(middle + y));
                    gameCells[middle - x][middle - y].setStartValue(true);;
                    gameCells[middle + x][middle + y].setStartValue(true);
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

    //Solves the sudoku regardless of num of starting vals - fewer starting vals = longer time
    private boolean backtrackingSolve(Cell cell){
        //Log.d("Backtracking","On - "+ cell.getX() + "," + cell.getY() + " Poss-" +(cell.getPossValues()));
        count++;
        //For each possible values of the cell
        for (Integer possvalue : cell.getPossValues()){
            //Log.d("Backtracking",cell.getX() + "," + cell.getY() + " Trying: " +possvalue);
            //consolesPrint();
            //if validated successfully
            if(validCellValue(possvalue, cell)) {
                //assign Value to the answer value of the cell
                cell.setAnswerValue(possvalue);
                //Check if this completes the Sudoku
                if (isComplete()){
                    Log.d("Backtracking ","complete");
                    Log.d("Backtracking ", String.valueOf(count));//373976
                    //D/Solving: Completed in (ms):326087 my phone
                    //D/Solving: Completed in (ms):1775 emulator
                    return true;
                }

                setPossibleValues();
                //find next cell and recurse, if it has possible values
                Cell nextCell = findCellWithFewestPossVals();
                //Next cell has no values go back to previous cell
                if (nextCell == null || nextCell.getPossValues().size() == 0){
                    cell.setAnswerValue(0);
                    return false;
                }else if (backtrackingSolve(nextCell)) {
                    return true;
                    //Reset answerValue and try next
                } else {
                    cell.setAnswerValue(0);
                }
                setPossibleValues();
            }
        }
        return false;
    }

    //Update possible values lists for every cell without a set answer
    private void setPossibleValues(){
        //Loop over ever cells
        for (int y = 0; y < gridSize; y++) {
            for (int x = 0; x < gridSize; x++) {
                //If the cell has no known answer
                if (gameCells[x][y].getAnswerValue() == 0) {
                    ArrayList<Integer> possvalues = new ArrayList<Integer>();
                    for (int counter = 1; counter < 10; counter++) {
                        //Add all valid values to the possible values list
                        if (validCellValue(counter, gameCells[x][y])) {
                            possvalues.add(counter);
                        }
                    }
                    gameCells[x][y].setPossValues(possvalues);
                }
            }
        }
    }

    //Finds the cell with the fewest possible values
    private Cell findCellWithFewestPossVals(){
        Cell bestOption = null;
        //Loop over the entire board
        for (int row = 0; row < gridSize ; row++) {
            for (int column = 0; column < gridSize; column++) {
                int numValues = gameCells[row][column].getPossValues().size();
                //Only check cells we don't have an answer for
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
        return bestOption;
    }

    //region Valid placement for specified cell and value combination
    //Check that this Value has not conflicts in - Row / Column / Region
    public boolean validCellValue(Integer possvalue, Cell cell) {
        boolean isValid = true;
        if(!validColumnValue(cell, possvalue) || !validRowValue(cell, possvalue) ||
                !validRegionValue(cell, possvalue)){
            isValid = false;
        }
        return isValid;
    }

    private boolean validColumnValue(Cell cell, int value) {
        for (int row = 0; row < gridSize ; row++) {
            //So it doesn't compare to itself
            if (row != cell.getX()) {
                if (gameCells[row][cell.getY()].getAnswerValue() == value) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean validRowValue(Cell cell, int value) {
        for (int column = 0; column < gridSize; column++){
            //So it doesn't compare to itself
            if (column!= cell.getY()) {
                if (gameCells[cell.getX()][column].getAnswerValue() == value) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean validRegionValue(Cell cell, int value) {
        //Could optimize so stops after finding 9 cells
        for (int row = 0; row < gridSize ; row++) {
            for (int column = 0; column < gridSize; column++){
                //So it doesn't compare to itself
                if (row != cell.getX() && column!= cell.getY()) {
                    if (gameCells[row][column].getRegion() == gameCells[cell.getX()][cell.getY()].getRegion()) {
                        if (gameCells[row][column].getAnswerValue() == value) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }
    //endregion

    //region Only valid position for a value (within row,column or region)
    private void constraintSolve(){
        boolean gridUpdated = true;
        while (gridUpdated) {
            gridUpdated = false;
            setPossibleValues();
            for (int row = 0; row < gridSize; row++) {
                for (int column = 0; column < gridSize; column++) {
                    //If any of these return true they updated the grid
                    if (checkColumnPossValues(gameCells[row][column]) ||
                            checkRowPossValues(gameCells[row][column])||
                            checkRegionPossValues(gameCells[row][column])) {
                        gridUpdated = true;
                    }
                    if (gameCells[row][column].getPossValues().size() == 1){
                        gameCells[row][column].setAnswerValue
                                (gameCells[row][column].getPossValues().get(0));
                        gridUpdated = true;
                    }
                }
            }
        }
    }

    //Check column to see if the cell passed in has a value that can only be placed in that cell
    private boolean checkColumnPossValues(Cell cell) {
        boolean updated = false;
        boolean onlyPos = true;
        //For each possible value in the list, loop over row and compare
        for (Integer possvalue : cell.getPossValues()) {
            for (int row = 0; row < gridSize; row++) {
                //If it's not the passed obj and the other cell has no set answer
                if (row != cell.getX() && gameCells[row][cell.getY()].getAnswerValue() == 0) {
                    //Get possible values and compare to current value
                    ArrayList<Integer> possvalues2 = gameCells[row][cell.getY()].getPossValues();
                    if (possvalues2.contains(possvalue)){
                        onlyPos = false;
                    }
                }
            }
            //If there are no other possible places for this num it must be the answer for this cell
            if (onlyPos && validCellValue(possvalue,cell) && cell.getAnswerValue() == 0){
                cell.setAnswerValue(possvalue);
                updated = true;
            }
        }
        return updated;
    }

    //Check row to see if the cell passed in has a value that can only be placed in that cell
    private boolean checkRowPossValues(Cell cell) {
        boolean onlyPos = true;
        boolean updated = false;
        //for each possible value in the list, loop over column and compare
        for (Integer possvalue : cell.getPossValues()) {
            for (int column = 0; column < gridSize; column++) {
                //If it's not the passed obj and the other cell has no set answer
                if (column != cell.getY() && gameCells[cell.getX()][column].getAnswerValue() == 0) {
                    //Get possible values can compare to current value
                    ArrayList<Integer> possvalues2 = gameCells[cell.getX()][column].getPossValues();
                    if (possvalues2.contains(possvalue)){
                        onlyPos = false;
                    }
                }
            }
            //If there are no other possible places for this num it must be the answer for this cell
            if (onlyPos && validCellValue(possvalue,cell)&& cell.getAnswerValue() == 0){
                cell.setAnswerValue(possvalue);
                updated = true;
            }
        }
        return updated;
    }

    //Check region to see if the cell passed in has a value that can only be placed in that cell
    private boolean checkRegionPossValues(Cell cell) {
        //Could optimize so stops after finding 9 cells
        boolean updated = false;
        boolean onlyPos = true;
        //for each possible value in the list, loop over row, column and compare
        for (Integer possvalue : cell.getPossValues()) {
            for (int row = 0; row < gridSize; row++) {
                for (int column = 0; column < gridSize; column++) {
                    //If same region but not same object and has no set answer
                    if (gameCells[row][column].getRegion() == cell.getRegion()
                            && (cell != gameCells[row][column])
                            && (gameCells[row][column].getAnswerValue() == 0)){
                        //Get possible values can compare to current value
                        ArrayList<Integer> possvalues2 = gameCells[row][column].getPossValues();
                        if (possvalues2.contains(possvalue)){
                            onlyPos = false;
                        }
                    }
                }
            }
            //If there are no other possible places for this num it must be the answer for this cell
            if (onlyPos && validCellValue(possvalue,cell) && cell.getAnswerValue() == 0){
                cell.setAnswerValue(possvalue);
                updated = true;
            }
        }
        return updated;
    }
    //endregion

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
