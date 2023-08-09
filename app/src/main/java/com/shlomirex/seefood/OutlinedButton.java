package com.shlomirex.seefood;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class OutlinedButton extends androidx.appcompat.widget.AppCompatButton {

    private int outlineColor;
    private int outlineWidth;

    public OutlinedButton(@NonNull Context context) {
        super(context);
    }

    public OutlinedButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public OutlinedButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
//        outlineColor;
//        outlineWidth;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }
}
