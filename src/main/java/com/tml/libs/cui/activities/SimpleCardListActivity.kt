package com.tml.libs.cui.activities

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import com.tml.libs.cui.R
import com.tml.libs.cui.cardlist.CardListAdapter
import com.tml.libs.cui.cardlist.CardListItemModel
import com.tml.libs.cui.cardlist.CardTitleDesc
import com.tml.libs.cui.cardlist.FragmentCardList
import kotlinx.android.synthetic.main.simple_card_list_activity.*

open class SimpleCardListActivity : AppCompatActivity() {
    lateinit var rvA: CardListAdapter<CardTitleDesc>
    internal var highlightSelectedCard = false

    open fun getLayoutID(): Int {
        return R.layout.simple_card_list_activity
    }

    open fun getContentContainerID(): Int {
        return R.id.sl_frame_marker
    }

    open fun getCardLayoutID(): Int {
        return R.layout.card_title_desc
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutID())
        setSupportActionBar(scla_toolbar)

        scla_fab.setOnClickListener { view ->
            onClickFloatButtonAdd()
        }

        initRecyclerViewFragment()
    }

    open fun onClickFloatButtonAdd() {

    }


    private fun initRecyclerViewFragment() {
        val f = FragmentCardList.newInstance(createListAdapter())
        getSupportFragmentManager().beginTransaction().replace(getContentContainerID(), f).commit()
    }

    protected fun createListAdapter(): RecyclerView.Adapter<CardListAdapter<CardListItemModel>.CardItemHolder<CardListItemModel>>? {
        rvA = CardListAdapter<CardTitleDesc>(getCardLayoutID(),
                object : CardListAdapter.CardListModelProvider<CardTitleDesc> {
                    override fun size(): Int {
                        return getListSize()
                    }

                    override fun getModel(position: Int): CardTitleDesc? {
                        return createModelCardAt(position)
                    }
                },
                object : CardListAdapter.CardClickListener {
                    override fun onClickCard(index: Int) {
                        onClickCardImpl(index)
                        if (highlightSelectedCard)
                            rvA.notifyDataSetChanged()
                    }

                    override fun onLongClickCard(index: Int): Boolean {
                        return onLongClickCardImpl(index)
                    }
                }
        )

        setupCardStyle()
        return rvA
    }

    open fun setupCardStyle() {


    }

    open fun onLongClickCardImpl(index: Int): Boolean {
        return false
    }

    open fun onClickCardImpl(index: Int) {
        //if (playSoundWhenUserClickCard)
        //    AppCore.Ins().playSoundEffect();
    }

    open fun createModelCardAt(position: Int): CardTitleDesc {
        return CardTitleDesc("item " + position, "")
    }

    open fun getListSize(): Int {
        return 0
    }

    open fun updateUI() {
        rvA.notifyDataSetChanged()
    }
}
