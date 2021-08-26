package com.innerCat.multiQR.factories

import com.innerCat.multiQR.Item
import com.innerCat.multiQR.fragments.MasterFragment
import com.innerCat.multiQR.itemAdapter.ItemAdapter
import java.util.ArrayList

/**
 * Empty item adapter.
 *
 * @return the item adapter
 */
fun MasterFragment.emptyItemAdapter(): ItemAdapter {
    return ItemAdapter(this, ArrayList())
}

fun MasterFragment.itemAdapterFromList(list: MutableList<Item>): ItemAdapter {
    return ItemAdapter(this, list)
}

