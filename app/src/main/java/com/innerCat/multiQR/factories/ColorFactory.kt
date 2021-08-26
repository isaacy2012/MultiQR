package com.innerCat.multiQR.factories

import android.content.Context
import android.graphics.Color

/**
 * Gets attr color.
 *
 * @param context the context
 * @return the attr color
 */
fun getAttrColor(context: Context, colorId: Int): Int {
    val attribute = intArrayOf(colorId)
    val array = context.obtainStyledAttributes(attribute)
    return array.getColor(0, Color.TRANSPARENT)
}
