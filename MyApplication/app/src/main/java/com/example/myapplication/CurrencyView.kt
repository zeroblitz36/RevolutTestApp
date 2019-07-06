package com.example.myapplication

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

class CurrencyView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle) {

    private var imageView1 : ImageView
    private var textView1 : TextView
    private var textView2 : TextView
    public var editTextView : EditText
    private var dataSet : CurrencyData? = null
    var rateData = CurrencyData.RateData()
        get() = field
        set(value){
            field = value
            textView1.text = field.name
            textView2.text = field.name + " country"
            var x = field.rateValue
            val localDataSet = dataSet
            if(localDataSet != null){
                x = localDataSet.masterCoinQuantity * rateData.rateValue / localDataSet.masterRateData.rateValue
            }else{
                x = -999.0f
            }
            editTextView.setText(x.toString())
        }

    constructor(context: Context, dataSet: CurrencyData) : this(context, null){
        this.dataSet = dataSet
    }
 //   constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    //constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
//        imageView1 = this.findViewById(R.id.currency_image_view) as ImageView
//        textView1 = this.findViewById(R.id.currency_short_name_text_view) as TextView
//        textView2 = this.findViewById(R.id.curreny_long_name_text_view) as TextView
//        editTextView = this.findViewById(R.id.currency_value_edit_text) as EditText
    //}
    init {
        //LayoutInflater.from(context).inflate(R.layout.currency_display_view, this, true)
        View.inflate(getContext(), R.layout.currency_display_view, this)
        //orientation = VERTICAL
        imageView1 = this.findViewById(R.id.currency_image_view) as ImageView
        textView1 = this.findViewById(R.id.currency_short_name_text_view) as TextView
        textView2 = this.findViewById(R.id.curreny_long_name_text_view) as TextView
        editTextView = this.findViewById(R.id.currency_value_edit_text) as EditText
        editTextView.addTextChangedListener( object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            var lastText : String = ""
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                var currentText : String = ""
                if (p0 != null){
                    currentText = p0.toString()
                }

                val localDataSet = dataSet
                if(localDataSet == null){
                    return
                }
                if (rateData != localDataSet.masterRateData) {
                    return
                }
                Log.d("Test", "lastText = " + lastText)
                Log.d("Test", "currentText = " + currentText)
                if(lastText.compareTo(currentText) == 0){
                    Log.d("Test", "String are identical, don't do anything")
                    return
                }
                lastText = currentText
                val floatValue = p0.toString().toFloat()
                localDataSet.masterCoinQuantity = floatValue
                localDataSet.notifyAllDataChangedExceptMaster()
                Log.d("Test", "floatValue = " +floatValue)
                //localDataSet.baseRateData.rateValue
            }
        })
    }
    /*
    override fun onFinishInflate() {
        super.onFinishInflate()
        imageView1 = this.findViewById(R.id.currency_image_view) as ImageView
        textView1 = this.findViewById(R.id.currency_short_name_text_view) as TextView
        textView2 = this.findViewById(R.id.curreny_long_name_text_view) as TextView
        editTextView = this.findViewById(R.id.currency_value_edit_text) as EditText
    }
    */
    /*
    fun setSmallText(text: String){
        textView1.text = text
    }

    fun setBigText(text: String){
        textView2.text = text
    }

    fun setCurrencyValue(rateValue: Float){
        editTextView.setText(rateValue.toString())
    }
    */
}