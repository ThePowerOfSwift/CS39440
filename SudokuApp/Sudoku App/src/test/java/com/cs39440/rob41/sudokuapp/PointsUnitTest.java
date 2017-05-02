package com.cs39440.rob41.sudokuapp;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Robert Bayliss on 06/03/2017.
 */
public class PointsUnitTest {
    @Test
    public void createPoint() throws Exception {
        Points point = new Points(1,2);
        assertEquals("x should be 1 ", 1, point.getX());
        assertEquals("y should be 2 ", 2, point.getY());
    }
}