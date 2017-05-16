package com.tml.libs.cui.cardlist;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    public void selectCardByID(String id) {
        selectedCardID = id;
        if (selectedModel != null)
            selectedModel.setBgDrawable(getVisualStyleDrawable( selectedModel.bgNormalVSName ));
        lastSelectedCardPosition = -1;
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

    int lastSelectedCardPosition = -1;
    String selectedCardID = null;
    T selectedModel = null;

    public T getCardAt(int index) {
        return provider.getModel(index);
    }

    public void selectItemAt(int position) {
        lastSelectedCardPosition = position;

        if (selectedModel != null)
            selectedModel.setBgDrawable(getVisualStyleDrawable( selectedModel.bgNormalVSName ));

        selectedModel = null;
        notifyDataSetChanged();

        if (mCardClickListener != null)
            mCardClickListener.onClickCard(position);
    }
    public void selectItemAt(int position, CardItemHolder<T> holder) {
        T clickModel = holder.cardModel;
        if (clickModel.equals( selectedModel) == false) {
            if (selectedModel != null) {
                selectedModel.setBgDrawable(getVisualStyleDrawable(selectedModel.bgNormalVSName));
                Log.d(TAG, "onBindViewHolder: Deselect card " + lastSelectedCardPosition );
            }
        }


        lastSelectedCardPosition = position;
        selectedModel = clickModel;
        selectedCardID = selectedModel.getID();

        Drawable bg = getVisualStyleDrawable(selectedModel.bgSelectedVSName);
        selectedModel.setBgDrawable(bg);
        Log.d(TAG, "onBindViewHolder: SELECTED card " + position );


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

        Log.d(TAG, "onBindViewHolder: pos " + position);
        if (provider.getModel(position) == null)
        {
            Log.e(TAG, "onBindViewHolder: pos " + position + " card model is null" );
            return;
        }

        T model = provider.getModel(position);

        if (lastSelectedCardPosition == -1 && selectedCardID != null) {
            if (model.getID().equals(selectedCardID))
            {
                selectedModel = model;
                lastSelectedCardPosition = position;
            }
        }

        if (lastSelectedCardPosition == position) {
            if (selectedModel == null) {
                selectedModel = model;
                selectedCardID = model.getID();
            }
        }


        holder.onBindModel(this, position, model);


        if (model.equals(selectedModel))// != null && selectedModel.getID() != null &&   selectedModel.getID().equals(model.getID()))
            Log.d(TAG, "onBindViewHolder: SELECTED card " + position );
        else
            Log.d(TAG, "onBindViewHolder: card " + position );

//        if (selectedModel != null)
//            Log.d(TAG, "LAST SELECTION: pos " + lastSelectedCardPosition + " model " + selectedModel.getID());
//        Log.d(TAG, "BIND: pos " + position + " model " + model.getID());

        if (isEnableCardCustomBackground() == false) {
            Drawable bg = getVisualStyleDrawable(model.bgNormalVSName);
            if (model.equals(selectedModel))
                bg = getVisualStyleDrawable(model.bgSelectedVSName);

            if (bg != null)
                model.setBgDrawable(bg);
        }
        else {
            if (model.hasCustomBg() == false)
                model.showCustomBg();
                //model.setBgDrawable(getVisualStyleDrawable(model.bgNormalVSName));
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
