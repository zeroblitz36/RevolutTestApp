package com.example.myapplication

import org.json.JSONObject

class CurrencyData{
    var base : String = "NAN"
    var date : String = ""

    class RateData(var name: String = "NAN", var value: Float = -1.0f) {}

    var rates = ArrayList<RateData>()
    var rateMap = HashMap<String, RateData>()
    var masterRateData : RateData? = null
    var baseRateData : RateData? = null

    fun setRates(a: List<RateData>){
        rates = ArrayList(a)
        rateMap.clear()
        for(r in rates){
            rateMap[r.name] = r
        }
    }

    fun moveRateDataToTop(index: Int){
        val rd = rates[index]
        rates.removeAt(index)
        rates.add(0, rd)
    }

    fun update(j : JSONObject){
        base = j["base"].toString()
        date = j["date"].toString()

        var jsonObject = j.getJSONObject("rates")
        var list = ArrayList<CurrencyData.RateData>()
        for(i in jsonObject.keys()){
            list.add(CurrencyData.RateData(i,jsonObject.getDouble(i).toFloat()))
        }
        setRates(list)

        if(baseRateData == null){
            var i = 0
            for (rateData in rates){
                if(rateData.name == base){
                    baseRateData = rateData
                    masterRateData = baseRateData
                    break
                }
                ++i
            }

            if(i < rates.size){
                moveRateDataToTop(i)
            }
        }
    }
}