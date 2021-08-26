package com.innerCat.multiQR.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.InputType
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.innerCat.multiQR.R
import com.innerCat.multiQR.activities.State
import com.innerCat.multiQR.databinding.FragmentDetailBinding
import com.innerCat.multiQR.databinding.ManualInputBinding
import com.innerCat.multiQR.factories.cellAdapterFromList
import com.innerCat.multiQR.factories.emptyCellAdapter
import com.innerCat.multiQR.factories.getManualAddTextWatcher
import com.innerCat.multiQR.strAdapter.CellAdapter
import com.innerCat.multiQR.util.getItemType

class DetailFragment : MainActivityFragment() {

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

        g = FragmentDetailBinding.inflate(layoutInflater)
        setHasOptionsMenu(true)

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

}