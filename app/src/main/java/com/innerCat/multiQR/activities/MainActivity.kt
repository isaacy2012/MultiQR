package com.innerCat.multiQR.activities

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
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.text.bold
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.zxing.integration.android.IntentIntegrator
import com.innerCat.multiQR.Item
import com.innerCat.multiQR.R
import com.innerCat.multiQR.databinding.MainActivityBinding
import com.innerCat.multiQR.databinding.ManualInputBinding
import com.innerCat.multiQR.factories.*
import com.innerCat.multiQR.itemAdapter.ItemAdapter
import com.innerCat.multiQR.util.*
import com.innerCat.multiQR.views.HeaderTextView
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import java.io.File
import java.io.FileWriter


/**
 * Main Activity Class
 */
class MainActivity : AppCompatActivity() {

    private lateinit var g: MainActivityBinding
    private lateinit var adapter: ItemAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var matchRegex: OptionalRegex


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        g = MainActivityBinding.inflate(layoutInflater)
        val view: View = g.root
        setContentView(view)
        setSupportActionBar(g.toolbar)

        getSharedPreferences(this)?.let {
            sharedPreferences = it
        }

        // Create adapter from sharedPreferences
        val items = loadData(this, sharedPreferences)
        adapter = if (items.isEmpty() == false) {
            itemAdapterFromList(
                this,
                sharedPreferences.getSplitRegex(this),
                items
            )
        } else {
            emptyItemAdapter(this)
        }
        g.toolbarLayout.title = getTitleString()

        // Attach the adapter to the recyclerview to populate items
        g.rvItems.adapter = adapter
        adapter.notifyDataSetChanged()
        // Set layout manager to position the items
        g.rvItems.layoutManager = LinearLayoutManager(this)

        g.fab.setOnClickListener {
            initiateScan()
        }

        matchRegex = sharedPreferences.getMatchRegex(this)
        populateHeader();
    }

    /**
     * Populate the table header from sharedPreferences
     */
    private fun populateHeader() {
        g.headerLayout.removeAllViews()
        getHeaderStrings()?.forEach {
            val spacedTextView = HeaderTextView(this, it)
            g.headerLayout.addView(spacedTextView)
        }
    }

    private fun getHeaderStrings(): List<String>? {
        val headerString =
            sharedPreferences.getString(getString(R.string.sp_column_headers_string), null)
        if (headerString == null || headerString == "") {
            return null
        }
        return adapter.splitRegex.split(headerString)
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
            MaterialAlertDialogBuilder(this, R.style.MaterialAlertDialog_Rounded)

        builder.setMessage(
            "Are you sure you wish to clear all data?"
        )
            .setPositiveButton(
                "Delete"
            ) { _: DialogInterface?, _: Int ->
                adapter.reset()
                clearData(sharedPreferences, getString(R.string.sp_items))
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
        val builder = MaterialAlertDialogBuilder(this, R.style.MaterialAlertDialog_Rounded)
        val manualG: ManualInputBinding = ManualInputBinding.inflate(layoutInflater)

        manualG.edit.requestFocus()

        builder.setTitle("Add Item")
            .setView(manualG.root)
            .setPositiveButton(
                "Ok"
            ) { _: DialogInterface?, _: Int ->
                val output = manualG.edit.text.toString()
                if (matchRegex.passes(output)) {
                    addItem(Item(output))
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
        if (sharedPreferences.getString(
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
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
                val intent = Intent(this, SettingsActivity::class.java)
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
            val builder = MaterialAlertDialogBuilder(this, R.style.MaterialAlertDialog_Rounded)
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
        val integrator = IntentIntegrator(this)
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
        // Delete old files
        filesDir.listFiles()?.forEach {
            it.delete()
        }
        // Make new file
        val file = File(filesDir, sharedPreferences.getExportFileName(this) + ".csv")
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
        val splitRegex = adapter.splitRegex

        // Write file with csvPrinter
        adapter.itemList.forEach {
            csvPrinter.printRecord(splitRegex.split(it.dataString))
        }
        fileWriter.close()

        // Send file
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(
            Intent.EXTRA_STREAM,
            FileProvider.getUriForFile(this, "com.multiQR.FileProvider", file)
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
        saveData(adapter.itemList, sharedPreferences, getString(R.string.sp_items))
        g.toolbarLayout.title = getTitleString()
    }

    /**
     * Show match failure dialog
     * @param output the string that failed to match
     * @param regex the regex
     */
    private fun showMatchFailureDialog(output: String, regex: Regex, onComplete: () -> Unit) {
        val builder =
            MaterialAlertDialogBuilder(this, R.style.MaterialAlertDialog_Rounded)
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
                addItem(Item(output))
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
                    addItem(Item(output))
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
        registerForActivityResult(StartActivityForResult()) {
            // set up regex
            matchRegex = sharedPreferences.getMatchRegex(this)
            populateHeader()
            adapter.splitRegex = adapter.splitRegex
            adapter.notifyDataSetChanged()
        }


    /**
     * @return Whether batch scanning is enabled
     */
    private fun batchScanEnabled(): Boolean {
        return sharedPreferences.getBoolean(getString(R.string.sp_batch_scan), true)
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