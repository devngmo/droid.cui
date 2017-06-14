package com.tml.libs.cui.cardlist;

import android.graphics.drawable.Drawable;
import android.util.Log;

import com.tml.libs.cutils.LoggableClass;

/**
 * Created by TML on 04/01/2017.
 */

public class CardListItemModel extends LoggableClass {
    private static final String TAG = "CardListItemModel";
    public static final String VSNAME_NORMAL = "normal";
    public static final String VSNAME_SELECTED = "selected";
    public String bgNormalVSName = VSNAME_NORMAL;
    public String bgSelectedVSName = VSNAME_SELECTED;
    CardListAdapter.CardItemHolder itemHolder;

    Drawable customBg = null;
    public String getID() {
        return null;
    }

    public void onBindView(int position, CardListAdapter.CardItemHolder cardItemHolder) {
        itemHolder = cardItemHolder;
    }

    public void setBgDrawable(Drawable bg) {
        if (itemHolder == null) return;
        if (itemHolder.itemView == null) return;
        itemHolder.itemView.setBackground(bg);
    }


    public boolean hasCustomBg() {
        return customBg != null;
    }

    public void showCustomBg() {
        setBgDrawable(customBg);
    }

    public void onSelect() {
        if (hasCustomBg())
            setBgDrawable(customBg);
    }

    public void onDeselect() {

    }
}
