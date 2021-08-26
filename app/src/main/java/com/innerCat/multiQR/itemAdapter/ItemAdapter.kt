package com.innerCat.multiQR.itemAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.innerCat.multiQR.Item
import com.innerCat.multiQR.activities.MainActivity
import com.innerCat.multiQR.databinding.MainRvItemBinding
import com.innerCat.multiQR.assertions.assert
import com.innerCat.multiQR.fragments.MasterFragment
import com.innerCat.multiQR.fragments.MasterFragmentDirections
import com.innerCat.multiQR.util.OptionalRegex
import com.innerCat.multiQR.views.CellView
import java.lang.RuntimeException
import java.util.*


class ItemsNotUniqueException : RuntimeException()
/**
 * Item Adapter for Items RecyclerView
 */
class ItemAdapter(
    val fragment: MasterFragment,
    private val items: MutableList<Item>
) :
    RecyclerView.Adapter<ItemAdapter.ViewHolder>() {
    private var itemSet: HashSet<Item>

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
                    (context as MainActivity).items.indexOf(item)
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
        items.clear()
        itemSet.clear()
    }

    /**
     * Add a item
     *
     * @param item     the Item to add
     */
    fun addItem(item: Item) {
        if (itemSet.contains(item) == false) {
            itemSet.add(item)
            items.add(0, item)
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
        items.remove(item)
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
        holder.item.strList.forEach {
            val cell = CellView(holder.context, it)
            g.root.addView(cell)
        }
    }

    /**
     * Returns the total item count
     */
    override fun getItemCount(): Int {
        assert(items.size == itemSet.size, "Item set size mismatch")
        return items.size
    }

}
