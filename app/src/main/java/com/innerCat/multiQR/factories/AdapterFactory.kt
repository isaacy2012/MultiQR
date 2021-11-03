package com.innerCat.multiQR.factories

import com.innerCat.multiQR.fragments.DetailFragment
import com.innerCat.multiQR.strAdapter.CellAdapter
import java.util.*



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

