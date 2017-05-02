package com.cs39440.rob41.sudokuapp;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import org.opencv.android.OpenCVLoader;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainMenuActivity extends Activity {
    String currentPhotoPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        if (!OpenCVLoader.initDebug()) {
            Log.d(this.getClass().getSimpleName(), "  OpenCVLoader.initDebug(), not working.");
        } else {
            Log.d(this.getClass().getSimpleName(), "  OpenCVLoader.initDebug(), working.");
        }
        //If the build version doesn't automatically assign permissions request them from user
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    //region Buttons to next activity
    public void openCamera(View View){
        Intent camIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (camIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.d("Photo File Creation: ","Failure");
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.cs39440.rob41.sudokuapp.fileprovider",photoFile);
                camIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(camIntent, 1);
            }
        }
    }

    public void openLoading(View View){
       // Button b = (Button) findViewById(R.id.createSudoku_button);;
        //b.setBackground(getResources().getDrawable(R.drawable.buttonshapepressed));
        //b.setBackgroundDrawable();
        Intent loadingIntent = new Intent(this, LoadingActivity.class);
        startActivity (loadingIntent);
        //b.setBackground(getResources().getDrawable(R.drawable.buttonshape));
    }

    public void openInformation(View View){
        Intent intent = new Intent(this, InformationActivity.class);
        startActivity (intent);
    }

    public void exit(View View){
        //if the API version is 21 or greater
        if (Build.VERSION.SDK_INT >= 21){
            finishAndRemoveTask();
        // If the API version is lower than 21
        }else{
            this.finishAffinity();
        }
    }
    //endregion

    //Runs once a picture has been taken
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //If the user took a picture direct to OCR element otherwise fall through to main menu
        if(resultCode == RESULT_OK) {

            Intent loadingIntent = new Intent(this, LoadingActivity.class);
            loadingIntent.putExtra("fromImage",true);
            loadingIntent.putExtra("imagePath",currentPhotoPath);
            startActivity(loadingIntent);
        }
    }

    //Requests Permission for Storage if not automatically provided
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        //If the array is empty the request was cancelled or denied
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Log.d("Requested Permissions: ","Granted");
            Button camBtn = (Button) findViewById(R.id.camera_button);
            camBtn.setEnabled(true);
        }else{
            Button camBtn = (Button) findViewById(R.id.camera_button);
            camBtn.setEnabled(false);
            Log.d("Requested Permissions: ","Denied");
            Toast.makeText(getApplicationContext(),"Camera feature disabled" ,
                    Toast.LENGTH_LONG).show();
        }
    }

    //From Android Developer Tutorial Found at:-
    //https://developer.android.com/training/camera/photobasics.html
    public File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
    //End of Android Developer Code
}
