package com.innerCat.multiQR.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.card.MaterialCardView
import com.innerCat.multiQR.R

open class HeaderTextView : LinearLayout {
    init {
        inflate(context, R.layout.header_text_view, this)
        layoutParams = LayoutParams(0, LayoutParams.WRAP_CONTENT, 1.0f)
//        cardElevation = 0f
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context) : super(context)

    constructor(context: Context, text: String) : super(context) {
        val textView: TextView = findViewById(R.id.textView)
        textView.text = text
    }
}
