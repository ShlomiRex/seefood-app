package com.shlomirex.seefood;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
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
    private File photoFile;
    private Uri imageUri;

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

                photoFile = new File(Environment.getExternalStorageDirectory().toURI());

                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "New Picture");
                values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");

                imageUri = getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                camera_intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

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

            if (data != null && data.getExtras() != null) {
                Bitmap photo = null;
                try {
                    photo = MediaStore.Images.Media.getBitmap(
                            getContentResolver(), imageUri);
                } catch (IOException e) {
                    Toast.makeText(this, "Internal error", Toast.LENGTH_SHORT).show();
                    throw new RuntimeException(e);
                }


                //Bitmap photo = (Bitmap) data.getExtras().get("data");

                Intent intent = new Intent(this.getApplicationContext(), EvaluatingActivity.class);
                intent.putExtra("photo", photo);
                startActivity(intent);
            } else {
                Log.e("onActivityResult", "Photo is null");
            }
        } else {
            Log.e("onActivityResult", "Invalid request code");
            Toast.makeText(this, "Internal error", Toast.LENGTH_SHORT).show();
        }
    }
}
