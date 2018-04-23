package com.tml.libs.cui.cardlist;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import com.tml.libs.cutils.LoggableClass;
import com.tml.libs.cutils.StaticLogger;

/**
 * Created by TML on 04/01/2017.
 */

public class CardListItemModel {
    private static final String TAG = "CardListItemModel";
    public static final String VSNAME_NORMAL = "normal";
    public static final String VSNAME_SELECTED = "selected";
    public String bgNormalVSName = VSNAME_NORMAL;
    public String bgSelectedVSName = VSNAME_SELECTED;
    protected CardListAdapter.CardItemHolder itemHolder;

    Object data = null;
    public void setData(Object data) {
        this.data = data;
    }
    public Object getData() {
        return data;
    }

    protected Drawable customBg = null;
    public void setCustomBg(Drawable bg) {
        customBg = bg;
        setBgDrawable(bg);
    }

    public String getID() {
        return "" + position;
    }
    int position;

    public int getPosition() {
        return position;
    }

    public void onBindView(int position, CardListAdapter.CardItemHolder cardItemHolder) {
        StaticLogger.D("CardListItemModel::onBindView " + position + " hasBg " + hasCustomBg());
        this.position = position;
        itemHolder = cardItemHolder;
    }

    public void setBgDrawable(Drawable bg) {
        if (itemHolder == null) return;
        if (itemHolder.itemView == null) return;

        //StaticLogger.D("setBgDrawable " + getID());
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

    protected Resources getResource() {
        return itemHolder.itemView.getContext().getResources();
    }
}
