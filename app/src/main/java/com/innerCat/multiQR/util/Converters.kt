package com.innerCat.multiQR.util

import android.content.res.Resources
import android.util.TypedValue

/**
 * Converts dp to pixels. Used for setting padding programmatically and responsively
 *
 * @param dp the dp
 * @param r  resources
 * @return the number of pixels
 */
fun fromDpToPixels(dp: Int, r: Resources): Float {
    val px = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp.toFloat(),
        r.displayMetrics
    )
    return px
}
