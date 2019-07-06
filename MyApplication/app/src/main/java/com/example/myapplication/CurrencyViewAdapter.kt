package com.example.myapplication

import android.util.Log
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONObject

class CurrencyViewAdapter(private val myDataset: CurrencyData) :
        RecyclerView.Adapter<CurrencyViewAdapter.CurrencyViewHolder>(){

    private lateinit var recyclerView : RecyclerView

    init {
        setHasStableIds(true)
        myDataset.currencyViewAdapter = this
    }

    class CurrencyViewHolder(val currencyView: CurrencyView) : RecyclerView.ViewHolder(currencyView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        Log.d("Test", "onCreateViewHolder")
        val currencyView = CurrencyView(parent.context, myDataset)
        var lp = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        currencyView.layoutParams = lp

        val holder = CurrencyViewHolder(currencyView)

        holder.currencyView.editTextView.setOnFocusChangeListener {
            view, hasFocus ->
            if(hasFocus){
                val pos = holder.adapterPosition
                myDataset.moveRateDataToTop(pos)
                recyclerView.stopScroll()
                notifyItemMoved(pos, 0)
                recyclerView.scrollToPosition(0)

                val editText = view as EditText
                var floatValue = 0.0f
                if (editText != null && editText.editableText.isNotEmpty()){
                    floatValue = editText.editableText.toString().toFloat()
                }

                myDataset.masterCoinQuantity = floatValue
            }
        }

        return holder
    }

    override fun getItemCount(): Int {
        return myDataset.rates.size
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        holder.currencyView.rateData.currentlyAssignedViewHolder = null
        holder.currencyView.rateData = myDataset.rates[position]
        myDataset.rates[position].currentlyAssignedViewHolder = holder
    }

    override fun getItemId(position: Int): Long {
        val itemId = myDataset.rates[position].name.hashCode().toLong()
        Log.d("Test", "pos = "+position+" name = " + myDataset.rates[position].name + " id = " + itemId)
        return itemId
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        this.recyclerView = recyclerView
    }

    fun updateDataset(j : JSONObject){
        if (myDataset.update(j)) {
            //notifyItemRangeChanged(1, myDataset.rates.size-1)
        }else{
            notifyItemRangeInserted(0, myDataset.rates.size)
        }
    }
}