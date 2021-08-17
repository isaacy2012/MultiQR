/**
 * @author Isaac Young
 */
package com.innerCat.multiQR.util

import com.innerCat.multiQR.Item

/**
 * Parse a list of strings into a single CSV
 *
 * NOTE: this util function is basic for the MVP, but kept in case it is needed to format the
 * output differently in future
 *
 * @param list - list to parse
 */
fun listToString(list: List<Item>): String {
    return list.joinToString(","){
        it.dataString
    }
}

/**
 * Parse a list of strings into a single CSV for email
 *
 * NOTE: this util function is basic for the MVP, but kept in case it is needed to format the
 * output differently in future
 *
 * @param list - list to parse
 */
fun listToEmailString(list: List<Item>): String {
    return list.joinToString(",\n"){
        it.dataString
    }
}

fun stringToList(str: String): ArrayList<Item> {
    return if (str.isEmpty()) {
        ArrayList()
    } else {
        ArrayList(str.split(",").map{ Item(it)})
    }
}
