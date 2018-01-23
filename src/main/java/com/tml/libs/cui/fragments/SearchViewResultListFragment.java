package com.tml.libs.cui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tml.libs.cui.R;
import com.tml.libs.cui.cardlist.CardListAdapter;
import com.tml.libs.cui.cardlist.CardTitleDesc;
import com.tml.libs.cui.cardlist.FragmentCardList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SVRLFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchViewResultListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchViewResultListFragment extends Fragment {
    private SVRLFragmentInteractionListener mListener;
    CardListAdapter<CardTitleDesc> rvA;
    CardListAdapter.CardListModelProvider<CardTitleDesc> mp = null;
    public String SVRLTag;

    public SearchViewResultListFragment() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SearchViewResultListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchViewResultListFragment newInstance(String tag, CardListAdapter.CardListModelProvider<CardTitleDesc> mp) {
        SearchViewResultListFragment fragment = new SearchViewResultListFragment();
        fragment.mp = mp;
        fragment.SVRLTag = tag;
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
        View root = inflater.inflate(R.layout.fragment_search_view_result_list, container, false);
        SearchView searchView = (SearchView)root.findViewById(R.id.svrlSearchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mListener.SVRL_onRequestQuery(query);
                rvA.notifyDataSetChanged();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                mListener.SVRL_onRequestQuery(newText);
                rvA.notifyDataSetChanged();
                return false;
            }
        });
        searchView.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {

                                          }
                                      }
        );

        FragmentCardList frag = FragmentCardList.newInstance(createAdapter());
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.svrlContent, frag).commit();
        return root;
    }

    private RecyclerView.Adapter<CardListAdapter.CardItemHolder> createAdapter() {
        rvA = new CardListAdapter<CardTitleDesc>(R.layout.card_title_desc, mp,
                new CardListAdapter.CardClickListener() {
                    @Override
                    public void onClickCard(int index) {
                        mListener.SVRL_onSelectItemAt(SearchViewResultListFragment.this, index);
                    }

                    @Override
                    public boolean onLongClickCard(int index) {
                        return false;
                    }
                }
        );
        return rvA;
    }

    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SVRLFragmentInteractionListener) {
            mListener = (SVRLFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface SVRLFragmentInteractionListener {
        void SVRL_onSelectItemAt(SearchViewResultListFragment sender, int index);
        void SVRL_onRequestQuery(String query);
    }
}
