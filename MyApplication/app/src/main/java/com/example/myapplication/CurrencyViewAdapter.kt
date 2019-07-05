package com.example.myapplication

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlin.concurrent.fixedRateTimer

class CurrencyViewAdapter(private val myDataset: CurrencyData) :
        RecyclerView.Adapter<CurrencyViewAdapter.CurrencyViewHolder>(){

    private lateinit var recyclerView : RecyclerView

    init {
        setHasStableIds(true)
    }

    class CurrencyViewHolder(val currencyView: CurrencyView) : RecyclerView.ViewHolder(currencyView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        //val currencyView = CurrencyView.generateNewCurrenyViewObject(parent)
        //return CurrencyViewHolder(currencyView)
        val currencyView = CurrencyView(parent.context)
        var lp = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        currencyView.layoutParams = lp

        val holder = CurrencyViewHolder(currencyView)

        holder.currencyView.setOnClickListener{
            val pos = holder.adapterPosition
            myDataset.moveRateDataToTop(pos)
            notifyItemMoved(pos, 0)
            recyclerView.scrollToPosition(0)
        }

        return holder
    }

    override fun getItemCount(): Int {
        return myDataset.rates.size
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        /*
        holder.currencyView.setBigText("Big "+myDataset.rates[position].name)
        holder.currencyView.setSmallText("Small " + myDataset.rates[position].name)
        holder.currencyView.setCurrencyValue(myDataset.rates[position].value)
        */
        holder.currencyView.rateData = myDataset.rates[position]
    }

    override fun getItemId(position: Int): Long {
        val name = myDataset.rates[position].name
        var itemId : Long
        itemId = 0
        for (c in name){
            itemId = c.toInt() + itemId*256
        }
        return itemId
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        this.recyclerView = recyclerView
    }
}