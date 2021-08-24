package com.innerCat.multiQR.factories

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager


fun getSharedPreferences(context: Context): SharedPreferences? {
    return PreferenceManager.getDefaultSharedPreferences(context)
}
