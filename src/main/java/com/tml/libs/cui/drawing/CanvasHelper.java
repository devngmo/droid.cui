package com.tml.libs.cui.drawing;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;

/**
 * Created by TML on 16/02/2017.
 */
public class CanvasHelper {
    private static final String TAG = "CanvasHelper";
    private static final int GRID_SIZE = 20;
    private static final int GRID_COLOR = 0x11000000;
    private static final int AXIS_COLOR = 0xffff0000;

    public static void drawGridAxis(Canvas c, Point offset, boolean centerAtOffset) {
        Paint pAxis = new Paint();
        pAxis.setColor(AXIS_COLOR);
        Paint pGrid = new Paint();
        pGrid.setColor(GRID_COLOR);

        int minY = 0;
        int minX = 0;

        if (centerAtOffset) {
            minY = Math.abs(offset.y);
            while (minY > GRID_SIZE)
                minY -= GRID_SIZE;

            if (offset.y < 0)
                minY = GRID_SIZE - minY;

            minX = Math.abs(offset.x);
            while (minX > GRID_SIZE)
                minX -= GRID_SIZE;

            if (offset.x < 0)
                minX = GRID_SIZE - minX;

            Log.d(TAG, String.format("drawGridAxis: min %d,%d", minX, minY));
            int y = minY;
            while(y < c.getHeight()) {
                if (y == offset.y)
                    c.drawLine(0, y, c.getWidth(), y, pAxis);
                else
                    c.drawLine(0, y, c.getWidth(), y, pGrid);
                y += GRID_SIZE;
            }
            int x = minX;
            while(x < c.getWidth()) {
                if (x == offset.x)
                    c.drawLine(x, 0, x, c.getHeight(), pAxis);
                else
                    c.drawLine(x, 0, x, c.getHeight(), pGrid);
                x += GRID_SIZE;
            }
        }
    }
}
