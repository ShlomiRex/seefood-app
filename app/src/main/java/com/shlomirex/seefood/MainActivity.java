package com.shlomirex.seefood;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final int CAPTURE_IMAGE_CALLBACK = 100;
    private String currentPhotoPath;

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

        Context context = this;
        btn_screenshot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Camera", "Screenshot button clicked");

                // Create the camera_intent ACTION_IMAGE_CAPTURE it will open the camera for capture the image
                Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                File storageDir = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                //File image = File.createTempFile("photo", ".jpg", storageDir);
                File image = new File(storageDir, "photo.jpg");
                currentPhotoPath = image.getAbsolutePath();
                Log.d("Camera", "Photo path: " + currentPhotoPath);
                Uri imageUri = FileProvider.getUriForFile(context,
                        "com.shlomirex.seefood.fileprovider",
                        image);
                camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

                // Start the activity with camera_intent, and request pic id
                startActivityForResult(camera_intent, CAPTURE_IMAGE_CALLBACK);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAPTURE_IMAGE_CALLBACK) {
            Log.d("onActivityResult", "Photo taken");

            //Bitmap photo = BitmapFactory.decodeFile(currentPhotoPath);

            Intent intent = new Intent(this.getApplicationContext(), EvaluatingActivity.class);
            intent.putExtra("photoPath", currentPhotoPath);

            startActivity(intent);

        } else {
            Log.e("onActivityResult", "Invalid request code");
            Toast.makeText(this, "Internal error", Toast.LENGTH_SHORT).show();
        }
    }
}
