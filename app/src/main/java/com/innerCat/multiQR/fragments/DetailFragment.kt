package com.innerCat.multiQR.fragments

import android.content.DialogInterface
import android.graphics.Canvas
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.InputType
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.innerCat.multiQR.R
import com.innerCat.multiQR.activities.State
import com.innerCat.multiQR.databinding.FragmentDetailBinding
import com.innerCat.multiQR.databinding.ManualInputBinding
import com.innerCat.multiQR.factories.cellAdapterFromList
import com.innerCat.multiQR.factories.emptyCellAdapter
import com.innerCat.multiQR.factories.getAttrColor
import com.innerCat.multiQR.factories.getManualAddTextWatcher
import com.innerCat.multiQR.strAdapter.CellAdapter
import com.innerCat.multiQR.util.getItemType


class DetailFragment : AbstractMainActivityFragment() {

    private lateinit var g: FragmentDetailBinding
    private lateinit var adapter: CellAdapter
    private val index: Int
        get() {
            return requireArguments().getInt("index")
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        mainActivity.state = State.DETAIL_FRAGMENT

        mainG.toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        navigationImageButton?.visibility = View.INVISIBLE
        mainG.toolbar.setNavigationOnClickListener { mainActivity.onBackPressed() }


        g = FragmentDetailBinding.inflate(layoutInflater)
        setHasOptionsMenu(true)

        ItemTouchHelper(getItemTouchHelperCallback()).attachToRecyclerView(g.rvCells)
        setRecyclerViewAdapter()

        mainG.fab.text = getString(R.string.fab_add_column)
        Handler(Looper.getMainLooper()).postDelayed({
            mainG.fab.extend()
        }, resources.getInteger(R.integer.fab_animation_duration).toLong())
        mainG.fab.setOnClickListener {
            showAddItemDialog()
        }

        return g.root
    }

    /**
     * Set the recyclerview Adapter
     */
    private fun setRecyclerViewAdapter() {
        adapter =
            if (mainActivity.items.isEmpty() == false) {
                cellAdapterFromList(
                    mainActivity.items[index].strList
                )
            } else {
                emptyCellAdapter()
            }

        // Attach the adapter to the recyclerview to populate items
        g.rvCells.adapter = adapter
        // Set layout manager to position the items
        g.rvCells.layoutManager = LinearLayoutManager(requireActivity())
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // Inflate the menu; requireActivity() adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_detail, menu)
        fadeInMenuIcons(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete_item -> {
                mainActivity.mutateData { mainActivity.deleteItemAt(index) }
                mainActivity.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Show the add item dialog
     */
    private fun showAddItemDialog() {
        // Use the Builder class for convenient dialog construction
        val builder = MaterialAlertDialogBuilder(
            requireActivity(),
            R.style.MaterialAlertDialog_Rounded
        )
        val manualG: ManualInputBinding = ManualInputBinding.inflate(layoutInflater)

        manualG.edit.requestFocus()

        builder.setTitle("Add Item")
            .setView(manualG.root)
            .setPositiveButton("Ok") { _: DialogInterface?, _: Int ->
                val output = manualG.edit.text.toString()
                mainActivity.mutateData { adapter.addString(output) }
            }
            .setNegativeButton(
                "Cancel"
            ) { _: DialogInterface?, _: Int ->
            }
        val dialog = builder.create()
        dialog.show()
        dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        val okButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        okButton.isEnabled = true
        if (mainActivity.sharedPreferences.getItemType(mainActivity).equals("numeric")) {
            manualG.edit.inputType = InputType.TYPE_CLASS_NUMBER
        }
        manualG.edit.addTextChangedListener(
            getManualAddTextWatcher(
                manualG.edit,
                okButton
            )
        )
    }


    private fun getItemTouchHelperCallback(): ItemTouchHelper.Callback {
        return object : ItemTouchHelper.Callback() {
            /**
             * when an item is in the process of being moved
             * @param recyclerView  the recyclerView
             * @param viewHolder    the viewholder
             * @param target        the target viewHolder
             * @return whether the move was handled
             */
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                // get the viewHolder's and target's positions in your adapter data, swap them
                val fromPosition = viewHolder.adapterPosition
                val toPosition = target.adapterPosition
                val strs: MutableList<String> = adapter.strs
                if (fromPosition == toPosition) {
                    return true
                } else {
                    val thisStr: String = strs[fromPosition]
                    strs.removeAt(fromPosition)
                    strs.add(toPosition, thisStr)
                }
                // and notify the adapter that its dataset has changed
                adapter.notifyItemMoved(viewHolder.adapterPosition, target.adapterPosition)
                mainActivity.mutateData {  }
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

            //defines the enabled move directions in each state (idle, swiping, dragging).
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                return makeFlag(
                    ItemTouchHelper.ACTION_STATE_DRAG,
                    ItemTouchHelper.DOWN or ItemTouchHelper.UP or ItemTouchHelper.START or ItemTouchHelper.END
                )
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                var scale = 1.0f
                if (isCurrentlyActive) {
                    scale = 1.02f
                }
                val dur = resources.getInteger(R.integer.ith_animation_duration).toLong()

                val cardView: CardView = viewHolder.itemView.findViewById(R.id.cardView)
                cardView.animate().apply {duration = dur}.scaleX(scale)
                cardView.animate().apply {duration = dur}.scaleY(scale)

                if (isCurrentlyActive) {
                    context?.let {
                        cardView.setCardBackgroundColor(getAttrColor(it, R.attr.colorOnCardSelected))
                    }
                } else {
                    context?.let {
                        cardView.setCardBackgroundColor(getAttrColor(it, R.attr.colorOnCard))
                    }
                }
//                cardView.animate().scaleY(end)
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }

        }
    }

}