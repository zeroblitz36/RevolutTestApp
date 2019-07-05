package com.example.myapplication

import android.util.Log
import org.json.JSONObject

class CurrencyData{
    var base : String = "NAN"
    var date : String = ""
    var receivedFirstUpdate : Boolean = false

    class RateData(var name: String = "NAN", var rateValue: Float = -1.0f) {}

    var rates = ArrayList<RateData>()
    var rateMap = HashMap<String, RateData>()
    var masterRateData = RateData()
    var baseRateData = RateData()
    var masterCoinQuantity : Float = 100.0f

    lateinit var currencyViewAdapter : CurrencyViewAdapter

    fun updateRates(a: List<RateData>){
        if(rates.isEmpty()){
            rates = ArrayList(a)
            rateMap.clear()
            for(r in rates){
                rateMap[r.name] = r
            }
            return
        }

        for (r in a){
            var rateData = rateMap[r.name]
            if(rateData != null){
                rateData.rateValue = r.rateValue
            }
        }
    }

    fun moveRateDataToTop(index: Int){
        val rd = rates[index]
        rates.removeAt(index)
        rates.add(0, rd)
    }

    fun update(j : JSONObject) : Boolean{
        base = j["base"].toString()
        date = j["date"].toString()

        var jsonObject = j.getJSONObject("rates")
        var list = ArrayList<CurrencyData.RateData>()
        for(i in jsonObject.keys()){
            list.add(CurrencyData.RateData(i,jsonObject.getDouble(i).toFloat()))
        }
        val flag = receivedFirstUpdate
        if(!receivedFirstUpdate){
            baseRateData = RateData(base, 1.0f)
            masterRateData = baseRateData
            list.add(0, masterRateData)

            receivedFirstUpdate = true
        }
        updateRates(list)

        return flag
    }

    fun notifyAllDataChangedExceptMaster(){
        try{
            currencyViewAdapter.notifyItemRangeChanged(1, rates.size-1)
        }catch(e : IllegalStateException){
            Log.e("CurrencyData", "Cannot update the adapter now")
        }
    }
}