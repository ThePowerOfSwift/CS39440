package com.cs39440.rob41.sudokuapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Prommy on 04/03/2017.
 */

public class SudokuGrid extends BaseAdapter {
    private Context context;
    private int [] cells;

    public SudokuGrid (Context paramContext, int [] paramCell){
        context = paramContext;
        cells = paramCell;
    }

    @Override
    public int getCount() {
        return cells.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View sudokuGrid;

        if (convertView == null){
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            sudokuGrid = layoutInflater.inflate(R.layout.sudoku_cell, null);
            //Log.d("y","edittxt : "+editText.getId());
            //Log.d("y","sudokuGrid : "+sudokuGrid.getId());
        }else{
            sudokuGrid = (View) convertView;
        }
        TextView editText = (TextView) sudokuGrid.findViewById(R.id.cellText);
        Log.d("y","cells[position] : "+cells[position]);
        editText.setText(String.valueOf(cells[position]));
        return sudokuGrid;
    }
}
