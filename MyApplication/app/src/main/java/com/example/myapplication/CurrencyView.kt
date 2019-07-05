package com.example.myapplication

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    private var editTextView : EditText
    var rateData = CurrencyData.RateData()
        get() = field
        set(value){
            field = value
            textView1.text = field.name
            textView2.text = field.name + " country"
            editTextView.setText(field.value.toString())
        }

 //   constructor(context: Context) : this(context, null)
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

    fun setCurrencyValue(value: Float){
        editTextView.setText(value.toString())
    }
    */
}