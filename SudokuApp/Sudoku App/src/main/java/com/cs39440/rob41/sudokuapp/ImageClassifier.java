package com.cs39440.rob41.sudokuapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.utils.Converters;

import java.util.ArrayList;
import java.util.List;

import static org.opencv.core.Core.bitwise_not;

/**
 * Created by Robert Bayliss on 08/04/17.
 */

class ImageClassifier {
    private int cellValues[];
    private Point[] cornersUnordered;
    private Point[] cornersOrdered;
    private Bitmap processedImg;

    ImageClassifier(Mat sudokuOriginal, int gridSize, Context context) {
        int resize = 1026;
        cellValues = new int[gridSize * gridSize];
        Mat sudokuAltered = preprocessImage(sudokuOriginal);

        /*
        Mat dilate_kernal = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size (10, 10));
        Imgproc.dilate(sudokuAltered, sudokuAltered,dilate_kernal);
        */
        //IF RETURNS NULL
        setCornersUnordered(sudokuAltered);
        if (cornersUnordered == null) {
            Log.d("Corners not found", " Returned Null");
            processedImg = Bitmap.createBitmap(sudokuAltered.cols(), sudokuAltered.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(sudokuAltered, processedImg);
            return;
        }

        //IF > 4 points Dilate the image so white areas are more defined
        if (cornersUnordered.length != 4) {
            Log.d("Corners not found", cornersUnordered.length + " Dilating Image");
            Mat dilate_kernal = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5, 5));
            Imgproc.dilate(sudokuAltered, sudokuAltered, dilate_kernal);
            setCornersUnordered(sudokuAltered);
            //If we still have not found the corners display error
        }
        if (cornersUnordered.length != 4) {
            Log.d("Corners still not found", cornersUnordered.length + " quiting");
            sudokuAltered = drawSudokuOutline(sudokuAltered, cornersUnordered);
            processedImg = Bitmap.createBitmap(sudokuAltered.cols(), sudokuAltered.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(sudokuAltered, processedImg);
        } else {
            //Useful for debugging
            //sudokuOriginal = drawSudokuOutline(sudokuOriginal, cornersUnordered);
            setCornersOrdered(cornersUnordered);

            int croppedWidth = getCropImageWidth();
            int croppedHeight = getCropImageHeight();

            Mat outputMat = new Mat(croppedWidth, croppedHeight, CvType.CV_8UC1);

            //DeSkew the image
            Mat perspectiveTransform = getTransformation(croppedWidth, croppedHeight);
            Imgproc.warpPerspective(sudokuAltered, outputMat, perspectiveTransform, new Size(croppedWidth, croppedHeight));
            outputMat = drawCells(outputMat);

            //Reverse the colours back to normal and resize
            bitwise_not(outputMat, outputMat);
            Imgproc.resize(outputMat, outputMat, new Size(resize, resize));

            //Called the OCR
            callTesseract(context, outputMat);

            //Convert to bitmap
            processedImg = Bitmap.createBitmap(resize, resize, Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(outputMat, processedImg);
            //Log.d("createFromImage ", "Finished Successfully");
        }
    }

    Point[] getCornersUnordered() {
        return cornersUnordered;
    }

    Bitmap getProcessedImg() {
        return processedImg;
    }

    //Run the OCR on each specified cell image
    private void callTesseract(Context context, Mat outputMat) {
        TessOCR tessOCR = new TessOCR(context);
        int cellPadding = 13;
        int cellWidth = (outputMat.width() / 9) - (cellPadding * 2);
        int cellHeight = (outputMat.height() / 9) - (cellPadding * 2);
        int xPos = cellPadding;
        int yPos = cellPadding;
        int counter = 0;
        //Log.d("Cell width: ", +cellWidth + " height: " + cellHeight);
        Mat croppedCell;
        Mat ocrMat = outputMat.clone();
        Bitmap croppedCellbm = Bitmap.createBitmap(cellWidth, cellHeight, Bitmap.Config.ARGB_8888);
        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                //Select the size and position which makes up a cell
                Rect cellPos = new Rect(xPos, yPos, cellWidth, cellHeight);
                //Find that point on the de-skewed image
                croppedCell = new Mat(ocrMat, cellPos);
                //Convert to BitMap
                Utils.matToBitmap(croppedCell, croppedCellbm);
                String cellText = tessOCR.getOCRResult(croppedCellbm, cellHeight);
                Log.d("Cellxy:", +x + "," + y + "  " + cellText);
                cellValues[counter] = Integer.valueOf(cellText);
                counter++;

                Imgproc.rectangle(outputMat, new Point(xPos, yPos),
                        new Point(xPos + cellWidth, yPos + cellHeight), new Scalar(105, 255, 180), 1);
                xPos = xPos + cellWidth + cellPadding * 2;
            }
            yPos = yPos + cellHeight + cellPadding * 2;
            xPos = cellPadding;
        }
        //Create a gameboard with the suspected values from the OCR
        GameBoard.getInstance().createCells(cellValues);
        GameBoard.getInstance().consolesPrint();
    }

    //Transform the image so that it's a unskewed square
    private Mat getTransformation(int croppedWidth, int croppedHeight) {
        List<Point> source = new ArrayList<>();
        source.add(cornersOrdered[0]);
        source.add(cornersOrdered[1]);
        source.add(cornersOrdered[3]);
        source.add(cornersOrdered[2]);
        Mat source2f = Converters.vector_Point2f_to_Mat(source);

        List<Point> dest = new ArrayList<>();
        dest.add(new Point(0, 0));
        dest.add(new Point(croppedWidth, 0));
        dest.add(new Point(0, croppedHeight));
        dest.add(new Point(croppedWidth, croppedHeight));
        Mat dest2f = Converters.vector_Point2f_to_Mat(dest);

        Mat transformed = Imgproc.getPerspectiveTransform(source2f, dest2f);
        return transformed;
    }

    //returns the larger of the two Heights
    private int getCropImageHeight() {
        int largestHeight = (int) (cornersOrdered[3].y - cornersOrdered[0].y);
        int bottomHeight = (int) (cornersOrdered[2].y - cornersOrdered[1].y);
        //Take the larger of the two
        if (bottomHeight > largestHeight)
            largestHeight = bottomHeight;
        return largestHeight;
    }

    //returns the larger of the two Widths
    private int getCropImageWidth() {
        int largestWidth = (int) (cornersOrdered[1].x - cornersOrdered[0].x);
        int bottomWidth = (int) (cornersOrdered[2].x - cornersOrdered[3].x);
        //Take the larger of the two
        if (bottomWidth > largestWidth)
            largestWidth = bottomWidth;
        return largestWidth;
    }

    //Draws each individual cell area which will be checked for characters
    private Mat drawCells(Mat outputMat) {
        int cellWidth = outputMat.width() / 9;
        int cellHeight = outputMat.height() / 9;
        int yPos = 0;
        int xPos = 0;
        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                Imgproc.rectangle(outputMat, new Point(xPos, yPos),
                        new Point(xPos + cellWidth, yPos + cellHeight), new Scalar(105, 255, 180), 1);
                xPos = xPos + cellWidth;
            }
            yPos = yPos + cellHeight;
            xPos = 0;
        }
        return outputMat;
    }

    //Takes the unordered list of corners and draws a line between them- outline of the sudoku grid
    private Mat drawSudokuOutline(Mat sudokuOriginal, Point[] cornersUnordered) {
        for (int counter = 0; counter < cornersUnordered.length; counter++) {
            //Log.d("for ","x: "+cornersUnordered[counter].x+" y: "+cornersUnordered[counter].y);
            if (counter >= 1) {
                Imgproc.line(sudokuOriginal, cornersUnordered[counter - 1], cornersUnordered[counter], new Scalar(255, 105, 180), 4);
            }
        }
        Imgproc.line(sudokuOriginal, cornersUnordered[0], cornersUnordered[cornersUnordered.length - 1], new Scalar(255, 105, 180), 4);
        return sudokuOriginal;
    }

    //Convert to grey scale and apply Thresholding
    private Mat preprocessImage(Mat sudokuOriginal) {
        Mat sudokuAltered = sudokuOriginal.clone();
        //Set to grey scale
        Imgproc.cvtColor(sudokuOriginal, sudokuAltered, Imgproc.COLOR_RGB2GRAY);

        //Blur to reduce noise and so easier  identify lines / numbers
        //Imgproc.GaussianBlur(sudokuAltered, sudokuAltered, new Size (5, 5), 0);
        //Further reduce noise with Adaptive gaussian(vs mean) and deals with varying illumination
        Imgproc.adaptiveThreshold(sudokuAltered, sudokuAltered, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY, 25, 11);

        //Invert the colours
        bitwise_not(sudokuAltered, sudokuAltered);
        return sudokuAltered;
    }

    //Finds the corners of the image passed in
    private void setCornersUnordered(Mat sudokuAltered) {
        double area = 0;
        double largestArea = 0;
        List<MatOfPoint> contourList = new ArrayList<>();
        Imgproc.findContours(sudokuAltered, contourList, new Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
        //Log.d("createContourList ",String.valueOf(contourList.size()));
        MatOfPoint2f approx = null;

        for (int counter = 0; counter < contourList.size(); counter++) {
            area = Imgproc.contourArea(contourList.get(counter));
            //filters out the smallest areas
            if (area > 200) {
                MatOfPoint2f sudokuPosCorners = new MatOfPoint2f(contourList.get(counter).toArray());
                MatOfPoint2f approxCurve = new MatOfPoint2f();
                double perimeter = 0.02 * Imgproc.arcLength(sudokuPosCorners, true);
                Imgproc.approxPolyDP(sudokuPosCorners, approxCurve, perimeter, true);
                //Log.d("for ","approx: "+String.valueOf(approxCurve.size())+" area:"+String.valueOf(area));
                if (area > largestArea) {//&& 4 == approxCurve
                    largestArea = area;
                    approx = approxCurve;
                    //Log.d("NEW ","LARGEST: "+String.valueOf(largestArea));
                }
            }
        }
        Point[] list = null;
        if (approx != null) {
            list = approx.toArray();
        }
        cornersUnordered = list;
    }

    //Standardise the order of the corners
    private void setCornersOrdered(Point[] unorderedList) {
        Point[] orderedList = new Point[4];

        Log.d("Size  of ARRAY ", String.valueOf(unorderedList.length));
        //Smallest
        orderedList[0] = new Point(unorderedList[0].x, unorderedList[0].y);
        //Largest
        orderedList[2] = new Point(unorderedList[0].x, unorderedList[0].y);

        //Find the Top right (smallest total Val) and Bottom left (largest total Val)
        for (int counter = 1; counter < unorderedList.length; counter++) {
            if (sumPoint(unorderedList[counter]) > sumPoint(orderedList[2])) {
                orderedList[2] = unorderedList[counter];
            }
            if (sumPoint(unorderedList[counter]) < sumPoint(orderedList[0])) {
                orderedList[0] = unorderedList[counter];
            }
        }

        Point[] remainingPtnList = new Point[2];
        int pos = 0;
        for (int counter = 0; counter < unorderedList.length; counter++) {
            //If it's not the smallest of the Largest
            if (!unorderedList[counter].equals(orderedList[0]) && !unorderedList[counter].equals(orderedList[2])) {
                remainingPtnList[pos] = unorderedList[counter];
                pos++;
                //Log.d("pos: ",String.valueOf(pos));
            }
        }
        //The one with the higher x Val will be the Top Right
        if (remainingPtnList[0].x > remainingPtnList[1].x) {
            orderedList[1] = remainingPtnList[0];
            orderedList[3] = remainingPtnList[1];
        } else {
            orderedList[1] = remainingPtnList[1];
            orderedList[3] = remainingPtnList[0];
        }
        /*
        Returns in the order: -
        Top Left, Top Right, Bottom Right, Bottom Left
        Log.d("topLeft x: ",orderedList[0].x+" y: "+orderedList[0].y);
        Log.d("topRight x: ",orderedList[1].x+" y: "+orderedList[1].y);
        Log.d("botRight x: ",orderedList[2].x+" y: "+orderedList[2].y);
        Log.d("botLeft x: ",orderedList[3].x+" y: "+orderedList[3].y);
        */
        cornersOrdered = orderedList;
    }

    private double sumPoint(Point point) {
        return point.x + point.y;
    }
}
