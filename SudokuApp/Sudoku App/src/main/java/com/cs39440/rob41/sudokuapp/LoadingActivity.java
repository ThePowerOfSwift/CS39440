package com.cs39440.rob41.sudokuapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class LoadingActivity extends Activity implements View.OnFocusChangeListener {
    private final int gridSize = 9;
    private GridLayout gridLayout;
    private View oldFocus = null;
    private Bitmap croppedSudoku;
    Map<Integer, Points> cellLookUpTable = new HashMap<Integer, Points>();

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
            finish();
            startActivity (intent);
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
    }

    private void createFromImage(String imagePath) {
        Log.d("createFromImage: ","Started, Using img: "+imagePath);
        //Mat sudokuOriginal = new Mat();

        //Fetch the image from the path
        //Bitmap bmImg = BitmapFactory.decodeFile(imagePath);
        Bitmap bmImg = BitmapFactory.decodeResource(getResources(),R.drawable.test1);

        if (bmImg == null) {
            Log.d("CreateFromImage ", "Failed reading image");
        }else{
            final ImageClassifier classifier = new ImageClassifier(bmImg, gridSize, LoadingActivity.this);
            croppedSudoku = classifier.getProcessedImg();
            //ImageView img = (ImageView) findViewById(R.id.capturedImage);
            //img.setImageBitmap(croppedSudoku);
            if (classifier.getCornersUnordered()!= null && classifier.getCornersUnordered().length == 4) {

                GameBoard.getInstance().createCells(classifier.getCellValues());

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
                LinearLayout layout = (LinearLayout) findViewById(R.id.templateHolder);
                layout.post(new Runnable() {
                    @Override
                    public void run() {
                        if (classifier.getCornersUnordered()== null) {
                            Log.d("CreateFromImage", "No Corners found");
                        }else{
                            Log.d("CreateFromImage", String.valueOf(classifier.getCornersUnordered().length)+" Corners found");
                        }
                        //createExampleGrid();
                        updateDisplay();
                    }
                });
                //finish();
            }
        }
    }

    private void createExampleGrid(){
        gridLayout = (GridLayout)findViewById(R.id.templateGrid);

        int gridSize = updateDisplay();
        Log.d("gridSize",String.valueOf(gridSize));
        int numOfCol = gridLayout.getColumnCount();
        int numOfRow = gridLayout.getRowCount();
        int cellSize = gridSize/9;

        for(int yPos=0; yPos<numOfRow; yPos++){
            for(int xPos=0; xPos<numOfCol; xPos++){
                int id = (yPos*9)+xPos;
                boolean isStartValue = GameBoard.getInstance().getCell(xPos,yPos).getStartValue();
                LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService
                        (LAYOUT_INFLATER_SERVICE);
                EditText cellText = (EditText) inflater.inflate(R.layout.cell_edit_text,null);
                //Set the ID
                cellText.setId(id);
                //Size of the cell
                cellText.setWidth(cellSize);
                cellText.setHeight(cellSize);
                //Limit input to 1
                cellText.setFilters(new InputFilter[] {new InputFilter.LengthFilter(1)});
                if (isStartValue) {
                    cellText.setText(String.valueOf
                            (GameBoard.getInstance().getCell(xPos,yPos).getAnswerValue()));
                }else{
                    cellText.setText(String.valueOf(""));
                }
                cellText.setTextColor(Color.parseColor("#111111"));

                //Add Listeners..
                // ...so when clicked on it's highlighted
                cellText.setOnFocusChangeListener(this);
                // ...so when datas entered it's valided and passed to the gameboard
                cellText.setOnClickListener(cellValueChanged);

                cellLookUpTable.put(id,new Points(xPos,yPos));
                gridLayout.addView(cellText);
            }
        }
        markNonSymmetrical();
        validateExampleGrid();
    }

    private boolean validateExampleGrid(){
        int numOfCol = gridLayout.getColumnCount();
        int numOfRow = gridLayout.getRowCount();
        int count = 0;
        boolean isValid = true;
        for(int yPos=0; yPos<numOfRow; yPos++) {
            for (int xPos = 0; xPos < numOfCol; xPos++) {
                EditText cellText = (EditText)findViewById(count);
                String cellString = cellText.getText().toString();
                if (!cellString.equals("")) {
                    int currentValue = Integer.parseInt(cellString);
                    Points cellRef = cellLookUpTable.get(count);
                    int x = cellRef.getX();
                    int y = cellRef.getY();
                    if (!GameBoard.getInstance().validCellValue(currentValue, GameBoard.getInstance().getCell(x, y))) {
                        //Set Text red
                        cellText.setTextColor(Color.parseColor("#8c1500"));
                        isValid = false;
                    }else{
                        cellText.setTextColor(Color.parseColor("#111111"));
                    }
                }
                count++;
            }
        }
        return isValid;
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
            //If they both
            if (cellText.getText().length() != cellText2.getText().length()) {
                cellText.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.cellhighlightred,null));
                cellText2.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.cellhighlightred,null));
            }else{
                cellText.setBackgroundColor(Color.TRANSPARENT);
                cellText2.setBackgroundColor(Color.TRANSPARENT);
            }
            y++;
            //Prevents out of bounds (left/right of grid) resets to center of next row
            if(y == 5){
                y = -4;
                x++;
            }
            //Prevents out of bounds (top/bottom of grid)
            if (x == 5){
                complete = true;
            }
        }
    }

    private int coordToInt(int x, int y){
        int id = (x*9)+y;
        return id;
    }

    //Resize the photo and grid to best fit the Screen
    private int updateDisplay(){
        LinearLayout layout = (LinearLayout)findViewById(R.id.templateHolder);
        LinearLayout btnLayout = (LinearLayout)findViewById(R.id.templateButtonHolder);
        gridLayout = (GridLayout)findViewById(R.id.templateGrid);
        //Display the cropped Photo and ensure the size is 1024x1024
        ImageView img = (ImageView) findViewById(R.id.capturedImage);
        img.setImageBitmap(Bitmap.createScaledBitmap(croppedSudoku, 1024, 1024, false));

        int gridWidth = layout.getWidth();
        int gridHeight = layout.getHeight()/2 - btnLayout.getHeight();

        //Find which is the shortest and set both to that to ensure it's square
        int dimensions = Math.min(gridWidth,gridHeight);

        ViewGroup.LayoutParams layoutParams = gridLayout.getLayoutParams();
        layoutParams.height = dimensions;
        layoutParams.width = dimensions;
        gridLayout.setLayoutParams(layoutParams);

        ViewGroup.LayoutParams imglayoutParams = img.getLayoutParams();
        imglayoutParams.height = dimensions;
        imglayoutParams.width = dimensions;
        img.setLayoutParams(imglayoutParams);

        return dimensions;
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
                GameBoard.getInstance().getCell(x, y).setStartValue(true);
                GameBoard.getInstance().getCell(x, y).setAnswerValue(cellValue);
            }else{
                //Error toast and reset to ""
                Toast.makeText(getApplicationContext(),"Please use numbers between 1-9" ,
                            Toast.LENGTH_SHORT).show();
                cellText.setText("");
            }
        }else{
            GameBoard.getInstance().getCell(x, y).setStartValue(false);
            GameBoard.getInstance().getCell(x, y).setAnswerValue(0);
        }
    }

    //region Listeners
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        //Log.d("FOCUS CHANGE ","CALLED");
        validateExampleGrid();
        markNonSymmetrical();
        if (hasFocus) {
            v.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.cellhighlightgrey,null));
        }
        if (oldFocus != null){
            oldFocus.setBackground(null);
            validateCellInput(oldFocus);
        }
        oldFocus = v;
    }

    private OnClickListener cellValueChanged = new OnClickListener() {
        @Override
        public void onClick(View v) {
            validateCellInput(v);
        }
    };
    //endregion

    //region Button Methods
    //This is called when the Play Button is clicked
    public void createPlaySudoku(View view) {
        //Check the grid has no conflicts
        if(validateExampleGrid()){
            GameBoard.getInstance().solve();
            Intent intent = new Intent(this, PlaySudokuActivity.class);
            startActivity (intent);
            finish();
        }else{
            Toast.makeText(getApplicationContext(),
                    "Please ensure the Sudoku is Valid, Errors will be highlighted in Red" ,
                    Toast.LENGTH_LONG).show();
        }
    }

    //This is called when the Back Button is clicked
    public void closeActivity(View view) {
        finish();
    }
    //endregion
}
