/**
 * @author Nathanael Rais, Isaac Young
 */
package com.innerCat.multiQR.util

import android.icu.text.SimpleDateFormat
import java.util.*

fun getCurrentTimeString() : String {
    val c = Date(System.currentTimeMillis())
    println("Current time => $c")

    val df = SimpleDateFormat("HH:mm yyyy-MM-dd", Locale.getDefault())
    return df.format(c)
}