package com.shlomirex.seefood;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class OutlinedTextView extends androidx.appcompat.widget.AppCompatTextView {
    private int outlineColor;
    private int outlineWidth;

    public OutlinedTextView(@NonNull Context context) {
        super(context);
    }

    public OutlinedTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public OutlinedTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.outlineColor = attrs.getAttributeIntValue(R.attr.outlineColor, Color.BLACK);
        this.outlineWidth = attrs.getAttributeIntValue(R.attr.outlineWidth, 20);
    }

    @Override
    public void draw(Canvas canvas) {
        // Snapshot current paint settings
        Paint paint = getPaint();
        int originalTextColor = getCurrentTextColor();

        // First draw the outline
        setTextColor(outlineColor);
        paint.setStrokeWidth(outlineWidth);
        paint.setStyle(Paint.Style.STROKE);
        super.draw(canvas);

        // Then draw the text
        setTextColor(originalTextColor);
        paint.setStyle(Paint.Style.FILL);
        super.draw(canvas);

        paint.setColor(originalTextColor);
    }
}
