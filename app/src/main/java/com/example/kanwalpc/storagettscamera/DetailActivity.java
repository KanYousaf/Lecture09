package com.example.kanwalpc.storagettscamera;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class DetailActivity extends AppCompatActivity {
    private ImageView show_image;
    private final static int REQ_CODE=140;
    private TextView display_data;
    private Scanner scan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        show_image=(ImageView)this.findViewById(R.id.display_captured_image);
        display_data=(TextView)this.findViewById(R.id.display_detail_record);
        readFromInternalFile();
    }

    public void readFromInternalFile(){
        try {
            String allText="";
            scan=new Scanner(openFileInput("gre_own_words.txt"));
            while(scan.hasNextLine()){
                String line=scan.nextLine();
                allText+=line;
            }
            display_data.setText(allText);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if(scan!=null){
            scan.close();
        }
    }





    public void captured_pressed(View view) {
        // Check permission for CAMERA
        if (ActivityCompat.checkSelfPermission(DetailActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            // Callback onRequestPermissionsResult interceptado na Activity MainActivity
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    DetailActivity.REQ_CODE);
        } else {
            // permission has been granted, continue as usual
            Intent camera_intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(camera_intent, REQ_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==REQ_CODE && resultCode==RESULT_OK) {
            //1. after capturing gets thumbnail, convert that into bitmap and display it
            Bundle extras = data.getExtras();
            Bitmap photoCapturedBitmap = (Bitmap) extras.get("data");
            show_image.setImageBitmap(photoCapturedBitmap);
        }
    }
}
