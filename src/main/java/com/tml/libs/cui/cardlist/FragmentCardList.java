package com.tml.libs.cui.cardlist;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tml.libs.cui.R;

public class FragmentCardList extends Fragment {

    RecyclerView rvItems;

    RecyclerView.Adapter<CardListAdapter.CardItemHolder> rvItemsAdapter;

    public RecyclerView.Adapter<CardListAdapter.CardItemHolder> getRvItemsAdapter() {
        return rvItemsAdapter;
    }

    public void setRvItemsAdapter(RecyclerView.Adapter<CardListAdapter.CardItemHolder> rvItemsAdapter) {
        this.rvItemsAdapter = rvItemsAdapter;
        rvItems.setAdapter(rvItemsAdapter);
    }

    public FragmentCardList() {
    }

    public static FragmentCardList newInstance(RecyclerView.Adapter<CardListAdapter.CardItemHolder> adapter) {
        FragmentCardList fragment = new FragmentCardList();
        fragment.rvItemsAdapter = adapter;
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_card_list, container, false);
        rvItems = (RecyclerView)root.findViewById(R.id.frag_card_list_rv);
        rvItems.setLayoutManager(new LinearLayoutManager(getContext()));
        rvItems.setAdapter(rvItemsAdapter);
        return root;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
