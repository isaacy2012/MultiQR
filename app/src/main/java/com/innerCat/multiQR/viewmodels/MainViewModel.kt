package com.innerCat.multiQR.viewmodels

import android.app.Application
import android.content.Intent
import android.content.SharedPreferences
import androidx.core.content.FileProvider
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.innerCat.multiQR.Item
import com.innerCat.multiQR.R
import com.innerCat.multiQR.dp
import com.innerCat.multiQR.factories.getSharedPreferences
import com.innerCat.multiQR.util.*
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import java.io.File
import java.io.FileWriter

/**
 * Main view model
 *
 * @constructor
 *
 * @param application
 */
class MainViewModel(application: Application) : AndroidViewModel(application) {
    val sharedPreferences: SharedPreferences
    var items: MutableList<Item> = ArrayList()
    private val liveItems: MutableLiveData<out MutableList<Item>> = MutableLiveData(items)
    val rvVisibility: MutableLiveData<Boolean> = MutableLiveData(items.size > 0).apply {
        liveItems.observeForever{value = it.size > 0}
    }
    private lateinit var matchRegex: OptionalRegex
    lateinit var splitRegex: OptionalRegex
    val app: Application get() { return getApplication<Application>() }

    init {
        getSharedPreferences(app)!!.apply {
            sharedPreferences = this
            items = loadData(app, sharedPreferences)
        }
    }


    /**
     * Refresh regex
     */
    fun refreshRegex() {
        matchRegex = sharedPreferences.getMatchRegex(app)
        splitRegex = sharedPreferences.getSplitRegex(app)
    }

    /**
     * Delete the item at a particular index
     *
     * @param index
     */
    fun deleteItemAt(index: Int) {
        mutateData { items.removeAt(index) }
    }

    /**
     * Save the current adapter information to the persistent data storage
     */
    fun mutateData(run: () -> Unit) {
        dp { println("BEFORE " + items) }
        run()
        dp { println("AFTER" + items) }
        saveData(
            items,
            sharedPreferences,
            app.getString(R.string.sp_items)
        )
        liveItems.value = items
    }

    /**
     * Manual add
     *
     * @param input the input string
     * @param onSuccess when the match was successful
     * @param onFailure when the match was successful
     */
    fun add(input: String, onSuccess: (Item) -> Unit, onFailure: (Regex) -> Unit) {
        if (matchRegex.passes(input)) {
            onSuccess(Item(input, splitRegex))
        } else {
            onFailure((matchRegex as EnabledRegex).regex)
        }
    }

    /**
     * Gets the title string counting how many items there are
     * @return How many items there are as a formatted string
     */
    fun getTitleString(): String {
        return when (val count = items.size) {
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
     * Get header strings
     *
     * @return
     */
    fun getHeaderStrings(): List<String>? {
        val headerString =
            sharedPreferences.getString(
                app.getString(R.string.sp_column_headers_string),
                null
            )
        if (headerString == null || headerString == "") {
            return null
        }
        return splitRegex.split(headerString)
    }

    /**
     * Export to csv intent
     *
     * @return
     */
    fun exportToCsvIntent(): Intent {
        val filesDir = app.filesDir
        // Delete old files
        filesDir.listFiles()?.forEach {
            it.delete()
        }
        // Make new file
        val file = File(
            filesDir,
            sharedPreferences.getExportFileName(app) + ".csv"
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
        items.forEach {
            csvPrinter.printRecord(it.strList)
        }
        fileWriter.close()

        // Send file
        return Intent(Intent.ACTION_SEND).apply {
            this.putExtra(
                Intent.EXTRA_STREAM,
                FileProvider.getUriForFile(app, "com.multiQR.FileProvider", file)
            )
            this.type = "text/csv";
            this.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
    }

}

