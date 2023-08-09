package com.shlomirex.seefood;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View overlayView = findViewById(R.id.overlayView);
        View txt_letsGetStarted = findViewById(R.id.textView_letsGetStarted);
        View txt_touchToSeeFood = findViewById(R.id.textView_touchToSeeFood);
        View btn_screenshot = findViewById(R.id.btn_screenshot);

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
    }
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        int action = event.getAction();
//        switch (action) {
//            case MotionEvent.ACTION_DOWN:
//                // User pressed down on the screen
//                Log.d("Touch", "ACTION_DOWN at x: " + event.getX() + ", y: " + event.getY());
//                return true;
//            case MotionEvent.ACTION_UP:
//                // User released their finger
//                Log.d("Touch", "ACTION_UP at x: " + event.getX() + ", y: " + event.getY());
//                return true;
//            default:
//                return super.onTouchEvent(event);
//        }
//    }
}
