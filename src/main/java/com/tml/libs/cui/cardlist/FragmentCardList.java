package com.tml.libs.cui.cardlist;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tml.libs.cui.R;


public class FragmentCardList extends Fragment {
    boolean showAsGrid = false;
    int nCols = 1;
    int spacing = 2;
    RecyclerView rvItems;

    RecyclerView.Adapter<CardListAdapter.CardItemHolder> rvItemsAdapter;

    public RecyclerView.Adapter<CardListAdapter.CardItemHolder> getRvItemsAdapter() {
        return rvItemsAdapter;
    }

    public void setRvItemsAdapter(RecyclerView.Adapter<CardListAdapter.CardItemHolder> rvItemsAdapter) {
        this.rvItemsAdapter = rvItemsAdapter;
        rvItems.setAdapter(rvItemsAdapter);
    }

    private FragmentCardListInteractionListener ilis = null;
    public FragmentCardList() {
    }

    public static FragmentCardList newInstance(RecyclerView.Adapter<CardListAdapter.CardItemHolder> adapter) {
        FragmentCardList fragment = new FragmentCardList();
        fragment.rvItemsAdapter = adapter;
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    public static FragmentCardList newInstance(RecyclerView.Adapter<CardListAdapter.CardItemHolder> adapter, FragmentCardListInteractionListener lis) {
        FragmentCardList fragment = new FragmentCardList();
        fragment.ilis = lis;
        fragment.rvItemsAdapter = adapter;
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    public static FragmentCardList newInstanceOfGrid(int nCols, int spacing, RecyclerView.Adapter<CardListAdapter.CardItemHolder> adapter) {
        FragmentCardList fragment = new FragmentCardList();
        fragment.rvItemsAdapter = adapter;
        fragment.nCols = nCols;
        fragment.spacing = spacing;
        fragment.showAsGrid = true;
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

    public void scrollTo(int x, int y) {
        rvItems.scrollTo(x, y);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_card_list, container, false);
        rvItems = (RecyclerView)root.findViewById(R.id.frag_card_list_rv);

        if (showAsGrid) {
            GridLayoutManager glm = new GridLayoutManager(getContext(), nCols);
            rvItems.addItemDecoration(
                    new GridSpacingItemDecoration(nCols, spacing, true));
//            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvItems.getContext(),
//                    glm.getOrientation());
//            rvItems.addItemDecoration(dividerItemDecoration);
            rvItems.setLayoutManager(glm);
        }
        else
            rvItems.setLayoutManager(new LinearLayoutManager(getContext()));
        rvItems.setAdapter(rvItemsAdapter);


        if (ilis != null)
            ilis.onViewCreated(rvItems);
        return root;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (ilis != null) ilis.onAttached();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public interface FragmentCardListInteractionListener {
        void onAttached();
        void onViewCreated(RecyclerView rv);
    }
}
