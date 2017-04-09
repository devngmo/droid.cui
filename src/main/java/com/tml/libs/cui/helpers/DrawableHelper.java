package com.tml.libs.cui.helpers;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * Created by TML on 20/02/2017.
 */

public class DrawableHelper {
    private static final String TAG = "DrawableHelper";
    /**
     *
     * @param hexColor = ffffffff  (ARGB)
     * @return
     */
    public static Drawable createColorDrawable(String hexColor) {
        ColorDrawable cd = new ColorDrawable();
        cd.setColor((int)Long.parseLong(hexColor, 16));
        return cd;
    }
}
