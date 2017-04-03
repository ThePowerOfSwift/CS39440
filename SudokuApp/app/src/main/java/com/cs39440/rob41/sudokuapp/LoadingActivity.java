package com.cs39440.rob41.sudokuapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.utils.Converters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.opencv.core.Core.bitwise_not;


public class LoadingActivity extends Activity {
    private final int gridSize = 9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        Log.d("test1","LOADINGACTIVITY");
        //Retrive the additional data added to the intent
        Intent passedIntent = getIntent();
        boolean fromImage = passedIntent.getBooleanExtra("fromImage",false);

        if (fromImage){
            Log.d("fromImage:",String.valueOf(fromImage));
            createFromImage();
        }else{
            Log.d("fromImage:",String.valueOf(fromImage));
            createFromAlgorithm();
        }
        /*
        GameBoard.getInstance().consolesPrint();

        Intent intent = new Intent(this, CreateSudokuActivity.class);
        intent.putExtra("fromImage",true);
        startActivity (intent);
        Log.d("test2","LOADINGACTIVITY - - - - CLOSING");
        finish();
        */
    }

    private void createFromAlgorithm() {
        //SAMPLE SUDOKU
        /*int [] sudoku ={
                8,0,0,5,0,0,3,2,0,
                7,0,3,1,0,0,4,0,0,
                1,2,0,0,0,9,0,0,8,
                6,5,0,0,9,3,0,8,0,
                0,9,0,0,0,0,0,1,0,
                0,8,0,6,2,0,0,9,3,
                2,0,0,4,0,0,0,6,7,
                0,0,6,0,0,8,1,0,2,
                0,7,4,0,0,6,0,0,5,0};*/

        int numCells = gridSize*gridSize;
        int cellValues []  = new int [numCells];
        Arrays.fill(cellValues,0);

        Random random = new Random();
        for(int counter = 1; counter < 10; counter++){
            boolean numPlaced = false;
            while(!numPlaced){
                int randomNum = random.nextInt(numCells);
                if (cellValues[randomNum] == 0){
                    cellValues[randomNum] = counter;
                    numPlaced = true;
                }
            }
        }
        GameBoard.getInstance().createCells(cellValues);
        GameBoard.getInstance().solve();
        GameBoard.getInstance().setVisibleCells();
    }

    private void createFromImage() {
        Log.d("createFromImage ","Started");
        //get image
        Mat sudokuOriginal = null;
        try {
            sudokuOriginal = Utils.loadResource(this, R.drawable.test3);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (sudokuOriginal.empty()){
            Log.d("Read: ", "Failed reading image");
        }

        Mat sudokuAltered = preprocessImage(sudokuOriginal);

        /*
        //CALL THIS IF > 4 POINTS
        //Dilate the image so white areas are more defined
        Mat dilate_kernal = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size (5, 5));
        Imgproc.dilate(sudoku, sudoku,dilate_kernal);
        */
        Point[] cornersUnordered = findCornerPnts(sudokuAltered);
        //Useful for debugging
        sudokuOriginal = drawOuline(sudokuOriginal, cornersUnordered);
        Point[] cornersOrdered = orderCornerPnts(cornersUnordered);

        int croppedWidth = getCropImageWidth(cornersOrdered);
        int croppedHeight = getCropImageHeight(cornersOrdered);

        Mat outputMat = new Mat(croppedWidth, croppedHeight, CvType.CV_8UC1);


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

        Mat perspectiveTransform = Imgproc.getPerspectiveTransform(source2f, dest2f);
        Imgproc.warpPerspective(sudokuOriginal, outputMat, perspectiveTransform, new Size(croppedWidth, croppedHeight));

        outputMat = drawCells(outputMat,croppedWidth, croppedHeight);


        //Convert to bitmap
        Bitmap output = Bitmap.createBitmap(croppedWidth, croppedHeight, Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(outputMat, output);

        //Imgproc.drawContours(sudoku2, contourList, tracker, new Scalar(255, 0, 0));

        //Find image display
        ImageView img= (ImageView) findViewById(R.id.capturedImage);
        img.setImageBitmap(output);
        Log.d("createFromImage ","Finished Successfully");
    }

    private Mat drawCells(Mat outputMat, int croppedWidth, int croppedHeight) {
        int cellWidth = croppedWidth/9;
        int cellHeight = croppedHeight/9;
        for (int y = 0; y < croppedHeight; y = y + cellHeight){
            for(int x = 0; x < croppedWidth; x = x + cellWidth){
                Imgproc.rectangle(outputMat,new Point(x, y),new Point(x+cellWidth, y+cellHeight),new Scalar(105, 255, 180),2);
            }
        }
        return outputMat;
    }

    private int getCropImageHeight(Point[] cornersOrdered) {
        int resultHeight = (int)(cornersOrdered[3].y - cornersOrdered[0].y);
        int bottomHeight = (int)(cornersOrdered[2].y - cornersOrdered[1].y);
        //Take the larger of the two
        if(bottomHeight > resultHeight)
            resultHeight = bottomHeight;
        return resultHeight;
    }

    private int getCropImageWidth(Point[] cornersOrdered) {
        int resultWidth = (int)(cornersOrdered[1].x - cornersOrdered[0].x);
        int bottomWidth = (int)(cornersOrdered[2].x - cornersOrdered[3].x);
        //Take the larger of the two
        if(bottomWidth > resultWidth)
            resultWidth = bottomWidth;
        return resultWidth;
    }

    private Mat drawOuline(Mat sudokuOriginal, Point[] cornersUnordered) {
        for(int counter = 0; counter < cornersUnordered.length; counter++ ){
            Log.d("for ","x: "+cornersUnordered[counter].x+" y: "+cornersUnordered[counter].y);
            if (counter >= 1){
                Imgproc.line(sudokuOriginal,cornersUnordered[counter-1],cornersUnordered[counter],new Scalar(255, 105, 180),4);
            }
        }
        Imgproc.line(sudokuOriginal,cornersUnordered[0],cornersUnordered[cornersUnordered.length-1],new Scalar(255, 105, 180),4);
        return sudokuOriginal;
    }

    private Point[] findCornerPnts(Mat sudokuAltered) {
        double area=0;
        double largestArea = 0;
        List<MatOfPoint> contourList = new ArrayList<>();
        Imgproc.findContours(sudokuAltered, contourList, new Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
        //Log.d("createContourList ",String.valueOf(contourList.size()));
        MatOfPoint2f approx = null;

        for(int counter = 0; counter < contourList.size(); counter++ ){
            area = Imgproc.contourArea(contourList.get(counter));
            //filters out the smallest areas
            if(area>200){
                MatOfPoint2f sudokuPosCorners = new MatOfPoint2f(contourList.get(counter).toArray());
                MatOfPoint2f approxCurve = new MatOfPoint2f();
                double perimeter = 0.02*Imgproc.arcLength(sudokuPosCorners,true);
                Imgproc.approxPolyDP(sudokuPosCorners,approxCurve,perimeter,true);
                //Log.d("for ","approx: "+String.valueOf(approxCurve.size())+" area:"+String.valueOf(area));
                if (area > largestArea  ){//&& 4 == approxCurve
                    largestArea = area;
                    approx = approxCurve;
                    //Log.d("NEW ","LARGEST: "+String.valueOf(largestArea));
                }
            }
        }

        Point[] list = approx.toArray();
        return list;
    }

    private Mat preprocessImage(Mat sudokuOriginal) {
        Mat sudokuAltered = sudokuOriginal.clone();
        //Set to grey scale
        Imgproc.cvtColor(sudokuOriginal, sudokuAltered, Imgproc.COLOR_RGB2GRAY);
        //Imgproc.medianBlur(sudoku,sudoku,5);

        //Blur to reduce noise and so easier  identify lines / numbers
        Imgproc.GaussianBlur(sudokuAltered, sudokuAltered, new Size (5, 5), 0);
        //Further reduce noise with Adaptive gaussian(vs mean) and deals with varying illumination
        Imgproc.adaptiveThreshold(sudokuAltered, sudokuAltered, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY, 19, 2);
        //Invert the colours
        bitwise_not(sudokuAltered, sudokuAltered);
        return sudokuAltered;
    }

    private Point[] orderCornerPnts(Point[] list) {
        Point[] orderedList = new Point[4];

        Log.d("Size  of ARRAY ",String.valueOf(list.length));
        //Smallest
        orderedList[0] = new Point (list[0].x,list[0].y);
        //Largest
        orderedList[2] = new Point (list[0].x,list[0].y);

        //Find the Top right (smallest total Val) and Bottom left (largest total Val)
        for(int counter = 1; counter < list.length; counter++ ){
            if(sumPoint(list[counter]) > sumPoint(orderedList[2])){
                orderedList[2] = list[counter];
            }
            if(sumPoint(list[counter]) < sumPoint(orderedList[0])){
                orderedList[0] = list[counter];
            }
        }

        Point[] remainingPtnList = new Point[2];
        int pos = 0;
        for(int counter = 0; counter < list.length; counter++ ){
            //If it's not the smallest of the Largest
            if(!list[counter].equals(orderedList[0]) && !list[counter].equals(orderedList[2])){
                remainingPtnList[pos] = list[counter];
                pos++;
                //Log.d("pos: ",String.valueOf(pos));
            }
        }
        //The one with the higher x Val will be the Top Right
        if (remainingPtnList[0].x > remainingPtnList[1].x ){
            orderedList[1] = remainingPtnList[0];
            orderedList[3] = remainingPtnList[1];
        }else{
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

        return orderedList;
    }


    private double sumPoint(Point point){
        return point.x+point.y;
    }
}
