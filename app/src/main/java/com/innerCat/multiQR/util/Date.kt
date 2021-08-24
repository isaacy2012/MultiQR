package com.innerCat.multiQR.util

import android.icu.text.SimpleDateFormat
import java.util.*

fun getCurrentTimeString() : String {
    val currentTime = Date(System.currentTimeMillis())
    val df = SimpleDateFormat("HH:mm yyyy-MM-dd", Locale.getDefault())
    return df.format(currentTime)
}