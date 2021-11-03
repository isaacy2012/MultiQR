package com.innerCat.multiQR.viewmodels

import android.app.Application
import android.content.Intent
import android.content.SharedPreferences
import androidx.core.content.FileProvider
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
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
 * Observe immediately
 *
 * @param T type parameter
 * @param lifecycleOwner the lifecycleOwner
 * @param callback the callback to call immediately
 * @receiver
 */
fun <T> LiveData<T>.observeImmediately(lifecycleOwner: LifecycleOwner, callback: (T) -> Unit) {
    this.observe(lifecycleOwner, callback)
    this.value?.let { callback(it) }
}

/**
 * Main view model
 *
 * @constructor
 *
 * @param application
 */
class MainViewModel(application: Application) : AndroidViewModel(application) {
    /**
     * Shared preferences
     */
    val sharedPreferences: SharedPreferences

    /**
     * Items
     */
    private var _items: MutableList<Item>

    /**
     * Live items
     */
    val items: MutableLiveData<out MutableList<Item>> = MutableLiveData()

    init {
        sharedPreferences = getSharedPreferences(app)!!.apply {
            _items = loadData(app, this)
            items.value = _items
        }
    }

    /**
     * Set items
     *
     * @param newItems
     */
    fun setItems(newItems: MutableList<Item>) {
        this._items = newItems
        items.value = this._items
    }

    /**
     * Rv visibility
     */
    val rvVisibility: LiveData<Boolean> = MutableLiveData(_items.size > 0).apply {
        items.observeForever{value = it.size > 0}
   }

    /**
     * Title string
     */
    val titleString: LiveData<String> = MutableLiveData(getTitleString(_items.size)).apply {
        items.observeForever{value = getTitleString(it.size)}
    }

    /**
     * Match regex
     */
    private lateinit var matchRegex: OptionalRegex

    /**
     * Split regex
     */
    lateinit var splitRegex: OptionalRegex

    /**
     * App
     */
    val app: Application get() { return getApplication<Application>() }


    /**
     * Refresh regex
     */
    fun refreshRegex() {
        matchRegex = sharedPreferences.getMatchRegex(app)
        splitRegex = sharedPreferences.getSplitRegex(app)
    }


    /**
     * Save the current adapter information to the persistent data storage
     */
    fun mutateData(run: () -> Unit) {
        dp { println("BEFORE $_items") }
        run()
        dp { println("AFTER$_items") }
        saveData(
            _items,
            sharedPreferences,
            app.getString(R.string.sp_items)
        )
        items.value = _items
    }

    /**
     * Delete the item at a particular index
     *
     * @param index
     */
    fun deleteItemAt(index: Int) {
        mutateData { _items.removeAt(index) }
    }

    /**
     * Clear items
     *
     */
    fun clearItems() {
        mutateData { _items.clear() }
    }

    /**
     * Add item
     *
     * @param index
     * @param item
     */
    fun addItem(index: Int, item: Item) {
        mutateData { _items.add(index, item) }
    }

    /**
     * Remove item
     *
     * @param item
     */
    fun removeItem(item: Item) {
        mutateData { _items.remove(item) }
    }

    /**
     * Make item
     *
     * @param input the input string
     * @param onSuccess when the match was successful
     * @param onFailure when the match was successful
     */
    fun makeItem(input: String, onSuccess: (Item) -> Unit, onFailure: (Regex) -> Unit) {
        if (matchRegex.passes(input)) {
            onSuccess(Item(input, splitRegex))
        } else {
            onFailure((matchRegex as EnabledRegex).regex)
        }
    }

    /**
     * Gets the title string counting how many _items there are
     * @return How many _items there are as a formatted string
     */
    private fun getTitleString(count: Int): String {
        return when (count) {
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
        _items.forEach {
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

