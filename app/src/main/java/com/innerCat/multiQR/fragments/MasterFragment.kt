package com.innerCat.multiQR.fragments

import android.content.DialogInterface
import android.content.Intent
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.InputType
import android.text.SpannableStringBuilder
import android.view.*
import android.view.View.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.text.bold
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.zxing.integration.android.IntentIntegrator
import com.innerCat.multiQR.Item
import com.innerCat.multiQR.R
import com.innerCat.multiQR.activities.CaptureActivityPortrait
import com.innerCat.multiQR.activities.SettingsActivity
import com.innerCat.multiQR.databinding.FragmentMasterBinding
import com.innerCat.multiQR.databinding.ManualInputBinding
import com.innerCat.multiQR.factories.getManualAddTextWatcher
import com.innerCat.multiQR.itemAdapter.ItemAdapter
import com.innerCat.multiQR.itemAdapter.ItemsNotUniqueException
import com.innerCat.multiQR.itemAdapter.MAX_COLS
import com.innerCat.multiQR.util.clearData
import com.innerCat.multiQR.util.getItemType
import com.innerCat.multiQR.views.HeaderTextView
import com.innerCat.multiQR.views.makeMoreHorizontal
import java.lang.Integer.min


/**
 * Master fragment
 *
 * @constructor Create empty Master fragment
 */
class MasterFragment : AbstractMainActivityFragment() {

    private lateinit var g: FragmentMasterBinding
    private lateinit var adapter: ItemAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        g = FragmentMasterBinding.inflate(layoutInflater)
        setHasOptionsMenu(true)

        mainG.toolbar.setNavigationIcon(R.drawable.ic_multiqr_icon_24)
        navigationImageButton?.visibility = INVISIBLE
        mainG.toolbar.setNavigationOnClickListener {}

        setRecyclerViewAdapter()


        mainG.fab.text = getString(R.string.fab_scan_item)
        mainG.fab.setOnClickListener {
            initiateScan()
        }

        refresh()

        return g.root
    }

    /**
     * Set the recyclerview Adapter
     */
    private fun setRecyclerViewAdapter() {
        // Create adapter from viewModel.sharedPreferences
        adapter =
            try {
                ItemAdapter(viewModel)
            } catch (e: ItemsNotUniqueException) {
                viewModel.items = ArrayList(HashSet(viewModel.items))
                ItemAdapter(viewModel)
            }
        // Attach the adapter to the recyclerview to populate items
        g.rvItems.adapter = adapter
        // Set layout manager to position the items
        g.rvItems.layoutManager = LinearLayoutManager(requireActivity())
        viewModel.rvVisibility.observe(viewLifecycleOwner){updateRVVisibility(it)}
        updateRVVisibility(viewModel.rvVisibility.value?:false)
    }

    /**
     * Refresh regex from sharedPreferences
     */
    private fun refresh() {
        viewModel.refreshRegex()
        populateHeader()
    }

    /**
     * Populate the table header from viewModel.sharedPreferences
     */
    private fun populateHeader() {
        g.headerLayout.removeAllViews()
        viewModel.getHeaderStrings()?.let {
            for (i in 0 until min(MAX_COLS, it.size)) {
                val spacedTextView = HeaderTextView(requireActivity(), it[i])
                g.headerLayout.addView(spacedTextView)
            }
            if (it.size > MAX_COLS) {
                g.headerLayout.addView(makeMoreHorizontal(mainActivity).apply {
                    visibility = INVISIBLE
                })
            }
        }
//        getHeaderStrings()?.forEach {
//            val spacedTextView = HeaderTextView(requireActivity(), it)
//            g.headerLayout.addView(spacedTextView)
//        }
    }


    /**
     * Ask the user if they are sure that all the IDs should be cleared
     */
    private fun askToClearIds() {
        // Use the Builder class for convenient dialog construction
        val builder =
            MaterialAlertDialogBuilder(requireActivity(), R.style.MaterialAlertDialog_Rounded)

        builder.setMessage(
            "Are you sure you wish to clear all data?"
        )
            .setPositiveButton("Delete") { _: DialogInterface?, _: Int ->
                adapter.reset()
                clearData(viewModel.sharedPreferences, getString(R.string.sp_items))
            }
            .setNegativeButton("Cancel") { _: DialogInterface?, _: Int -> }
        val dialog = builder.create()
        dialog.show()
    }

    /**
     * Manually add an entry
     */
    private fun addManually() {
        if (checkShowLimitDialog() == false) {
            return
        }
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
                val input = manualG.edit.text.toString()
                viewModel.add(input,
                    onSuccess = { item -> addItem(item) },
                    onFailure = { regex ->
                        showMatchFailureDialog(
                            input,
                            regex
                        ) {} // don't do anything else when manual add
                    })
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
        if (viewModel.sharedPreferences.getItemType(mainActivity).equals("numeric")) {
            manualG.edit.inputType = InputType.TYPE_CLASS_NUMBER
        }
        manualG.edit.addTextChangedListener(
            getManualAddTextWatcher(
                manualG.edit,
                okButton
            )
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // Inflate the menu; requireActivity() adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_main, menu)
        fadeInMenuIcons(menu, R.id.action_add_manually)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_add_manually -> {
                addManually()
                true
            }
            R.id.action_clear -> {
                askToClearIds()
                true
            }
            R.id.action_export -> {
                export()
                true
            }
            R.id.action_settings -> {
                val intent = Intent(requireActivity(), SettingsActivity::class.java)
                launchActivityForResult.launch(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Check if we have reached the limit of adding items, and if so, show a dialog
     */
    private fun checkShowLimitDialog(): Boolean {
        val limit = resources.getInteger(R.integer.max_items_limit)
        if (adapter.itemList.size >= limit) {
            // Use the Builder class for convenient dialog construction
            val builder = MaterialAlertDialogBuilder(
                requireActivity(),
                R.style.MaterialAlertDialog_Rounded
            )
            builder.setTitle("Item Limit Reached")
                .setMessage("Sorry, you can only add up to $limit items. An update will be released later, which will allow adding of unlimited items.")
                .setPositiveButton("Ok") { _: DialogInterface?, _: Int -> }
            val dialog = builder.create()
            dialog.show()
            return false
        }
        return true
    }

    /**
     * Initiate a scan
     */
    private fun initiateScan() {
        if (checkShowLimitDialog() == false) {
            return
        }
        val integrator = IntentIntegrator(requireActivity())
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setPrompt("Press back to finish")
        integrator.setOrientationLocked(true)
        integrator.setBeepEnabled(false)
        integrator.captureActivity = (CaptureActivityPortrait::class.java)
        integrator.initiateScan()
    }

    /**
     * Export a CSV to another app
     */
    private fun export() {
        startActivity(viewModel.exportToCsvIntent())
    }

    /**
     * mutateData
     */
    private fun mutateData(run: () -> Unit) {
        mainActivity.mutateData(run)
    }

    /**
     * Add an item to the adapter and persistent data storage
     * @param item the Id object to add
     */
    fun addItem(item: Item) {
        mutateData { adapter.addItem(item) }
    }

    /**
     * Remove an item from the adapter and persistent data storage
     * @param item The Id object to remove
     */
    internal fun removeItem(item: Item) {
        mutateData { adapter.removeItem(item) }
    }


    fun updateRVVisibility(visible: Boolean) {
        if (visible) {
            g.rvItems.visibility = VISIBLE
            g.emptyLayout.visibility = GONE
        } else {
            g.rvItems.visibility = GONE
            g.emptyLayout.visibility = VISIBLE
        }
    }

    /**
     * Show match failure dialog
     * @param output the string that failed to match
     * @param regex the regex
     */
    private fun showMatchFailureDialog(output: String, regex: Regex, onComplete: () -> Unit) {
        val builder =
            MaterialAlertDialogBuilder(requireActivity(), R.style.MaterialAlertDialog_Rounded)
        val ssb = SpannableStringBuilder()
            .append("Failed to match ")
            .bold { append(output) }
            .append(" to Regex string ")
            .bold { append(regex.toString()) }
            .append(".\n\nThis behaviour can be changed in settings.\n\nDo you wish to add it anyway?")

        builder.setTitle(getString(R.string.regex_match_failure_title))
        builder.setMessage(
            ssb
        )
            .setPositiveButton("Add") { _: DialogInterface?, _: Int ->
                addItem(Item(output, viewModel.splitRegex))
                onComplete()
            }
            .setNegativeButton("Discard") { _: DialogInterface?, _: Int ->
                onComplete()
            }
        val dialog = builder.create()
        dialog.show()
    }


    /**
     * Get the results
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        IntentIntegrator.parseActivityResult(requestCode, resultCode, data)?.let { result ->
            // If user didn't cancel, there will be contents
            result.contents?.let { contents ->
                beep()
                val input = contents.filterNot { it == '\n' }.filterNot { it == '\r' }
                viewModel.add(input,
                    onSuccess = { item ->
                        addItem(item)
                        if (batchScanEnabled()) {
                            initiateScan()
                        }
                    },
                    onFailure = { regex ->
                        showMatchFailureDialog(
                            input, regex
                        ) {
                            if (batchScanEnabled()) {
                                initiateScan()
                            }
                        }
                    }
                )
            } ?: super.onActivityResult(requestCode, resultCode, data)
        }
    }

    var launchActivityForResult =
        /**
         * On Activity result
         * From settings, update the regex splitting of the cells.
         */
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            // set up regex
            refresh()
        }


    /**
     * @return Whether batch scanning is enabled
     */
    private fun batchScanEnabled(): Boolean {
        return viewModel.sharedPreferences.getBoolean(getString(R.string.sp_batch_scan), true)
    }

    /**
     * Make a beeping noise, indicating a successful scan
     */
    private fun beep() {
        val length = 125
        val toneGenerator =
            ToneGenerator(AudioManager.STREAM_MUSIC, ToneGenerator.MAX_VOLUME)
        toneGenerator.startTone(ToneGenerator.TONE_CDMA_SOFT_ERROR_LITE, length)
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            toneGenerator.release()
        }, (length + 50).toLong())
    }

}