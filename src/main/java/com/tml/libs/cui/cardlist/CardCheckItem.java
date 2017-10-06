package com.tml.libs.cui.cardlist;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tml.libs.cui.R;

/**
 * Created by TML on 04/01/2017.
 */

public class CardCheckItem extends CardListItemModel {
    private static final String TAG = "CardCheckItem";

    boolean checked = false;
    String text;
    int textColor = 0xff000000;


    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isChecked() {
        return checked;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public CardCheckItem(boolean checked, String text) {
        this.text = text;
        this.checked = checked;
    }

    public CardCheckItem(boolean checked, String text, int textColor) {
        this.text = text;
        this.checked = checked;
        this.textColor = textColor;
    }


    public void onBindView(int position, CardListAdapter.CardItemHolder cardItemHolder) {
        super.onBindView(position, cardItemHolder);

        TextView txt = ((TextView)cardItemHolder.itemView.findViewById(R.id.text1));
        txt.setText(getText());
        txt.setTextColor(textColor);

        ImageView iv = (ImageView)cardItemHolder.itemView.findViewById(R.id.img_check);
        if (isChecked())
            iv.setVisibility(View.VISIBLE);
        else
            iv.setVisibility(View.INVISIBLE);
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        if (itemHolder.itemView != null) {
            ((TextView)itemHolder.itemView.findViewById(R.id.text1)).setTextColor(textColor);
        }
    }
}
