package com.innerCat.multiQR.strAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.innerCat.multiQR.activities.MainActivity
import com.innerCat.multiQR.assertions.assert
import com.innerCat.multiQR.databinding.MainRvItemBinding
import com.innerCat.multiQR.fragments.MasterFragmentDirections
import com.innerCat.multiQR.util.OptionalRegex
import com.innerCat.multiQR.views.CellView
import java.util.*

/**
 * String Adapter for Strings RecyclerView
 */
class CellAdapter(
    val strs: MutableList<String>
) :
    RecyclerView.Adapter<CellAdapter.ViewHolder>() {

    /**
     * Read only property for strList
     */
    val strList: List<String>
        get() = strs.toList()


    /**
     * Provide a direct reference to each of the views within a data str
     */
    inner class ViewHolder(var g: MainRvItemBinding, var context: Context) : RecyclerView.ViewHolder(g.root),
        View.OnClickListener {
        lateinit var str: String

        init {
            g.root.setOnClickListener(this)
        }

        /**
         * Handles the row being clicked
         *
         * @param view the strView
         */
        override fun onClick(view: View) {
//            val direction =
//                MasterFragmentDirections.actionMasterFragmentToDetailFragment(
//                    0
//                )
//            (context as MainActivity).g.appBarLayout.setExpanded(false)
//            view.findNavController().navigate(direction)
        }

    }

    /**
     * Reset.
     */
    fun reset() {
        strs.forEach { _ -> notifyItemRemoved(0) }
        strs.clear()
    }

    /**
     * Add a str
     *
     * @param str     the String to add
     */
    fun addString(str: String) {
        strs.add(0, str)
        notifyItemInserted(0)
    }


    /**
     * Remove an str.
     *
     * @param str     the str to remove
     */
    fun removeString(str: String) {
        notifyItemRemoved(strs.indexOf(str))
        strs.remove(str)
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
        holder.str = strs[position]
        val g: MainRvItemBinding = holder.g
        g.root.addView(CellView(holder.context, holder.str))
//        g.root.removeAllViews()
//        holder.str.forEach {
//            val cell = CellView(holder.context, it)
//            g.root.addView(cell)
//        }
    }

    /**
     * Returns the total str count
     */
    override fun getItemCount(): Int {
        return strs.size
    }

}
