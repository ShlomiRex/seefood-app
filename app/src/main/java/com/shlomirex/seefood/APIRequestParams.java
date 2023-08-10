package com.shlomirex.seefood;

import android.content.Context;
import android.graphics.Bitmap;

public class APIRequestParams {
    Bitmap photo;
    Context context;

    public APIRequestParams(Bitmap photo, Context context) {
        this.photo = photo;
        this.context = context;
    }
}
