package com.tml.libs.cui.drawing;

import android.graphics.Canvas;
import android.view.MotionEvent;

import com.tml.libs.cutils.LoggableClass;

/**
 * Created by TML on 16/02/2017.
 */

public class PaintViewAdapter extends LoggableClass {
    protected int bgColor = 0xffffffff;
    protected PaintView pv;

    protected boolean allowZooming = false;

    public void setAllowZooming(boolean allowZooming) {
        this.allowZooming = allowZooming;
    }

    public boolean isAllowZooming() {
        return allowZooming;
    }

    public PaintViewAdapter(PaintView v) {
        pv = v;
        pv.setAdapter(this);
    }

    public PaintView getView() {
        return pv;
    }

    public int getBgColor() {
        return bgColor;
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }

    public void onDraw(Canvas canvas) {
        canvas.drawColor(bgColor);
    }

    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }



}
