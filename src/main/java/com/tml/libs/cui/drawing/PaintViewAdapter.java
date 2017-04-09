package com.tml.libs.cui.drawing;

import android.graphics.Canvas;
import android.view.MotionEvent;

/**
 * Created by TML on 16/02/2017.
 */

public class PaintViewAdapter {
    protected int bgColor = 0xffffffff;
    protected PaintView pv;

    public PaintViewAdapter(PaintView v) {
        pv = v;
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
