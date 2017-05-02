package com.cs39440.rob41.sudokuapp;

import java.util.ArrayList;

/**
 * Created by Rob Bayliss on 02/03/2017.
 */

public class Cell {
    private final int region;
    private int startValue;
    private int answerValue;
    private int userAssignedValue;
    private final int x;
    private final int y;
    private ArrayList <Integer> possValues = new ArrayList<>();

    public Cell(int passRegion, int passStartValue, int passX, int passY){
        // TODO Auto-generated constructor stub
        region = passRegion;
        startValue = passStartValue;
        userAssignedValue = 0;
        x = passX;
        y = passY;
        //Populate the inital list with all values
        if (passStartValue == 0){
            for (int counter = 1; counter < 10; counter++) {
                possValues.add(counter);
            }
            answerValue = 0;
        }else if(passStartValue >= 1 && passStartValue <=9 ){
            answerValue = passStartValue;
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getRegion() {
        return region;
    }

    public ArrayList<Integer> getPossValues() {
        return possValues;
    }

    public int getStartValue() {
        return startValue;
    }

    public int getUserAssignedValue() {
        return userAssignedValue;
    }

    public int getAnswerValue() {
        return answerValue;
    }

    public void setPossValues(ArrayList<Integer> possValues) {
        this.possValues = possValues;
    }

    public void setStartValue(int startValue) {
        this.startValue = startValue;
        setAnswerValue(startValue);
    }

    public void setUserAssignedValue(int userAssignedValue) {
        this.userAssignedValue = userAssignedValue;
    }

    public void setAnswerValue(int answerValue) {
        possValues = new ArrayList<>();
        this.answerValue = answerValue;
    }

    public void setStartValToAnsVal(){
        this.startValue = this.answerValue;
    }
}
