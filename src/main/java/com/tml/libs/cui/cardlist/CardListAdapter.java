package com.tml.libs.cui.cardlist;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tml.libs.cutils.StaticLogger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by TML on 04/01/2017.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class CardListAdapter<T extends CardListItemModel> extends RecyclerView.Adapter<CardListAdapter.CardItemHolder> {
    public boolean showSelection = false;
    public static final int MODE_MULTIPLE = 2;
    public static final int MODE_SINGLE = 1;
    public int selectionMode = MODE_SINGLE;
    Map<String, CardChildViewClickListener> cardChildViewListenerMap = new HashMap<>();
    public CardChildViewClickListener getChildClickListener(String key) {
        return cardChildViewListenerMap.get(key);
    }

    @Deprecated
    public void selectCardByID(String id) {
    }

    private boolean enableCardCustomBackground = false;
    public boolean isEnableCardCustomBackground() {
        return enableCardCustomBackground;
    }

    public void setEnableCardCustomBackground(boolean enabled) {
        this.enableCardCustomBackground = enabled;
    }

    public interface CardChildViewClickListener {
        void onClick(int cardPosition, CardListAdapter.CardItemHolder card, int viewPosition, View v);
    }

    private Map<String, Drawable> visualStyles = new HashMap<>();
    private static final String TAG = "CardListAdapter";

    private int selectedIndex = -1;
    private List<Integer> indices = new ArrayList<>();

    public T getCardAt(int index) {
        return provider.getModel(index);
    }

    public void selectItemAt(int position) {
        selectedIndex = position;
        notifyDataSetChanged();
        if (mCardClickListener != null)
            mCardClickListener.onClickCard(position);
    }

    private void selectItemAt(int position, CardItemHolder<T> holder) {
        selectedIndex = position;
        if (selectionMode == MODE_MULTIPLE) {
            if (!indices.contains(position))
                indices.add(position);
            else {
                indices.remove(position);
            }
        }
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

    private CardClickListener mCardClickListener;

    private int mLayoutID;
    private CardListModelProvider<T> provider;

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
    public CardItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(mLayoutID, parent, false);

        parent.requestDisallowInterceptTouchEvent(true);
        return new CardItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CardListAdapter.CardItemHolder holder, int position) {
        //StaticLogger.D(TAG, "onBindViewHolder: pos " + position);
        T model = provider.getModel(position);

        if (model == null)
        {
            StaticLogger.E(TAG, "onBindViewHolder: pos " + position + " card model is null" );
            return;
        }

        holder.onBindModel(this, position, model);

        if (showSelection) {
            if (selectionMode == MODE_MULTIPLE) {
                if (indices.contains(position))
                    model.setBgDrawable(getVisualStyleDrawable(CardListItemModel.VSNAME_SELECTED));
                else {
                    if (model.hasCustomBg()) {
                        model.showCustomBg();
                    } else
                        model.setBgDrawable(getVisualStyleDrawable(CardListItemModel.VSNAME_NORMAL));
                }
            } else {
                if (selectedIndex == position)
                    model.setBgDrawable(getVisualStyleDrawable(CardListItemModel.VSNAME_SELECTED));
                else {
                    if (model.hasCustomBg()) {
                        model.showCustomBg();
                    } else
                        model.setBgDrawable(getVisualStyleDrawable(CardListItemModel.VSNAME_NORMAL));
                }
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
                selectItemAt(holder.getAdapterPosition(), holder);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return mCardClickListener.onLongClickCard(holder.getAdapterPosition());
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

    public class CardItemHolder<TCardModel extends CardListItemModel> extends RecyclerView.ViewHolder {
        TCardModel cardModel;
        CardListAdapter<TCardModel> adapter;

        public TCardModel getCardModel() {
            return cardModel;
        }

        public CardItemHolder(View itemView) {
            super(itemView);
        }

        public void onBindModel(CardListAdapter<TCardModel> adapter, int position, TCardModel cardModel) {
            this.adapter = adapter;
            this.cardModel = cardModel;
            cardModel.onBindView(position, this);
        }

        public CardListAdapter<TCardModel> getAdapter() {
            return adapter;
        }
    }
}
