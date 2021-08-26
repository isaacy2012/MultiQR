package com.innerCat.multiQR.fragments

import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.InputType
import android.text.SpannableStringBuilder
import android.view.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.core.text.bold
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.zxing.integration.android.IntentIntegrator
import com.innerCat.multiQR.Item
import com.innerCat.multiQR.R
import com.innerCat.multiQR.activities.CaptureActivityPortrait
import com.innerCat.multiQR.activities.MainActivity
import com.innerCat.multiQR.activities.SettingsActivity
import com.innerCat.multiQR.databinding.FragmentMasterBinding
import com.innerCat.multiQR.databinding.MainActivityBinding
import com.innerCat.multiQR.databinding.ManualInputBinding
import com.innerCat.multiQR.factories.emptyItemAdapter
import com.innerCat.multiQR.factories.getManualAddTextWatcher
import com.innerCat.multiQR.factories.getSharedPreferences
import com.innerCat.multiQR.factories.itemAdapterFromList
import com.innerCat.multiQR.itemAdapter.ItemAdapter
import com.innerCat.multiQR.itemAdapter.ItemsNotUniqueException
import com.innerCat.multiQR.util.*
import com.innerCat.multiQR.views.HeaderTextView
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import java.io.File
import java.io.FileWriter

class MasterFragment : MainActivityFragment() {

    private lateinit var g: FragmentMasterBinding
    private lateinit var adapter: ItemAdapter
    private lateinit var matchRegex: OptionalRegex
    lateinit var splitRegex: OptionalRegex

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        g = FragmentMasterBinding.inflate(layoutInflater)
        setHasOptionsMenu(true)

        setRecyclerViewAdapter()

        mainG.toolbarLayout.title = getTitleString()

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
        // Create adapter from mainActivity.sharedPreferences
        adapter =
            if (mainActivity.items.isEmpty() == false) {
                try {
                    itemAdapterFromList(
                        mainActivity.items
                    )
                } catch (e: ItemsNotUniqueException) {
                    mainActivity.items = ArrayList(HashSet(mainActivity.items))
                    itemAdapterFromList(
                        mainActivity.items
                    )
                }
            } else {
                emptyItemAdapter()
            }
        // Attach the adapter to the recyclerview to populate items
        g.rvItems.adapter = adapter
        // Set layout manager to position the items
        g.rvItems.layoutManager = LinearLayoutManager(requireActivity())
    }

    private fun refresh() {
        matchRegex = mainActivity.sharedPreferences.getMatchRegex(requireActivity())
        splitRegex = mainActivity.sharedPreferences.getSplitRegex(requireActivity())
        populateHeader()
        adapter.refreshAll(splitRegex)
    }

    /**
     * Populate the table header from mainActivity.sharedPreferences
     */
    private fun populateHeader() {
        g.headerLayout.removeAllViews()
        getHeaderStrings()?.forEach {
            val spacedTextView = HeaderTextView(requireActivity(), it)
            g.headerLayout.addView(spacedTextView)
        }
    }

    private fun getHeaderStrings(): List<String>? {
        val headerString =
            mainActivity.sharedPreferences.getString(
                getString(R.string.sp_column_headers_string),
                null
            )
        if (headerString == null || headerString == "") {
            return null
        }
        return splitRegex.split(headerString)
    }


    /**
     * Gets the title string counting how many items there are
     * @return How many items there are as a formatted string
     */
    private fun getTitleString(): String {
        return when (val count = adapter.itemCount) {
            0 -> {
                "No items"
            }
            1 -> {
                "$count item"
            }
            else -> {
                "$count items"
            }
        }
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
            .setPositiveButton(
                "Delete"
            ) { _: DialogInterface?, _: Int ->
                adapter.reset()
                clearData(mainActivity.sharedPreferences, getString(R.string.sp_items))
            }
            .setNegativeButton(
                "Cancel"
            ) { _: DialogInterface?, _: Int -> }
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
            .setPositiveButton(
                "Ok"
            ) { _: DialogInterface?, _: Int ->
                val output = manualG.edit.text.toString()
                if (matchRegex.passes(output)) {
                    addItem(Item(output, splitRegex))
                } else {
                    showMatchFailureDialog(
                        output,
                        (matchRegex as EnabledRegex).regex
                    ) {} // don't do anything else when manual add
                }
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
        if (mainActivity.sharedPreferences.getString(
                getString(R.string.sp_item_type),
                getString(R.string.default_item_type)
            )
                .equals("numeric")
        ) {
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
        if (adapter.itemList.size >= resources.getInteger(R.integer.max_items_limit)) {
            // Use the Builder class for convenient dialog construction
            val builder = MaterialAlertDialogBuilder(
                requireActivity(),
                R.style.MaterialAlertDialog_Rounded
            )
            builder.setTitle("Item Limit Reached")
                .setMessage("Sorry, you can only add up to 50 items. An update will be released later, which will allow adding of unlimited items.")
                .setPositiveButton(
                    "Ok"
                ) { _: DialogInterface?, _: Int ->
                }
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
        val filesDir = requireActivity().filesDir
        // Delete old files
        filesDir.listFiles()?.forEach {
            it.delete()
        }
        // Make new file
        val file = File(
            filesDir,
            mainActivity.sharedPreferences.getExportFileName(requireActivity()) + ".csv"
        )
        val fileWriter = FileWriter(file)
        val csvPrinter = getHeaderStrings()?.toTypedArray()?.let {
            CSVPrinter(
                fileWriter,
                CSVFormat.Builder.create().setHeader(*it).setAllowMissingColumnNames(true)
                    .build()
            )
        } ?: run {
            CSVPrinter(fileWriter, CSVFormat.DEFAULT)
        }

        // Write file with csvPrinter
        adapter.itemList.forEach {
            csvPrinter.printRecord(it.strList)
        }
        fileWriter.close()

        // Send file
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(
            Intent.EXTRA_STREAM,
            FileProvider.getUriForFile(requireActivity(), "com.multiQR.FileProvider", file)
        );
        intent.type = "text/csv";
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivity(intent)
    }

    /**
     * Add an item to the adapter and persistent data storage
     * @param item the Id object to add
     */
    private fun addItem(item: Item) {
        mutateData { adapter.addItem(item) }
    }

    /**
     * Remove an item from the adapter and persistent data storage
     * @param item The Id object to remove
     */
    internal fun removeItem(item: Item) {
        mutateData { adapter.removeItem(item) }
    }

    /**
     * Save the current adapter information to the persistent data storage
     */
    internal fun mutateData(run: () -> Unit) {
        run()
        saveData(adapter.itemList, mainActivity.sharedPreferences, getString(R.string.sp_items))
        mainG.toolbarLayout.title = getTitleString()
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
            .setPositiveButton(
                "Add"
            ) { _: DialogInterface?, _: Int ->
                addItem(Item(output, splitRegex))
                onComplete()
            }
            .setNegativeButton(
                "Discard"
            ) { _: DialogInterface?, _: Int ->
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
                val output = contents.filterNot { it == '\n' }.filterNot { it == '\r' }
                if (matchRegex.passes(output)) {
                    addItem(Item(output, splitRegex))
                    if (batchScanEnabled()) {
                        initiateScan()
                    }
                } else {
                    showMatchFailureDialog(
                        output, (matchRegex as EnabledRegex).regex
                    ) {
                        if (batchScanEnabled()) {
                            initiateScan()
                        }
                    }
                }
            }
        } ?: super.onActivityResult(requestCode, resultCode, data)
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
        return mainActivity.sharedPreferences.getBoolean(getString(R.string.sp_batch_scan), true)
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