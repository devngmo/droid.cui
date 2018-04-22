package com.tml.libs.cui.cardlist;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tml.libs.cutils.StaticLogger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by TML on 04/01/2017.
 */
public class CardListAdapter<T extends CardListItemModel> extends RecyclerView.Adapter<CardListAdapter.CardItemHolder> {

    public boolean showSelection = false;
    Map<String, CardChildViewClickListener> cardChildViewListenerMap = new HashMap<>();
    public CardChildViewClickListener getChildClickListener(String key) {
        return cardChildViewListenerMap.get(key);
    }

    @Deprecated
    public void selectCardByID(String id) {
    }

    boolean enableCardCustomBackground = false;
    public boolean isEnableCardCustomBackground() {
        return enableCardCustomBackground;
    }

    public void setEnableCardCustomBackground(boolean enabled) {
        this.enableCardCustomBackground = enabled;
    }

    public interface CardChildViewClickListener {
        void onClick(int cardPosition, CardListAdapter.CardItemHolder card, int viewPosition, View v);
    }

    Map<String, Drawable> visualStyles = new HashMap<>();
    private static final String TAG = "CardListAdapter";

    int selectedIndex = -1;

    public T getCardAt(int index) {
        return provider.getModel(index);
    }

    public void selectItemAt(int position) {
        selectedIndex = position;
        notifyDataSetChanged();
        if (mCardClickListener != null)
            mCardClickListener.onClickCard(position);
    }

    public void selectItemAt(int position, CardItemHolder<T> holder) {
        selectedIndex = position;
        notifyDataSetChanged();
        if (mCardClickListener != null)
            mCardClickListener.onClickCard(position);
    }

    public interface CardListModelProvider<T> {
        int size();
        T getModel(int position);
    }

    public interface  CardClickListener {
        void onClickCard(int index);
        boolean onLongClickCard(int index);
    }

    CardClickListener mCardClickListener = null;

    int mLayoutID;
    CardListModelProvider<T> provider;

    public void setProvider(CardListModelProvider<T> provider) {
        this.provider = provider;
        notifyDataSetChanged();
    }

    public CardListAdapter(int layoutID, CardListModelProvider<T> listProvider, CardClickListener cardListener) {
        super();
        mLayoutID = layoutID;
        provider = listProvider;
        mCardClickListener = cardListener;
    }

    @Override
    public CardItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(mLayoutID, parent, false);

        parent.requestDisallowInterceptTouchEvent(true);
        return new CardItemHolder(view);
    }

    @Override
    public void onBindViewHolder(final CardListAdapter.CardItemHolder holder, final int position) {
        //StaticLogger.D(TAG, "onBindViewHolder: pos " + position);
        T model = provider.getModel(position);

        if (model == null)
        {
            StaticLogger.E(TAG, "onBindViewHolder: pos " + position + " card model is null" );
            return;
        }

        holder.onBindModel(this, position, model);

        if (showSelection) {
            if (selectedIndex == position)
                model.setBgDrawable(getVisualStyleDrawable(CardListItemModel.VSNAME_SELECTED));
            else {
                if (model.hasCustomBg()) {
                    //StaticLogger.D("showCustomBg " + position);
                    model.showCustomBg();
                }
                else
                    model.setBgDrawable(getVisualStyleDrawable(CardListItemModel.VSNAME_NORMAL));
            }
        }
        else {
            if (model.hasCustomBg()) {
                //StaticLogger.D("showCustomBg " + position);
                model.showCustomBg();
            }
            else {
                //StaticLogger.D("show normal bg " + position);
                model.setBgDrawable(getVisualStyleDrawable(CardListItemModel.VSNAME_NORMAL));
            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectItemAt(position, holder);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return mCardClickListener.onLongClickCard(position);
            }
        });

    }

    public Drawable getVisualStyleDrawable(String key) {
        if (visualStyles.containsKey(key))
            return visualStyles.get(key);
        return null;
    }

    public void setVisualStyle(String key, Drawable d) {
        visualStyles.put(key, d);
    }


    @Override
    public int getItemCount() {
        return provider.size();
    }

    public class CardItemHolder<T extends CardListItemModel> extends RecyclerView.ViewHolder {
        T cardModel;
        CardListAdapter<T> adapter;

        public T getCardModel() {
            return cardModel;
        }

        public CardItemHolder(View itemView) {
            super(itemView);
        }
        public void onBindModel(CardListAdapter<T> adapter, int position, T cardModel) {
            this.adapter = adapter;
            this.cardModel = cardModel;
            cardModel.onBindView(position, this);
        }

        public CardListAdapter<T> getAdapter() {
            return adapter;
        }
    }
}
