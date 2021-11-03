package com.innerCat.multiQR.strAdapter

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.innerCat.multiQR.R
import com.innerCat.multiQR.activities.MainActivity
import com.innerCat.multiQR.databinding.MainRvItemBinding
import com.innerCat.multiQR.databinding.ManualInputBinding
import com.innerCat.multiQR.factories.getManualAddTextWatcher
import com.innerCat.multiQR.fragments.DetailFragment
import com.innerCat.multiQR.util.getItemType
import com.innerCat.multiQR.views.CellView

/**
 * String Adapter for Strings RecyclerView
 */
class CellAdapter(
    val fragment: DetailFragment,
    val strs: MutableList<String>
) :
    RecyclerView.Adapter<CellAdapter.ViewHolder>() {

    /**
     * Provide a direct reference to each of the views within a data str
     */
    inner class ViewHolder(var g: MainRvItemBinding, var context: Context) :
        RecyclerView.ViewHolder(g.root),
        View.OnClickListener, View.OnLongClickListener {
        lateinit var str: String

        init {
            g.root.setOnClickListener(this)
            g.root.setOnLongClickListener(this)
        }

        /**
         * Handles the row being clicked
         *
         * @param view the strView
         */
        override fun onClick(view: View) {
            showEditDialog()
        }

        fun showEditDialog() {
            val builder = MaterialAlertDialogBuilder(context, R.style.MaterialAlertDialog_Rounded)
            val manualG: ManualInputBinding =
                ManualInputBinding.inflate((context as MainActivity).layoutInflater)

            manualG.edit.setText(str)
            manualG.edit.requestFocus()

            builder.setTitle("Edit Item")
                .setView(manualG.root)
                .setPositiveButton("Ok") { _: DialogInterface?, _: Int ->
                    val index = strs.indexOf(str)
                    strs[index] = manualG.edit.text.toString()
                    notifyItemChanged(index)
                    fragment.viewModel.mutateData {}
                }
                .setNegativeButton("Cancel") { _: DialogInterface?, _: Int ->
                    // User cancelled
                }
                .setNeutralButton("Delete") { _: DialogInterface?, _: Int ->
                    val index = strs.indexOf(str)
                    strs.removeAt(index)
                    notifyItemRemoved(index)
                    fragment.viewModel.mutateData {}
                }

            val dialog = builder.create()
            dialog.show()
            dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
            val okButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            okButton.isEnabled = true
            if (fragment.viewModel.sharedPreferences.getItemType(fragment.mainActivity)
                    .equals("numeric")) {
                manualG.edit.inputType = InputType.TYPE_CLASS_NUMBER
            }
            manualG.edit.addTextChangedListener(
                getManualAddTextWatcher(
                    manualG.edit,
                    okButton
                )
            )
        }

        override fun onLongClick(p0: View): Boolean {
            return true
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
        strs.add(str)
        notifyItemInserted(strs.size)
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
        return ViewHolder(
            MainRvItemBinding.inflate(LayoutInflater.from(context), parent, false),
            context
        )
    }

    /**
     * Populate the data into the ViewHolder
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Get the data model based on position
        holder.str = strs[position]
        val g: MainRvItemBinding = holder.g
        g.root.removeAllViews()
        g.root.addView(CellView(holder.context, holder.str))
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
