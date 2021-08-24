package com.innerCat.multiQR.util

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.innerCat.multiQR.Item
import com.innerCat.multiQR.R

fun write(stringToWrite: String, sharedPreferences: SharedPreferences, spItemsStr: String) {
    val editor = sharedPreferences.edit()
    editor.putString(spItemsStr, stringToWrite)
    editor.apply()
}

fun saveData(itemList: List<Item>, sharedPreferences: SharedPreferences, spItemsStr: String) {
    write(Gson().toJson(itemList), sharedPreferences, spItemsStr)
}

fun loadData(context: Context, sharedPreferences: SharedPreferences): MutableList<Item> {
    val itemsString = sharedPreferences.getString(context.getString(R.string.sp_items), null)
    return if (itemsString != null) {
        Gson().fromJson(itemsString, Array<Item>::class.java).toMutableList()
    } else {
        ArrayList()
    }

}


fun clearData(sharedPreferences: SharedPreferences, spItemsStr: String) {
    write("", sharedPreferences, spItemsStr)
}
