package com.tml.libs.cui.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.tml.libs.cui.JSONInputDialog;
import com.tml.libs.cui.R;
import com.tml.libs.cui.cardlist.CardListAdapter;
import com.tml.libs.cui.cardlist.CardListItemModel;
import com.tml.libs.cui.cardlist.CardTitleDesc;
import com.tml.libs.cui.cardlist.FragmentCardList;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Activity must use android:theme="@style/AppTheme.NoActionBar"
 */
public abstract class SimpleListBaseActivity extends AppCompatActivity {
    protected CardListAdapter<CardTitleDesc> rvA;
    protected String dlgAddNewItemTitle = "Add New Item";
    protected String dlgAddNewItemMessage = "";
    protected String dlgAddNewItemFieldName = "Name";
    protected int optionMenuID = -1;
    protected FloatingActionButton fab;
    protected SwipeRefreshLayout SRL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_list_base_activity);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar );

        SRL = (SwipeRefreshLayout)findViewById(R.id.slbcSRL);
        fab = (FloatingActionButton) findViewById(R.id.slba_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogAddNewItem();
            }
        });


        SRL.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (SRL.isRefreshing()) return;
                doReloadContent();
            }
        });

        FragmentCardList frag = FragmentCardList.newInstance(createAdapter());
        getSupportFragmentManager().beginTransaction().replace(R.id.rv_container, frag).commit();
    }

    protected void doReloadContent() {
        SRL.setRefreshing(true);
    }

    protected void onReloadContentFinished() {
        SRL.setRefreshing(false);
        rvA.notifyDataSetChanged();
    }

    private void showDialogAddNewItem() {
        JSONInputDialog.createOneTextInputDialog(this, dlgAddNewItemTitle, dlgAddNewItemMessage,
                dlgAddNewItemFieldName, new JSONInputDialog.JSONDialogListener() {
            @Override
            public boolean onConfirm(JSONObject jsonResult) {
                try {
                    onDlgAddNewItemResult(jsonResult.getString(dlgAddNewItemFieldName));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return true;
            }

            @Override
            public void onCancel() {

            }
        }).show();
    }

    protected abstract void onDlgAddNewItemResult(String newItemName);

    public abstract int getListSize();

    private RecyclerView.Adapter<CardListAdapter.CardItemHolder> createAdapter() {
        rvA = new CardListAdapter<CardTitleDesc>(R.layout.card_title_desc,
                new CardListAdapter.CardListModelProvider<CardTitleDesc>() {
            @Override
            public int size() {
                return getListSize();
            }

            @Override
            public CardTitleDesc getModel(int position) {
                return createListCardItemAt(position);
            }
        }, new CardListAdapter.CardClickListener() {
            @Override
            public void onClickCard(int index) {
                onClickListItem(index);
            }

            @Override
            public boolean onLongClickCard(int index) {
                return handleOnLongClickListItem(index);
            }
        });

        setupCardVisualStyle();
        return rvA;
    }

    protected void setupCardVisualStyle() {
        //TODO: fix bug background card not wrap content after notifychange
        //rvA.setVisualStyle(CardListItemModel.VSNAME_NORMAL, getResources().getDrawable(R.drawable.card_round_box_normal));
    }

    protected abstract void onClickListItem(int index);

    protected abstract boolean handleOnLongClickListItem(int index);

    protected abstract CardTitleDesc createListCardItemAt(int position);

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (optionMenuID > 0)
            getMenuInflater().inflate(optionMenuID, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (rvA != null)
            rvA.notifyDataSetChanged();
    }
}
