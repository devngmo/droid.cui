package com.tml.libs.cui.cardlist;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tml.libs.cui.R;

public class FragmentSearchCardList extends Fragment {
    SearchView sv;
    RecyclerView rvItems;

    SearchView.OnQueryTextListener onQueryTextListener = null;

    public void setOnQueryTextListener(SearchView.OnQueryTextListener onQueryTextListener) {
        this.onQueryTextListener = onQueryTextListener;
        if (sv != null)
            sv.setOnQueryTextListener(onQueryTextListener);
    }

    RecyclerView.Adapter<CardListAdapter.CardItemHolder> rvItemsAdapter;

    public RecyclerView.Adapter<CardListAdapter.CardItemHolder> getRvItemsAdapter() {
        return rvItemsAdapter;
    }

    public void setRvItemsAdapter(RecyclerView.Adapter<CardListAdapter.CardItemHolder> rvItemsAdapter) {
        this.rvItemsAdapter = rvItemsAdapter;
        rvItems.setAdapter(rvItemsAdapter);
    }

    public FragmentSearchCardList() {
    }

    public static FragmentSearchCardList newInstance(RecyclerView.Adapter<CardListAdapter.CardItemHolder> adapter) {
        FragmentSearchCardList fragment = new FragmentSearchCardList();
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
        View root = inflater.inflate(R.layout.fragment_search_card_list, container, false);
        sv = (SearchView) root.findViewById(R.id.frag_card_list_sv);
        rvItems = (RecyclerView)root.findViewById(R.id.frag_card_list_rv);
        rvItems.setLayoutManager(new LinearLayoutManager(getContext()));
        rvItems.setAdapter(rvItemsAdapter);

        if (onQueryTextListener != null)
            sv.setOnQueryTextListener(onQueryTextListener);
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
