package com.tml.libs.cui.drawing;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by TML on 16/02/2017.
 */

public class PaintView extends View {
    PaintViewAdapter pva;
    int szW = 0;
    int szH = 0;

    public int getSzH() {
        return szH;
    }

    public int getSzW() {
        return szW;
    }

    public void setAdapter(PaintViewAdapter adapter) {
        pva = adapter;
    }

    public PaintViewAdapter getAdapter() { return pva; }

    public PaintView(Context context) {
        this(context, null);
    }

    public PaintView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PaintView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (pva != null)
            pva.onDraw(canvas);
        else {
            Log.w("PaintView", "onDraw: PaintView have no adapter");
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (pva != null) {
            boolean handled = pva.onTouchEvent(event);
                return handled;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        szH = h;
        szW = w;
    }
}
