/**
 * @author Isaac Young
 */
package com.innerCat.multiQR.factories

import android.content.Context
import com.innerCat.multiQR.itemAdapter.ItemAdapter
import com.innerCat.multiQR.util.stringToList
import java.util.ArrayList

/**
 * Empty item adapter.
 *
 * @return the item adapter
 */
fun emptyItemAdapter(context: Context): ItemAdapter {
    return ItemAdapter(context, ArrayList())
}

fun itemAdapterFromString(context: Context, str: String): ItemAdapter {
    val list = stringToList(str)
    return ItemAdapter(context, list)
}

