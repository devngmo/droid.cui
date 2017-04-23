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

    public String getID() {
        return null;
    }

    public void onBindView(int position, CardListAdapter.CardItemHolder cardItemHolder) {
        itemHolder = cardItemHolder;
//        Log.w(TAG, "onBindView: NOT IMPLEMENTED");
    }

    public void setBgDrawable(Drawable bg) {
//        Log.w(TAG, "setBgDrawable: NOT IMPLEMENTED");
    }


    public boolean hasCustomBg() {
        return false;
    }
}
