package com.tml.libs.cui.cardlist;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.tml.libs.cui.R;
import com.tml.libs.cutils.LoggableClass;
import com.tml.libs.cutils.StaticLogger;

/**
 * Created by TML on 04/01/2017.
 */

public class CardTitleDesc extends CardListItemModel {
    private static final String TAG = "CardTitleDesc";
    Object data = null;
    public boolean showDescAsHTML = false;

    public void setData(Object data) {
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    String title, desc;
    int textColor = 0xff000000;
    int descTextColor = 0xff000000;


    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public CardTitleDesc(String title, String desc) {
        this.title = title;
        this.desc= desc;
        StaticLogger.enableLog(this);
    }

    public void setTitle(String title) {
        this.title = title;
        if (txtTitle != null)
            txtTitle.setText(title);
    }

    public void setDesc(String desc) {
        this.desc = desc;
        if (txtDesc != null) {
            if (showDescAsHTML)
                txtDesc.setText(Html.fromHtml(desc));
            else
                txtDesc.setText(desc);
        }
    }

    TextView txtTitle;
    TextView txtDesc;
    View viewBg;

    public void onBindView(int position, CardListAdapter.CardItemHolder cardItemHolder) {
        super.onBindView(position, cardItemHolder);
        //StaticLogger.D("CardTitleDesc::onBindView " + position);
        viewBg = cardItemHolder.itemView;

        txtTitle = ((TextView)cardItemHolder.itemView.findViewById(R.id.card_title));
        txtTitle.setText(getTitle());
        txtTitle.setTypeface(mTypeface);
        txtTitle.setTextColor(this.textColor);
        txtDesc = ((TextView)cardItemHolder.itemView.findViewById(R.id.card_desc));
        if (showDescAsHTML)
            txtDesc.setText(Html.fromHtml(desc));
        else
            txtDesc.setText(desc);
        txtDesc.setTextColor(this.descTextColor);
        //D("onBindView " + position + " title " + getTitle() + " desc " + desc);
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        if (viewBg != null) {
            ((TextView)viewBg.findViewById(R.id.text1)).setTextColor(textColor);
        }
    }

    public void setDescTextColor(int color) {
        descTextColor = color;
        if (viewBg != null) {
            txtDesc.setTextColor(descTextColor);
        }
    }

    private Typeface mTypeface = Typeface.DEFAULT;
    public void setTypeFace(Typeface tf) {
        mTypeface = tf;
        if (viewBg != null) {
            ((TextView)viewBg.findViewById(R.id.text1)).setTypeface(mTypeface);
        }
    }
}
