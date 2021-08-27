package com.innerCat.multiQR.factories

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager


/**
 * Return the default sharedPreferences
 */
fun getSharedPreferences(context: Context): SharedPreferences? {
    return PreferenceManager.getDefaultSharedPreferences(context)
}
