package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
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
    private lateinit var mHandler : Handler
    /*
    The "Revolut Android Test" document only mentions that this URL must be used
    It does not mention the existence of API calls such as
    "https://revolut.duckdns.org/latest?base=USD"
    All calculations are done relatively to the Euro currency
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
        viewAdapter = CurrencyViewAdapter(currentData)

        recyclerView = findViewById<RecyclerView>(R.id.my_recycler_view).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
        recyclerView.setScrollingTouchSlop(RecyclerView.TOUCH_SLOP_PAGING)
        mHandler = Handler()
    }

    private fun callApiStuffAndPrint(){
        Log.d("ApiCall", "Start api call")

        val jsonRequest = JsonObjectRequest(Request.Method.GET, url,
            Response.Listener{response ->
                viewAdapter.updateDataset(response)

                mHandler.postDelayed({
                    callApiStuffAndPrint()
                },1000)
            },
            Response.ErrorListener {
                Log.d("ApiCall", "something didn't work!")
            })
        jsonRequest.tag = this
        queue.add(jsonRequest)
    }

    override fun onResume() {
        super.onResume()
        queue.stop()
        queue.cancelAll(this)
        queue.start()
        callApiStuffAndPrint()
    }

    override fun onPause() {
        super.onPause()
        queue.stop()
        queue.cancelAll(this)
    }

    override fun onStop() {
        super.onStop()
        queue.stop()
        queue.cancelAll(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        queue.stop()
        queue.cancelAll(this)
    }
}

