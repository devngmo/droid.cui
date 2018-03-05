package com.tml.libs.cui.factories;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.View;

/**
 * Created by TML on 4/24/2017.
 */

public class ButtonFactory {
    public static Drawable createGradientButton(int bgColor, int borderColor, int strokeWidth) {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadii(new float[] { 2,2,2,2,2,2,2,2 });
        shape.setColor(bgColor);
        shape.setStroke(strokeWidth, borderColor);
        return shape;
    }
}
