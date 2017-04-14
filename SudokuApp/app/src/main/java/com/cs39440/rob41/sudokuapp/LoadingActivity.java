package com.cs39440.rob41.sudokuapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.opencv.core.Core.bitwise_not;


public class LoadingActivity extends Activity implements View.OnFocusChangeListener {
    private final int gridSize = 9;
    GridLayout gridLayout;
    Bitmap croppedSudoku;
    Map<Integer, Points> cellLookUpTable = new HashMap<Integer, Points>();
    View oldFocus = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        Log.d("Loading Activity ","Started");
        //Retrive the additional data added to the intent (defaulted set to false)
        Intent passedIntent = getIntent();
        boolean fromImage = passedIntent.getBooleanExtra("fromImage",false);

        if (fromImage){
            Log.d("fromImage:",String.valueOf(fromImage));
            String imagePath = passedIntent.getStringExtra("imagePath");
            createFromImage(imagePath);
        }else{
            Log.d("fromImage:",String.valueOf(fromImage));
            createFromAlgorithm();
            Intent intent = new Intent(this, PlaySudokuActivity.class);
            startActivity (intent);
            finish();
        }
    }

    private void createFromAlgorithm() {
        //SAMPLE SUDOKU
        /*int [] cellValues ={
                0,0,1,0,0,2,0,4,0,
                3,0,0,5,0,0,6,0,0,
                0,9,0,0,0,0,0,0,7,
                9,0,0,0,1,0,0,8,0,
                0,0,0,2,4,8,0,0,0,
                0,2,0,0,6,0,0,0,3,
                4,0,0,0,0,0,0,3,0,
                0,0,3,0,0,1,0,0,8,
                0,5,0,7,0,0,9,0,0};*/

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
        Log.d("CONSOLE PRINT","current");
        GameBoard.getInstance().consolesPrint();
    }

    private void createFromImage(String imagePath) {
        Log.d("createFromImage: ","Started, Using img: "+imagePath);
        Mat sudokuOriginal = new Mat();

        //get image
        Bitmap bmImg = BitmapFactory.decodeFile(imagePath);
        Utils.bitmapToMat(bmImg,sudokuOriginal);

        /* This can be used to load preset test images from the drawable folder.
        try {
            sudokuOriginal = Utils.loadResource(this, R.drawable.test2);
        } catch (IOException e) {
            e.printStackTrace();
        }
        */

        if (sudokuOriginal.empty()){
            Log.d("Read: ", "Failed reading image");
        }else{
            final ImageClassifier classifier = new ImageClassifier(sudokuOriginal, gridSize, LoadingActivity.this);
            croppedSudoku = classifier.getCroppedImage();
            //ImageView img = (ImageView) findViewById(R.id.capturedImage);
            //img.setImageBitmap(croppedSudoku);
            if (classifier.getCornersUnordered().length == 4) {
                LinearLayout layout = (LinearLayout) findViewById(R.id.templateHolder);
                layout.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("Runing",String.valueOf(classifier.getCornersUnordered().length));
                            createExampleGrid();
                    }
                });
            }else{
                Toast.makeText(getApplicationContext(),"Could not detect Sudoku" ,
                        Toast.LENGTH_LONG).show();
                //finish();
            }
        }
    }

    public void createExampleGrid(){
        gridLayout = (GridLayout)findViewById(R.id.templateGrid);

        int gridSize = updateDisplay();
        Log.d("gridSize",String.valueOf(gridSize));
        int numOfCol = gridLayout.getColumnCount();
        int numOfRow = gridLayout.getRowCount();
        int cellSize = gridSize/9;
        int count = 0;
        for(int yPos=0; yPos<numOfRow; yPos++){
            for(int xPos=0; xPos<numOfCol; xPos++){
                int gridValue = GameBoard.getInstance().getCell(xPos,yPos).getStartValue();
                EditText cellText = new EditText(this);
                cellText.setFilters(new InputFilter[] {new InputFilter.LengthFilter(1)});
                cellText.setInputType(InputType.TYPE_CLASS_NUMBER);
                //Disable the cursor and PointIcon as causes issues
                cellText.setCursorVisible(false);
                cellText.setLongClickable(false);
                //Required for the text to center correctly
                cellText.setPadding(0,0,0,0);
                //Set the ID
                cellText.setId(count);
                //Formatting of the text
                cellText.setTextSize(TypedValue.COMPLEX_UNIT_SP,24);
                cellText.setTypeface(null, Typeface.BOLD);
                cellText.setGravity(Gravity.CENTER);
                //Formatting of the cell
                cellText.setBackgroundColor(Color.TRANSPARENT);
                cellText.setWidth(cellSize);
                cellText.setHeight(cellSize);
                cellText.setSelectAllOnFocus(true);
                if (gridValue != 0) {
                    cellText.setText(String.valueOf(gridValue));
                }else{
                    cellText.setText(String.valueOf(""));
                }
                cellText.setTextColor(Color.parseColor("#111111"));

                //Add Listeners..
                // ...so when clicked on it's highlighted
                cellText.setOnFocusChangeListener(this);
                // ...so when datas entered it's valided and passed to the gameboard
                cellText.setOnClickListener(cellValueChanged);

                cellLookUpTable.put(count,new Points(xPos,yPos));
                gridLayout.addView(cellText);
                count++;
            }
        }
        markNonSymmetrical();
        validateExampleGrid();
    }

    private boolean validateExampleGrid(){
        int numOfCol = gridLayout.getColumnCount();
        int numOfRow = gridLayout.getRowCount();
        int count = 0;
        boolean valid = true;
        for(int yPos=0; yPos<numOfRow; yPos++) {
            for (int xPos = 0; xPos < numOfCol; xPos++) {
                EditText cellText = (EditText)findViewById(count);
                String cellString = cellText.getText().toString();
                if (!cellString.equals("")) {
                    int currentValue = Integer.parseInt(cellString);
                    Points cellRef = cellLookUpTable.get(count);
                    int x = cellRef.getX();
                    int y = cellRef.getY();
                    if (!GameBoard.getInstance().validValue(currentValue, GameBoard.getInstance().getCell(x, y))) {
                        //Set Text red
                        cellText.setTextColor(Color.parseColor("#8c1500"));
                        valid = false;
                    }else{
                        cellText.setTextColor(Color.parseColor("#111111"));
                    }
                }
                count++;
            }
        }
        return valid;
    }

    private void markNonSymmetrical(){
        int x = 0;
        int y = 1;
        int middle = 4;
        boolean complete = false;
        //Until we reach the end of the grid
        while (!complete){
            EditText cellText = (EditText)findViewById(coordToInt((middle - x),(middle - y)));
            EditText cellText2 = (EditText)findViewById(coordToInt((middle + x),(middle + y)));
            if (cellText.getText().length()!=cellText2.getText().length()) {
                cellText.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.cellHighlightRed,null));
                cellText2.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.cellHighlightRed,null));
            }else{
                cellText.setBackgroundColor(Color.TRANSPARENT);
                cellText2.setBackgroundColor(Color.TRANSPARENT);
            }
            y++;
            if(y == 5){
                y = -4;
                x++;
            }
            if (x == 5){
                complete = true;
            }
        }
    }

    private int coordToInt(int x, int y){
        return (x*9)+y;
    }

    //On load Resize the photo and grid to best fit the Screen
    public int updateDisplay(){
        LinearLayout layout = (LinearLayout)findViewById(R.id.templateHolder);
        LinearLayout btnLayout = (LinearLayout)findViewById(R.id.templateButtonHolder);
        gridLayout = (GridLayout)findViewById(R.id.templateGrid);
        //Display the cropped Photo
        ImageView img = (ImageView) findViewById(R.id.capturedImage);
        img.setImageBitmap(croppedSudoku);

        int gridWidth = layout.getWidth();
        int gridHeight = layout.getHeight()/2 - btnLayout.getHeight();

        //Find which is the shortest and set both to that to ensure it's square
        int newXY = Math.min(gridWidth,gridHeight);

        ViewGroup.LayoutParams layoutParams = gridLayout.getLayoutParams();
        layoutParams.height = newXY;
        layoutParams.width = newXY;
        gridLayout.setLayoutParams(layoutParams);

        ViewGroup.LayoutParams imglayoutParams = img.getLayoutParams();
        imglayoutParams.height = newXY;
        imglayoutParams.width = newXY;
        img.setLayoutParams(imglayoutParams);

        return newXY;
    }

    private void validateCellInput(View editTextCell){
        EditText cellText = (EditText) editTextCell;
        //using ID lookup the XY coordinates;
        Points cellRef= cellLookUpTable.get(cellText.getId());
        int x = cellRef.getX();
        int y = cellRef.getY();
        if (!cellText.getText().toString().isEmpty()){
            int cellValue = Integer.parseInt(cellText.getText().toString());
            if(cellValue >= 1 && cellValue <=9){
                //update gameboard cell with uservalue
                GameBoard.getInstance().getCell(x, y).setStartValue(cellValue);
                GameBoard.getInstance().getCell(x, y).setAnswerValue(cellValue);
            }else{
                //Error toast and reset to ""
                Toast.makeText(getApplicationContext(),"Please use numbers between 1-9" ,
                            Toast.LENGTH_SHORT).show();
                cellText.setText("");
            }
        }else{
            GameBoard.getInstance().getCell(x, y).setStartValue(0);
            GameBoard.getInstance().getCell(x, y).setAnswerValue(0);
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        //Log.d("FOCUS CHANGE ","CALLED");
        validateExampleGrid();
        markNonSymmetrical();
        if (hasFocus) {
            v.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.cellHighlightGrey,null));
        }
        if (oldFocus != null){
            validateCellInput(oldFocus);
        }
        oldFocus = v;
    }

    private View.OnClickListener cellValueChanged = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Log.d("OnClick ","CALLED");
            validateCellInput(v);
        }
    };

    //This is called when the Play Button is clicked
    public void createSudoku(View view) {
        //Check the grid has no conflicts
        if(validateExampleGrid()){
            GameBoard.getInstance().solve();
            Intent intent = new Intent(this, PlaySudokuActivity.class);
            startActivity (intent);
            finish();
        }else{
            Toast.makeText(getApplicationContext(),"Please ensure the Sudoku is Valid, Errors will be in Red" ,
                    Toast.LENGTH_LONG).show();
        }
    }

    //This is called when the Back Button is clicked
    public void closeActivity(View view) {
        finish();
    }
}
