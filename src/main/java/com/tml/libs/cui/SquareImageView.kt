package com.tml.libs.cui

import android.content.Context
import android.util.AttributeSet
import android.view.ContextMenu
import android.widget.ImageView

open class SquareImageView(context: Context?) : ImageView(context) {

    fun SquareImageView(context: Context?, attrs: AttributeSet?) {
        ImageView(context, attrs)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(measuredWidth, measuredWidth) //Snap to width
    }
}
