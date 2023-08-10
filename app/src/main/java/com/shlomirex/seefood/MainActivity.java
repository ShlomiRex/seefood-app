package com.shlomirex.seefood;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.hardware.Camera;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST_CODE = 100;

    private Bitmap photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View overlayView = findViewById(R.id.overlayView);
        View txt_letsGetStarted = findViewById(R.id.textView_letsGetStarted);
        View txt_touchToSeeFood = findViewById(R.id.textView_touchToSeeFood);
        Button btn_screenshot = findViewById(R.id.btn_screenshot);

        txt_touchToSeeFood.setVisibility(View.GONE);
        btn_screenshot.setVisibility(View.GONE);

        overlayView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (overlayView.getVisibility() == View.VISIBLE) {
                    Log.d("Touch", "Overlay is visible, hiding it");
                    overlayView.setVisibility(View.GONE);
                    txt_letsGetStarted.setVisibility(View.GONE);
                    txt_touchToSeeFood.setVisibility(View.VISIBLE);
                    btn_screenshot.setVisibility(View.VISIBLE);
                }
            }
        });

        btn_screenshot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Camera", "Screenshot button clicked");

                // Create the camera_intent ACTION_IMAGE_CAPTURE it will open the camera for capture the image
                Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Start the activity with camera_intent, and request pic id
                startActivityForResult(camera_intent, CAMERA_REQUEST_CODE);
            }
        });
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE) {
            Log.d("onActivityResult", "Photo taken");
            photo = (Bitmap) data.getExtras().get("data");
        } else {
            Log.d("onActivityResult", "Photo not taken");
        }
    }
}