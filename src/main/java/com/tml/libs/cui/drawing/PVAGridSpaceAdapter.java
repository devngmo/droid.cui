package com.tml.libs.cui.drawing;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import com.tml.libs.cutils.MathUtils;
import com.tml.libs.cutils.StaticLogger;

/**
 * Created by TML on 16/02/2017.
 */

public class PVAGridSpaceAdapter extends PaintViewAdapter {
    protected ScaleGestureDetector scaleGestureDetector;
    GestureDetector gestureDetector;
    protected Point camWorldCenter = null;
    protected Paint pGridLine;
    protected Paint pGridAxis;
    protected Paint pGridRulerText;

    protected int scrW = 0;
    protected int scrH = 0;

    protected boolean showAxis = false;

    protected boolean isZooming = false;
    protected float zoomRatio = 1;
    private boolean isMoveCamera = true;

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
        allowZooming = true;
        scaleGestureDetector = new ScaleGestureDetector(v.getContext(), new ScaleGestureDetector.OnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                if (allowZooming) {
                    zoomRatio = MathUtils.clamp(zoomRatio * detector.getScaleFactor(), 0.1f, 5f);
                    //D("zoom ratio " + zoomRatio);
                    pv.invalidate();
                }
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
        gestureDetector = new GestureDetector(v.getContext(), new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {
                handleGestureOnShowPress(e);
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return handleGestureOnSingleTapUp(e);
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                handleGestureOnLongPress(e);
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return false;
            }
        });
        StaticLogger.enableLog(this);
        bgColor = 0xffffffff;

        pGridLine = new Paint();
        pGridLine.setStyle(Paint.Style.STROKE);
        pGridLine.setColor(0x66000000);

        pGridAxis = new Paint();
        pGridAxis.setStyle(Paint.Style.STROKE);
        pGridAxis.setColor(0x88008800);

        pGridRulerText = new Paint();
        pGridRulerText.setStyle(Paint.Style.STROKE);
        pGridRulerText.setColor(0x66000000);
        pGridRulerText.setTextSize(getGridFontSize());
    }

    public void handleGestureOnShowPress(MotionEvent e) {

    }

    public void handleGestureOnLongPress(MotionEvent e) {

    }

    public boolean handleGestureOnSingleTapUp(MotionEvent e) {
        return false;
    }

    protected boolean drawDebug = false;

    @Override
    public void onDraw(Canvas c) {
        super.onDraw(c);
        calcScreenParams(c);
        drawGrid(c);

        if (drawDebug) {
            Paint p = new Paint();
            p.setColor(Color.BLUE);
            p.setTextSize(40);
            c.drawText("Zoom " + zoomRatio , 10, 50, p);
        }
    }

    protected int vpLeft;
    protected int vpTop;

    private void calcScreenParams(Canvas c) {
        scrW = c.getWidth();
        scrH = c.getHeight();

        if (camWorldCenter == null)
            camWorldCenter = new Point(scrW/2, scrH/2);

        // zoomed viewport
        float zmW = scrW / zoomRatio;
        float zmH = scrH / zoomRatio;
        vpLeft = camWorldCenter.x - (int)(zmW/2);
        vpTop = camWorldCenter.y - (int)(zmH/2);
    }

    private void drawGrid(Canvas c) {
        pGridRulerText.setTextAlign(Paint.Align.RIGHT);

        if (showAxis) {
            float axisXScreen = w2sX(0);
            float axisYScreen = w2sY(0);
            c.drawLine(0, axisYScreen, scrW, axisYScreen, pGridAxis);
            c.drawLine(axisXScreen, 0, axisXScreen, scrH, pGridAxis);
        }

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
        return (x - vpLeft) * zoomRatio;
    }

    private float w2sY(int y) {
        return (y - vpTop) * zoomRatio;
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
        gestureDetector.onTouchEvent(e);

        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = curX;
                downY = curY;
                oldCamX = camWorldCenter.x;
                oldCamY = camWorldCenter.y;

                notifyOnTouchDown();
                break;

            case MotionEvent.ACTION_MOVE:
                if (isZooming) return false;
                int dx = curX - downX;
                int dy = curY - downY;

                boolean handled = handleOnTouchMove(dx, dy);
                if (!handled) {
                    camWorldCenter.x = (int) MathUtils.clamp(oldCamX - (dx / zoomRatio), 0, getWorldWidth());
                    camWorldCenter.y = (int) MathUtils.clamp(oldCamY - (dy / zoomRatio), 0, getWorldHeight());
                }

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (e.getPointerCount() == 1)
                    isZooming = false;
        }
        requestRender();


        return true;

    }

    protected void notifyOnTouchDown() {

    }

    protected boolean handleOnTouchMove(int dx, int dy) {
        return false;
    }

    private void requestRender() {
        pv.invalidate();
    }
}
