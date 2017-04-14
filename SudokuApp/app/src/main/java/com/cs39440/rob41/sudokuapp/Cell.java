package com.cs39440.rob41.sudokuapp;

import android.util.Log;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Rob Bayliss on 06/03/2017.
 */

public class Cell {
    private int region;
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
        //populate the inital list with all values
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

    public void setRegion(int region) {
        this.region = region;
    }

    public ArrayList<Integer> getPossValues() {
        return possValues;
    }

    public void setPossValues(ArrayList<Integer> possValues) {
        this.possValues = possValues;
    }

    public int getStartValue() {
        return startValue;
    }

    public void setStartValue(int startValue) {
        this.startValue = startValue;
    }

    public void setStartValToAnsVal(){
        this.startValue = this.answerValue;
    }

    public int getAnswerValue() {
        return answerValue;
    }

    public void setAnswerValue(int answerValue) {
        possValues = new ArrayList<>();
        this.answerValue = answerValue;
    }

    public int getUserAssignedValue() {
        return userAssignedValue;
    }

    public void setUserAssignedValue(int userAssignedValue) {
        this.userAssignedValue = userAssignedValue;
    }

    public void updatePossValues(ArrayList<Integer> excludedValues) {
        if (this.getStartValue() == 0){
            for (int counter = possValues.size()-1; counter >= 0 ; counter--) {
                if(excludedValues.contains(possValues.get(counter))){
                    possValues.remove(counter);
                }
            }
        }
        //Log.d("possValues:",possValues.toString()+" SET:" + region +" ExValues"+excludedValues.toString());
    }
}
