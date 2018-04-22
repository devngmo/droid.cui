package com.tml.libs.cui.cardlist;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tml.libs.cui.R;
import com.tml.libs.cutils.StaticLogger;

/**
 * Created by TML on 04/01/2017.
 */

public class CardImageTitle extends CardListItemModel {
    private static final String TAG = "CardImageTitle";
    Bitmap bmp;
    String title;
    int textColor = 0xff000000;


    public String getTitle() {
        return title;
    }

    public Bitmap getBmp() {
        return bmp;
    }

    public void setBmp(Bitmap bmp) {
        this.bmp = bmp;
        if (img != null)
            img.setImageBitmap(bmp);
    }

    public CardImageTitle(String title, Bitmap b) {
        this.title = title;
        setBmp(b);
        //StaticLogger.enableLog(this);
    }

    public void setTitle(String title) {
        this.title = title;
        if (txtTitle != null)
            txtTitle.setText(title);
    }

    TextView txtTitle;
    ImageView img;
    View viewBg;

    public void onBindView(int position, CardListAdapter.CardItemHolder cardItemHolder) {
        viewBg = cardItemHolder.itemView;

        txtTitle = ((TextView)cardItemHolder.itemView.findViewById(R.id.ci_title));
        txtTitle.setText(getTitle());
        txtTitle.setTextColor(this.textColor);
        img = (ImageView)viewBg.findViewById(R.id.ci_img);
        if (bmp != null)
            img.setImageBitmap(bmp);
        //D("onBindView " + position + " title " + getTitle() + " desc " + desc);

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
            txtTitle.setTextColor(textColor);
        }
    }
}
