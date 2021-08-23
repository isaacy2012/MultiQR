/**
 * @author Isaac Young
 */
package com.innerCat.multiQR.factories

import android.content.Context
import com.innerCat.multiQR.itemAdapter.ItemAdapter
import com.innerCat.multiQR.util.DisabledRegex
import com.innerCat.multiQR.util.EnabledRegex
import com.innerCat.multiQR.util.OptionalRegex
import com.innerCat.multiQR.util.stringToList
import java.util.ArrayList

/**
 * Empty item adapter.
 *
 * @return the item adapter
 */
fun emptyItemAdapter(context: Context): ItemAdapter {
    return ItemAdapter(context, DisabledRegex(), ArrayList())
}

fun itemAdapterFromString(context: Context, splitRegex: OptionalRegex, str: String): ItemAdapter {
    val list = stringToList(str)
    return ItemAdapter(context, splitRegex, list)
}

