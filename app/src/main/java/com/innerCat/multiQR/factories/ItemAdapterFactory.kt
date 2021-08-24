package com.innerCat.multiQR.factories

import android.content.Context
import com.innerCat.multiQR.Item
import com.innerCat.multiQR.itemAdapter.ItemAdapter
import com.innerCat.multiQR.util.DisabledRegex
import com.innerCat.multiQR.util.EnabledRegex
import com.innerCat.multiQR.util.OptionalRegex
import java.util.ArrayList

/**
 * Empty item adapter.
 *
 * @return the item adapter
 */
fun emptyItemAdapter(context: Context): ItemAdapter {
    return ItemAdapter(DisabledRegex(), ArrayList())
}

fun itemAdapterFromList(context: Context, splitRegex: OptionalRegex, list: MutableList<Item>): ItemAdapter {
    return ItemAdapter(splitRegex, list)
}

