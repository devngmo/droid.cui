package com.tml.libs.cui.drawing;

import android.graphics.Canvas;
import android.view.MotionEvent;

import com.tml.libs.cui.drawing.PVAGridSpaceAdapter;
import com.tml.libs.cui.drawing.PaintView;

/**
 * Created by TML on 16/02/2017.
 */

public class PVACardLayout extends PVAGridSpaceAdapter {


    public PVACardLayout(PaintView v) {
        super(v);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);



    }

//    @Override
//    public void onNodeUpdated(NodeBase node) {
//        //super.onNodeUpdated(node);
//        getView().invalidate();
//    }


}
