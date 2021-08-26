package com.innerCat.multiQR.factories

import com.innerCat.multiQR.Item
import com.innerCat.multiQR.fragments.DetailFragment
import com.innerCat.multiQR.fragments.MasterFragment
import com.innerCat.multiQR.itemAdapter.ItemAdapter
import com.innerCat.multiQR.strAdapter.CellAdapter
import java.util.*



/**
 * Item adapter from list
 *
 * @return the item adapter
 */
fun MasterFragment.itemAdapterFromList(list: MutableList<Item>): ItemAdapter {
    return ItemAdapter(this, list)
}


/**
 * Empty cell adapter.
 *
 * @return the str adapter
 */
fun DetailFragment.emptyCellAdapter(): CellAdapter {
    return CellAdapter(this, ArrayList())
}


/**
 * Cell adapter from list
 *
 * @return the str adapter
 */
fun DetailFragment.cellAdapterFromList(list: MutableList<String>): CellAdapter {
    return CellAdapter(this, list)
}

