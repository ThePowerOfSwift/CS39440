package com.cs39440.rob41.sudokuapp;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by Rob Bayliss on 04/03/2017.
 */

public class CellUnitTest {
    @Test
    //Create a cell with a start value
    public void createAnswerCell() throws Exception {
        //region, StartValue, x, y
        Cell cell = new Cell(3, 9, 1, 2);
        //Check the passed in values are assigned
        assertEquals("The Region should be 3 ", 3, cell.getRegion());
        assertEquals("The Answer Value should be 9 ", 9, cell.getAnswerValue());
        assertEquals("X should be 1 ", 1, cell.getX());
        assertEquals("Y should be 2 ", 2, cell.getY());

        //Check the values the constructor creates
        assertEquals("The Start Value should be true ", true, cell.getStartValue());
        assertEquals("The User Value should be 0 ", 0, cell.getUserAssignedValue());
        assertTrue("Poss Value list should be empty", cell.getPossValues().isEmpty());
    }

    @Test
    //Create a cell with no start value
    public void createEmptyCell() throws Exception {
        //region, StartValue, x, y
        Cell cell = new Cell(3, 0, 1, 2);
        //Check the passed in values are assigned
        assertEquals("The Region should be 3 ", 3, cell.getRegion());
        assertEquals("The Answer Value should be 0 ", 0, cell.getAnswerValue());
        assertEquals("X should be 1 ", 1, cell.getX());
        assertEquals("Y should be 2 ", 2, cell.getY());

        //Check the values the constructor creates
        assertEquals("The Start Value should be false ", false, cell.getStartValue());
        assertEquals("The User Value should be 0 ", 0, cell.getUserAssignedValue());
        assertFalse("Poss Value list should NOT be empty", cell.getPossValues().isEmpty());
        assertEquals("Poss Value list should contain 9 values", 9, cell.getPossValues().size());
    }

    @Test
    //Update Starting value
    public void updateStartValueOfCell() throws Exception {
        Cell cell = new Cell(3, 0, 1, 2);

        //Check the starting values are correct
        assertEquals("The Start Value should be False ", false, cell.getStartValue());
        assertEquals("The Answer Value should be 0 ", 0, cell.getAnswerValue());
        assertFalse("Poss Value list should NOT be empty", cell.getPossValues().isEmpty());
        assertEquals("Poss Value list should contain 9 values", 9, cell.getPossValues().size());

        //Update the start value which should also clear the poss value list and answer value
        cell.setAnswerValue(3);
        cell.setStartValue(true);
        assertEquals("The Start Value should now be true ",true, cell.getStartValue());
        assertEquals("The Answer Value should now be 3 ", 3, cell.getAnswerValue());
        assertTrue("Poss Value list should be null", cell.getPossValues().isEmpty());
    }

    @Test
    //Update Possible value list ofa cell
    public void updatePossValueOfCell() throws Exception {
        Cell cell = new Cell(3, 0, 1, 2);

        //Check the starting values are correct
        assertFalse("Poss Value list should NOT be empty", cell.getPossValues().isEmpty());
        assertEquals("Poss Value list should contain 9 values", 9, cell.getPossValues().size());

        //Create and populate a new arraylist to replace the current
        ArrayList<Integer> possValues = new ArrayList<>();
        possValues.add(1);
        possValues.add(2);
        possValues.add(3);
        cell.setPossValues(possValues);
        assertFalse("Poss Value list should NOT be empty", cell.getPossValues().isEmpty());
        assertEquals("Poss Value list should now contain 3 values", 3, cell.getPossValues().size());
    }

    @Test
    //Update User assigned value of a cell
    public void updateUserValueOfCell() throws Exception {
        Cell cell = new Cell(3, 0, 1, 2);

        //Check the starting values are correct
        assertEquals("The User Value should be 0 ", 0, cell.getUserAssignedValue());

        //set User Assigned Value
        cell.setUserAssignedValue(2);
        assertEquals("The User Value should now be 2 ", 2, cell.getUserAssignedValue());
    }

    @Test
    //Update Answer value of a cell
    public void updateAnsValueOfCell() throws Exception {
        Cell cell = new Cell(3, 0, 1, 2);

        //Check the starting values are correct
        assertFalse("Poss Value list should NOT be empty", cell.getPossValues().isEmpty());
        assertEquals("Poss Value list should contain 9 values", 9, cell.getPossValues().size());
        assertEquals("The Answer Value should be 0 ", 0, cell.getAnswerValue());

        //Set Answer Value
        cell.setAnswerValue(7);
        assertEquals("The Answer Value should now be 7 ", 7, cell.getAnswerValue());
        assertTrue("Poss Value list should be empty", cell.getPossValues().isEmpty());

    }
}