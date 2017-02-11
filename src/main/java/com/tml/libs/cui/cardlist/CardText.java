package com.tml.libs.cui.cardlist;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.tml.libs.cui.R;

/**
 * Created by TML on 04/01/2017.
 */

public class CardText extends CardListItemModel {
    private static final String TAG = "CardText";

    String id, text;
    public String getID() {
        return id;
    }

    public String getText() {
        return text;
    }

    public CardText(String id, String text) {
        this.id = id;
        this.text = text;
    }

    public void setText(String text) {
        this.text = text;
    }

    View viewBg;
    public void onBindView(int position, CardListAdapter.CardItemHolder cardItemHolder) {
        viewBg = cardItemHolder.itemView;
        ((TextView)cardItemHolder.itemView.findViewById(R.id.text1)).setText(getText());
    }

    public void setBgDrawable(Drawable bg) {
        viewBg.setBackground(bg);
    }


}
