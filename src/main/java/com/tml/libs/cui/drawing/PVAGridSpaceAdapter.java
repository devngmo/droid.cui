package com.tml.libs.cui.drawing;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import com.tml.libs.cutils.MathUtils;
import com.tml.libs.cutils.StaticLogger;

/**
 * Created by TML on 16/02/2017.
 */

public class PVAGridSpaceAdapter extends PaintViewAdapter {
    protected ScaleGestureDetector scaleGestureDetector;
    protected Point camWorldCenter = null;
    protected Paint pGridLine;
    protected Paint pGridRulerText;

    protected int scrW = 0;
    protected int scrH = 0;

    protected boolean isZooming = false;
    protected float zoomRatio = 1;

    public int getGridTileSize() { return 50; }
    public int getGridFontSize() { return 20; }

    public int getGridWidth() {
        return scrW;
    }

    public int getGridHeight() {
        return scrH;
    }

    public int getWorldWidth() {
        return scrW;
    }

    public int getWorldHeight() {
        return scrH;
    }

    public int getGridCols() {return getGridWidth() / getGridTileSize(); }
    public int getGridRows() {return getGridHeight() / getGridTileSize(); }

    public PVAGridSpaceAdapter(PaintView v) {
        super(v);
        scaleGestureDetector = new ScaleGestureDetector(v.getContext(), new ScaleGestureDetector.OnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                zoomRatio = MathUtils.clamp(zoomRatio * detector.getScaleFactor(), 0.1f, 5f);
                //D("zoom ratio " + zoomRatio);
                pv.invalidate();
                return true;
            }

            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector) {
                isZooming = true;
                return true;
            }

            @Override
            public void onScaleEnd(ScaleGestureDetector detector) {

            }
        });

        StaticLogger.enableLog(this);
        bgColor = 0xffffffff;

        pGridLine = new Paint();
        pGridLine.setStyle(Paint.Style.STROKE);
        pGridLine.setColor(0x66000000);

        pGridRulerText = new Paint();
        pGridRulerText.setStyle(Paint.Style.STROKE);
        pGridRulerText.setColor(0x66000000);
        pGridRulerText.setTextSize(getGridFontSize());
    }


    @Override
    public void onDraw(Canvas c) {
        super.onDraw(c);
        calcScreenParams(c);
        drawGrid(c);
    }

    protected int vpLeft;
    protected int vpTop;

    private void calcScreenParams(Canvas c) {
        scrW = c.getWidth();
        scrH = c.getHeight();

        if (camWorldCenter == null)
            camWorldCenter = new Point(scrW/2, scrH/2);

        vpLeft = camWorldCenter.x - (int)(zoomRatio * scrW/2);
        vpTop = camWorldCenter.y - (int)(zoomRatio * scrH/2);
    }

    private void drawGrid(Canvas c) {
        pGridRulerText.setTextAlign(Paint.Align.RIGHT);

        for (int r = 0; r <= getGridRows(); r++) {
            c.drawLine(w2sX(0), w2sY(r * getGridTileSize()),
                    w2sX(getGridWidth()), w2sY(r * getGridTileSize()), pGridLine);
            String rowNum = "" + (r + 1);
            c.drawText(rowNum, w2sX(- 10), w2sY( r * getGridTileSize()) + getGridFontSize()/2, pGridRulerText);
        }

        for (int col = 0; col <= getGridCols(); col++) {
            c.drawLine(w2sX( col * getGridTileSize()), w2sY(0),
                    w2sX( col * getGridTileSize()), w2sY(getGridHeight()), pGridLine);
        }
    }

    private float w2sX(int x) {
        return (x - vpLeft) / zoomRatio;
    }

    private float w2sY(int y) {
        return (y - vpTop) / zoomRatio;
    }

    protected int downX = 0;
    protected int downY = 0;
    protected int curX = 0;
    protected int curY = 0;
    protected int oldCamX = 0;
    protected int oldCamY = 0;
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        curX = (int)e.getX();
        curY = (int)e.getY();

        scaleGestureDetector.onTouchEvent(e);

        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = curX;
                downY = curY;
                oldCamX = camWorldCenter.x;
                oldCamY = camWorldCenter.y;
                break;

            case MotionEvent.ACTION_MOVE:
                if (isZooming) return false;
                int dx = curX - downX;
                int dy = curY - downY;
                camWorldCenter.x = (int)MathUtils.clamp(  oldCamX - zoomRatio *(dx), 0, getWorldWidth());
                camWorldCenter.y = (int)MathUtils.clamp(  oldCamY - zoomRatio *(dy), 0, getWorldHeight());

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (e.getPointerCount() == 1)
                    isZooming = false;
        }
        requestRender();
        return true;

    }

    private void requestRender() {
        pv.invalidate();
    }
}
