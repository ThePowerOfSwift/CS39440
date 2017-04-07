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
 * Created by Vishal Shrestha 09/2016
 * Available at http://www.thecodecity.com/2016/09/creating-ocr-android-app-using-tesseract.html
 */

public class TessOCR {
    private String datapath;
    private TessBaseAPI mTess;
    Context context;

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

        mTess = new TessBaseAPI();
        String language = "eng";
        mTess.init(datapath, language);
        //White line only the numbers we are looking for
        mTess.setVariable(TessBaseAPI.VAR_CHAR_WHITELIST,"123456789");

    }

    public void stopRecognition() {
        mTess.stop();
    }

    public String getOCRResult(Bitmap bitmap) {
        mTess.setImage(bitmap);
        String result = mTess.getUTF8Text();
        if (result.equals("") || result.equals(" ")  || result==null){
            return "0";
        }
        return result;
    }

    public void onDestroy() {
        if (mTess != null)
            mTess.end();
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
