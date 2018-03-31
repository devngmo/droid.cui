package com.tml.libs.cui.cardlist;

import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.tml.libs.cui.R;

/**
 * Created by TML on 04/01/2017.
 */

public class CardText extends CardListItemModel {
    private static final String TAG = "CardText";


    Drawable customBg = null;
    String id, text;
    int textColor = 0xff000000;
    public boolean textAsHtml = false;
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

    View viewBg = null;
    public void onBindView(int position, CardListAdapter.CardItemHolder cardItemHolder) {
        viewBg = cardItemHolder.itemView;
        TextView txt = ((TextView)cardItemHolder.itemView.findViewById(R.id.text1));
        if (textAsHtml)
            txt.setText(Html.fromHtml(getText()));
        else
            txt.setText(getText());
        txt.setTextColor(textColor);

        if (customBg != null)
            viewBg.setBackground(customBg);
    }

    @Override
    public boolean hasCustomBg() {
        return customBg != null;
    }

    public void setBgDrawable(Drawable bg) {
        if (viewBg != null)
            viewBg.setBackground(bg);
    }


    public void setTextColor(int textColor) {
        this.textColor = textColor;
        if (viewBg != null) {
            ((TextView)viewBg.findViewById(R.id.text1)).setTextColor(textColor);
        }
    }
}
