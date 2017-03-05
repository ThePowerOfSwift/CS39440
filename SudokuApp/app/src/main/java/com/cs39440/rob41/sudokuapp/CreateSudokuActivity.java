package com.cs39440.rob41.sudokuapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class CreateSudokuActivity extends Activity {
    GridView gridView;
    //String [][] gameData = new String [9][9];
    int [] basic = new int [81];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        for (int count = 0;  count < 81; count++){
            basic[count] = count;
            //Log.d("a",basic[count]);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_sudoku);
        gridView = (GridView)findViewById(R.id.sudokuGrid);
        gridView.setAdapter(new SudokuGrid(this,basic));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),
                        ((TextView) view.findViewById(R.id.cellText)
                        ).getText(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void closeActivity(View view) {
        finish();
    }


}
