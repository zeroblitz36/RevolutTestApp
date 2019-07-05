package com.example.myapplication

class CurrencyData{
    var base = "EUR"
    var date = ""
    //var rates = HashMap<String,Float>()

    class RateData(var name: String = "NAN", var value: Float = -1.0f) {}

    var rates = ArrayList<RateData>()
    var rateMap = HashMap<String, Float>()

    /*
    fun setRates(d: HashMap<String,Float>) {
        rates.clear()
        for ((k,v) in d){
            rates.add(RateData(k,v))
        }
    }
    */
    fun setRates(a: List<RateData>){
        rates = ArrayList(a)
        rateMap.clear()
        for(r in rates){
            rateMap[r.name] = r.value
        }
    }

    fun moveRateDataToTop(index: Int){
        val rd = rates[index]
        rates.removeAt(index)
        rates.add(0, rd)
    }
}