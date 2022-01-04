package com.tml.libs.cui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.tml.libs.cui.R;
import com.tml.libs.cui.cardlist.CardListAdapter;
import com.tml.libs.cui.cardlist.CardListItemModel;
import com.tml.libs.cui.cardlist.FragmentCardList;


public class CardListBasedFragment<T extends CardListItemModel> extends Fragment {
    protected CardListAdapter<T> rvA;
    protected boolean playSoundWhenUserClickCard = true;
    boolean highlightSelectedCard = false;

    public CardListBasedFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    public int getLayoutID() {
        return R.layout.fragment_card_list_based;
    }
    public int getContentContainerID() { return R.id.fclb_fragcon; }
    public int getCardLayoutID() { return R.layout.card_title_desc; }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(getLayoutID(), container, false);
        FragmentCardList f = FragmentCardList.newInstance(createListAdapter());
        getActivity().getSupportFragmentManager().beginTransaction().replace(getContentContainerID(), f).commit();
        return root;
    }

    protected RecyclerView.Adapter<CardListAdapter.CardItemHolder> createListAdapter() {
        rvA = new CardListAdapter<T>(getCardLayoutID(),
                new CardListAdapter.CardListModelProvider<T>() {
                    @Override
                    public int size() {
                        return getListSize();
                    }

                    @Override
                    public T getModel(int position) {
                        return createModelCardAt(position);
                    }
                },
                new CardListAdapter.CardClickListener() {
                    @Override
                    public void onClickCard(int index) {
                        onClickCardImpl(index);
                        if (highlightSelectedCard)
                            rvA.notifyDataSetChanged();
                    }

                    @Override
                    public boolean onLongClickCard(int index) {
                        return onLongClickCardImpl(index);
                    }
                }
        );

        setupCardStyle();
        return rvA;
    }

    protected void setupCardStyle() {


    }

    protected boolean onLongClickCardImpl(int index) {
        return false;
    }

    protected void onClickCardImpl(int index) {
        //if (playSoundWhenUserClickCard)
        //    AppCore.Ins().playSoundEffect();
    }

    protected T createModelCardAt(int position) {
        return null;
    }

    protected int getListSize() {
        return 0;
    }

    public void updateUI() {
        if (isVisible())
            rvA.notifyDataSetChanged();
    }
}
