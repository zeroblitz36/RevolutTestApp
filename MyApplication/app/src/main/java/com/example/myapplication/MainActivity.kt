package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class MainActivity : AppCompatActivity() {
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewManager : RecyclerView.LayoutManager
    private lateinit var callApiButton : Button
    private val url = "https://revolut.duckdns.org/latest?base=EUR"
    private val currentData = CurrencyData()

    val queue: RequestQueue by lazy {
        Volley.newRequestQueue(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var myDataset = Array<String>(1000) {i -> "Text " + i}

        viewManager = LinearLayoutManager(this)
        //viewAdapter = MyAdapter(myDataset)
        viewAdapter = CurrencyViewAdapter(currentData)

        recyclerView = findViewById<RecyclerView>(R.id.my_recycler_view).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        callApiButton = findViewById<Button>(R.id.call_api_button)
        callApiButton.setOnClickListener{
            callApiStuffAndPrint()
        }
    }

    fun callApiStuffAndPrint(){
        Log.d("ApiCall", "Start api call")
        /*
        val stringRequest = StringRequest(Request.Method.GET, url,
            Response.Listener<String>{response ->
                Log.d("ApiCall", "received = '" + response + "'")
            },
            Response.ErrorListener {
                Log.d("ApiCall", "something didn't work!")
            })

        queue.add(stringRequest)
        */

        val jsonRequest = JsonObjectRequest(Request.Method.GET, url,
            Response.Listener{response ->
                //Log.d("ApiCall", "received = '" + response + "'")
                //Log.d("ApiCall", "base = '" + response["base"] + "'")
                //Log.d("ApiCall", "date = '" + response["date"] + "'")
                //Log.d("ApiCall", "rates = '" + response["rates"] + "'")

                currentData.base = response["base"].toString()
                currentData.date = response["date"].toString()
                //currentData.rates = response["rates"].
                //var map = HashMap<String,Float>()
                //val jsonRates = response["rates"]
                var jsonObject = response.getJSONObject("rates")
                /*
                var map = HashMap<String,Float>()
                jsonObject.keys()
                for(i in jsonObject.keys()){
                    val j = jsonObject.getDouble(i)
                    //Log.d("ApiCall", "jsonArray["+i+"] = "+j)
                    map[i] = j.toFloat()
                }
                currentData.setRates(map)
                */
                var list = ArrayList<CurrencyData.RateData>()
                for(i in jsonObject.keys()){
                    list.add(CurrencyData.RateData(i,jsonObject.getDouble(i).toFloat()))
                }
                currentData.setRates(list)
                viewAdapter.notifyDataSetChanged()
            },
            Response.ErrorListener {
                Log.d("ApiCall", "something didn't work!")
            })

        queue.add(jsonRequest)
    }

    override fun onStop() {
        super.onStop()
        queue.cancelAll(Any())
    }
}

