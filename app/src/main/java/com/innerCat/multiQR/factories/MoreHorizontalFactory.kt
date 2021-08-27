package com.innerCat.multiQR.views

import android.content.Context
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.innerCat.multiQR.R

/**
 * Create "more horizontal" View
 */
fun makeMoreHorizontal(context: Context): ImageView {
    return ImageView(context).apply {
        setImageResource(R.drawable.ic_baseline_more_horiz_24)
        layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
            weight = 0f
            gravity = Gravity.CENTER_VERTICAL
        }
        alpha = 0.5f
    }
}