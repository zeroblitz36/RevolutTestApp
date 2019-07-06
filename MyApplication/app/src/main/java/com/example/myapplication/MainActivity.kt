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
    private lateinit var viewAdapter: CurrencyViewAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewManager : RecyclerView.LayoutManager
    private lateinit var callApiButton : Button

    /*
    The "Revolut Android Test" document only mentions that this URL must be used
    It does not mention the existence of API calls such as
    "https://revolut.duckdns.org/latest?base=USD"
    */
    private val url = "https://revolut.duckdns.org/latest?base=EUR"
    private val currentData = CurrencyData()

    val queue: RequestQueue by lazy {
        Volley.newRequestQueue(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

        val jsonRequest = JsonObjectRequest(Request.Method.GET, url,
            Response.Listener{response ->
                viewAdapter.updateDataset(response)
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

