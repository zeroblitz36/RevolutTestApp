package com.example.myapplication

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import java.util.*
import kotlin.collections.HashMap

class CurrencyView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle) {

    private var imageView1 : ImageView
    private var currencyShortNameTextView : TextView
    private var currentLongNameTextView : TextView
    var editTextView : EditText
    private var dataSet : CurrencyData? = null
    var rateData = CurrencyData.RateData()
        get() = field
        set(value){
            val previousValue = field
            field = value
            currencyShortNameTextView.text = field.name
            val currenyInstance = Currency.getInstance(field.name)
            currentLongNameTextView.text = currenyInstance.displayName
            var x = 0.0f
            val localDataSet = dataSet
            if(localDataSet != null){
                x = localDataSet.masterCoinQuantity * rateData.rateValue / localDataSet.masterRateData.rateValue
            }
            if(x <= 0.0f){
                editTextView.setText("")
            } else{
                editTextView.setText(x.toString())
            }

            if (field.name.isNotEmpty() && previousValue.name != field.name){
                var drawable : Drawable? = currencyFlagMap[field.name]
                if(drawable == null){
                    val drawableName = "flag_"+field.name.toLowerCase()
                    val drawableId = context.resources.getIdentifier(drawableName, "drawable", context.packageName)
                    drawable = ContextCompat.getDrawable(context, drawableId)
                    if(drawable != null){
                        currencyFlagMap.set(field.name, drawable)
                    }
                }
                imageView1.setImageDrawable(drawable)
            }
        }

    companion object{
        val currencyFlagMap = HashMap<String, Drawable>()
    }

    constructor(context: Context, dataSet: CurrencyData) : this(context, null){
        this.dataSet = dataSet
    }

    init {
        View.inflate(getContext(), R.layout.currency_display_view, this)
        imageView1 = this.findViewById(R.id.currency_image_view) as ImageView
        currencyShortNameTextView = this.findViewById(R.id.currency_short_name_text_view) as TextView
        currentLongNameTextView = this.findViewById(R.id.curreny_long_name_text_view) as TextView
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
                if(lastText.compareTo(currentText) == 0){
                    return
                }
                lastText = currentText

                //val floatValue = p0.toString().toFloat()
                var floatValue = 0.0f
                if (p0 != null && p0.isNotEmpty()){
                    floatValue = p0.toString().toFloat()
                }

                localDataSet.masterCoinQuantity = floatValue
                localDataSet.notifyAllDataChangedExceptMaster()
            }
        })
    }
}