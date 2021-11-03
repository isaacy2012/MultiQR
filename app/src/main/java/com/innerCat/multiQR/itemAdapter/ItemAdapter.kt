package com.innerCat.multiQR.itemAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.innerCat.multiQR.Item
import com.innerCat.multiQR.activities.MainActivity
import com.innerCat.multiQR.assertions.assert
import com.innerCat.multiQR.databinding.MainRvItemBinding
import com.innerCat.multiQR.fragments.MasterFragmentDirections
import com.innerCat.multiQR.viewmodels.MainViewModel
import com.innerCat.multiQR.views.CellView
import com.innerCat.multiQR.views.makeMoreHorizontal
import java.lang.Integer.min
import java.util.*


val MAX_COLS = 3

class ItemsNotUniqueException : RuntimeException()
/**
 * Item Adapter for Items RecyclerView
 */
class ItemAdapter(
    val viewModel: MainViewModel,
) :
    RecyclerView.Adapter<ItemAdapter.ViewHolder>() {
    private var itemSet: HashSet<Item>
    val items: List<Item>
        get() {
            return viewModel.items.value!!
        }

    /**
     * Pass in the tasks array into the Adapter
     *
     * @param items the items
     */
    init {
        // Try to preserve order if there are no duplicates
        this.itemSet = HashSet(items)
        // Otherwise make a new ArrayList from the HashSet
        if (this.items.size != this.itemSet.size) {
            throw ItemsNotUniqueException()
        }
    }

    /**
     * Read only property for itemList
     */
    val itemList: List<Item>
        get() = items.toList()


    /**
     * Provide a direct reference to each of the views within a data item
     */
    inner class ViewHolder(var g: MainRvItemBinding, var context: Context) : RecyclerView.ViewHolder(g.root),
        View.OnClickListener {
        lateinit var item: Item

        init {
            g.root.setOnClickListener(this)
        }

        /**
         * Handles the row being clicked
         *
         * @param view the itemView
         */
        override fun onClick(view: View) {
            (context as MainActivity).g.fab.shrink()
            val direction =
                MasterFragmentDirections.actionMasterFragmentToDetailFragment(
                    items.indexOf(item)
                )
            (context as MainActivity).g.appBarLayout.setExpanded(false)
            view.findNavController().navigate(direction)
        }

    }

    /**
     * Reset.
     */
    fun reset() {
        items.forEach { _ -> notifyItemRemoved(0) }
        viewModel.clearItems()
        itemSet.clear()
    }

    /**
     * Add a item
     *
     * @param item     the Item to add
     */
    fun addItem(item: Item) {
        if (itemSet.contains(item) == false) {
            viewModel.addItem(0, item)
            itemSet.add(item)
            notifyItemInserted(0)
        }
    }


    /**
     * Remove an item.
     *
     * @param item     the item to remove
     */
    fun removeItem(item: Item) {
        notifyItemRemoved(items.indexOf(item))
        itemSet.remove(item)
        viewModel.removeItem(item)
    }

    /**
     * Usually involves inflating a layout from XML and returning the holder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        return ViewHolder(MainRvItemBinding.inflate(LayoutInflater.from(context), parent, false), context)
    }

    /**
     * Populate the data into the ViewHolder
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Get the data model based on position
        holder.item = items[position]
        val g: MainRvItemBinding = holder.g
        g.root.removeAllViews()
        for (i in 0 until min(MAX_COLS, holder.item.strList.size)) {
            val cell = CellView(holder.context, holder.item.strList[i])
            g.root.addView(cell)
        }
        if (holder.item.strList.size > MAX_COLS) {
            g.root.addView(makeMoreHorizontal(holder.context))
        }
//        holder.item.strList.forEach {
//            val cell = CellView(holder.context, it)
//            g.root.addView(cell)
//        }
    }

    /**
     * Returns the total item count
     */
    override fun getItemCount(): Int {
        assert(items.size == itemSet.size, "Item set size mismatch")
        return items.size
    }

}
