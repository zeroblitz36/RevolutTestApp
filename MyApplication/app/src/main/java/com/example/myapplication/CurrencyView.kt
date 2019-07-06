package com.example.myapplication

import android.content.Context
import android.graphics.*
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import java.util.*
import kotlin.collections.HashMap


class CurrencyView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle) {

    private var countryImageView : ImageView
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
                var countryBitmap : Bitmap? = currencyFlagMap[field.name]
                if(countryBitmap == null){
                    val drawableName = "flag_"+field.name.toLowerCase()
                    val drawableId = context.resources.getIdentifier(drawableName, "drawable", context.packageName)

                    val tempBitmap = BitmapFactory.decodeResource(context.resources, drawableId)
                    countryBitmap = getCircleCountryFlagBitmap(tempBitmap)
                    if(countryBitmap != null){
                        currencyFlagMap.set(field.name, countryBitmap)
                    }
                }
                countryImageView.setImageBitmap(countryBitmap)
            }
        }

    companion object{
        val currencyFlagMap = HashMap<String, Bitmap>()

        /*
        Function will only work if the width of the sourceBitmap is bigger than the height
         */
        private fun getCircleCountryFlagBitmap(sourceBitmap: Bitmap): Bitmap {
            val w = sourceBitmap.width
            val h = sourceBitmap.height

            val outputBitmap = Bitmap.createBitmap(h, h, Bitmap.Config.ARGB_8888)

            val canvas = Canvas(outputBitmap)
            val paintColor = Paint()
            paintColor.setFlags(Paint.ANTI_ALIAS_FLAG)

            val rectF = RectF(Rect(0, 0, h, h))

            canvas.drawRoundRect(rectF, h / 2.0f, h / 2.0f, paintColor)

            val paintImage = Paint()
            paintImage.setXfermode(PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP))
            canvas.drawBitmap(sourceBitmap, (h - w)/2.0f, 0.0f, paintImage)

            return outputBitmap
        }
    }

    constructor(context: Context, dataSet: CurrencyData) : this(context, null){
        this.dataSet = dataSet
    }

    init {
        View.inflate(getContext(), R.layout.currency_display_view, this)
        Log.d("Test", "Inflating new CurrencyView")
        countryImageView = this.findViewById(R.id.country_image_view) as ImageView
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
                //localDataSet.notifyAllDataChangedExceptMaster()

                for (i in 1..localDataSet.rates.size-1){
                    val x = localDataSet.rates[i]
                    val otherCurrencyView = x.currentlyAssignedViewHolder?.currencyView
                    if(otherCurrencyView != null){
                        //trigger update
                        otherCurrencyView.rateData = otherCurrencyView.rateData
                    }
                }
            }
        })
    }
}