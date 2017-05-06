package com.cs39440.rob41.sudokuapp;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Vishal Shrestha on 09/2016
 * Available at http://www.thecodecity.com/2016/09/creating-ocr-android-app-using-tesseract.html
 * Edited by Robert Bayliss 09/04/2016
 * Added - resultsFilter method and also modified TessOCR & getOCRResult methods
 */

public class TessOCR {
    private String datapath;
    private TessBaseAPI tess;

    public TessOCR(Context context) {
        // TODO Auto-generated constructor stub        this.context = context;
        datapath = Environment.getExternalStorageDirectory().toString();
        File dir = new File(datapath + "/tessdata/");
        File file = new File(datapath + "/tessdata/" + "eng.traineddata");
        boolean success;
        if (!file.exists()) {
            Log.d("mylog", "in file doesn't exist");
            success= dir.mkdirs();
            copyFile(context);
            Log.d("mylog", String.valueOf(success));
        }

        tess = new TessBaseAPI();
        String language = "eng";
        tess.init(datapath, language);
        //White line only the numbers we are looking for
        tess.setVariable(TessBaseAPI.VAR_CHAR_WHITELIST,"123456789");
    }

    public String getOCRResult(Bitmap bitmap, int cellheight) {
        tess.setImage(bitmap);
        String result = tess.getUTF8Text();
        String charboxes = tess.getBoxText(0);
        if (result.equals("") || result.equals(" ")  || result==null){
            return "0";
        }
        if (result.length() == 1 ){
            return result;
        }
        Log.d("Posible Chars", result);
        return resultsFilter (charboxes, cellheight);
    }

    public String getDatapath() {
        return datapath;
    }

    private String resultsFilter(String boxText, int cellheight){
        /*
        Example input" 2 19 0 57 76 0
                       1 63 6 71 11 0"
         1st value is the number identified by OCR
         2nd and 3rd are the top left coordinates of a box
         4th and 5th are the bottom right coordinates of a box
         if (Val 2 + val 3) - (Val 4 + val 5) > 40 then valid
         */
        Log.d("Filtering Chars", boxText +" and cell height= "+cellheight);
        String correctValue = "0";
        //Log.d("boxtext ", boxText);
        String substrings[] = boxText.split("\\r\\n|\\n|\\r");
        //For each possible values
        for (String substring:substrings) {
            //Log.d("substring ", substring);
            String values[] = substring.split(" ");
            //check value is a number and not the entire height of the cell
            int possGridLine = Integer.valueOf(values[1])+Integer.valueOf(values[4]);
            if(android.text.TextUtils.isDigitsOnly(values[0])
                    && !((Integer.valueOf(values[4])>=cellheight) && ((Integer.valueOf(values[1])<7)))) {
                //If the character is large enough
                if ((Integer.valueOf(values[3]) + Integer.valueOf(values[4])) -
                        (Integer.valueOf(values[1]) + Integer.valueOf(values[2])) > 40) {
                    //Log.d("substring RETURNING", values[0]);
                    return values[0];
                }
            }
        }
        return correctValue;
    }

    private void copyFile(Context context) {
        AssetManager assetManager = context.getAssets();
        try {
            InputStream in = assetManager.open("eng.traineddata");
            OutputStream out = new FileOutputStream(datapath + "/tessdata/" + "eng.traineddata");
            byte[] buffer = new byte[1024];
            int read = in.read(buffer);
            while (read != -1) {
                out.write(buffer, 0, read);
                read = in.read(buffer);
            }
        } catch (Exception e) {
            Log.d("mylog", "couldn't copy with the following error : "+e.toString());
        }
    }



}
