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
fun listToString(list: List<Item>): kotlin.String {
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
fun listToEmailString(list: List<Item>): kotlin.String {
    return list.joinToString(",\n"){
        it.dataString
    }
}

