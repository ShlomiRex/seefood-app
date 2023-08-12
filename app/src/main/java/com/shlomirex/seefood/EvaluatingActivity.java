package com.shlomirex.seefood;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class EvaluatingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluating);

        if (getIntent().getExtras() == null) {
            Log.e("EvaluatingActivity", "No extras in intent");
            Toast.makeText(this, "Internal error", Toast.LENGTH_SHORT).show();
            return;
        } else {
            Log.d("EvaluatingActivity", "Got photo");
        }

        // Change the background to the photo
        Bitmap photo = (Bitmap) getIntent().getExtras().get("photo");
        findViewById(R.id.evaluatingActivity).setBackground(new BitmapDrawable(getResources(), photo));
    }
}
