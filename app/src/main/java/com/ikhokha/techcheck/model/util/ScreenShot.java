package com.ikhokha.techcheck.model.util;

import android.graphics.Bitmap;
import android.view.View;

public class ScreenShot {
    public static Bitmap takeScreenShot(View view){
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        return bitmap;
    }

    public static Bitmap takeScreenShotOfRootView(View view){
        return takeScreenShot(view.getRootView());
    }
}
