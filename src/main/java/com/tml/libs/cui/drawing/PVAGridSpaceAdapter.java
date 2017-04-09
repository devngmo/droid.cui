package com.tml.libs.cui.drawing;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;

/**
 * Created by TML on 16/02/2017.
 */

public class PVAGridSpaceAdapter extends PaintViewAdapter {
    protected Point camOffset = new Point(-1, -1);
    protected Paint pGridline = new Paint();
    protected Point touchPos = new Point();
    protected Point oldCamOffset = new Point();
    protected boolean centerCam = true;

    public PVAGridSpaceAdapter(PaintView v) {
        super(v);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (centerCam) {
            centerCam = false;
            camOffset.x = canvas.getWidth() / 2;
            camOffset.y = canvas.getHeight() / 2;
        }

        CanvasHelper.drawGridAxis(canvas, camOffset, true);
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN :
                touchPos.x = (int)event.getX();
                touchPos.y = (int)event.getY();
                oldCamOffset.set(camOffset.x, camOffset.y);

                break;
            case MotionEvent.ACTION_MOVE :
                int dx = (int)event.getX() - touchPos.x;
                int dy = (int)event.getY() - touchPos.y;
                camOffset.x = oldCamOffset.x + dx;
                camOffset.y = oldCamOffset.y + dy;
                getView().invalidate();
                break;
        }
        return true;
    }
}
