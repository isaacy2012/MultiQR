package com.innerCat.multiQR.util

import android.content.Context
import android.content.SharedPreferences
import com.innerCat.multiQR.R

const val dateString: String = "\$date"

/**
 * Get the export filename from sharedPreferences
 */
fun SharedPreferences.getExportFileName(context: Context): String {
    return getString(
        context.getString(R.string.sp_export_file_name),
        context.getString(R.string.default_export_file_name)
    )!!.replace(dateString, getCurrentTimeString())
}

/**
 * Get the splitRegex from sharedPreferences
 */
fun SharedPreferences.getSplitRegex(context: Context): OptionalRegex {
    val splitRegexEnable =
        getBoolean(context.getString(R.string.sp_split_columns_enable), false)
    val splitRegexString =
        getString(context.getString(R.string.sp_split_regex_string), null)
    return if (splitRegexEnable && splitRegexString != null) {
        EnabledRegex(splitRegexString)
    } else {
        DisabledRegex()
    }
}

/**
 * Get the matchRegex from sharedPreferences
 */
fun SharedPreferences.getMatchRegex(context: Context): OptionalRegex {
    val matchRegexEnable =
        getBoolean(context.getString(R.string.sp_match_regex_enable), false)
    val matchRegexString = getString(
        context.getString(R.string.sp_match_regex_string),
        context.getString(R.string.default_regex_string)
    )
    return if (matchRegexEnable && matchRegexString != null) EnabledRegex(matchRegexString) else DisabledRegex()
}
