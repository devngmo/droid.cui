package com.tml.libs.cui.cardlist;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import com.tml.libs.cui.R;
import com.tml.libs.cutils.StaticLogger;

/**
 * Created by TML on 04/01/2017.
 */

public class CardItemStats extends CardListItemModel {
    private static final String TAG = "CardItemStats";
    String title, props, values;
    public String getTitle() {
        return title;
    }

    public String getProps() {
        return props;
    }

    public String getValues() {
        return values;
    }



    public CardItemStats(String title, String props, String values) {
        this.title = title;
        this.props = props;
        this.values = values;
        StaticLogger.enableLog(this);
    }

    public void setTitle(String title) {
        this.title = title;
        if (txtTitle != null)
            txtTitle.setText(title);
    }

    public void setProps(String props) {
        this.props = props;
        if (txtProps != null)
            txtProps.setText(props);
    }

    public void setValues(String values) {
        this.values = values;
        if (txtValues != null)
            txtValues.setText(values);
    }

    TextView txtTitle;
    TextView txtProps;
    TextView txtValues;


    public void onBindView(int position, CardListAdapter.CardItemHolder cardItemHolder) {
        txtTitle = ((TextView)cardItemHolder.itemView.findViewById(R.id.cis_name));
        txtTitle.setText(getTitle());
        txtProps = ((TextView)cardItemHolder.itemView.findViewById(R.id.cis_props));
        txtProps.setText(getProps());
        txtValues = ((TextView)cardItemHolder.itemView.findViewById(R.id.cis_values));
        txtValues.setText(getValues());

        if (customBg != null)
            cardItemHolder.itemView.setBackground(customBg);
    }

}
