package com.innerCat.multiQR.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.PurchasesUpdatedListener
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.innerCat.multiQR.Item
import com.innerCat.multiQR.R
import com.innerCat.multiQR.databinding.MainActivityBinding
import com.innerCat.multiQR.dp
import com.innerCat.multiQR.factories.getSharedPreferences
import com.innerCat.multiQR.util.loadData
import com.innerCat.multiQR.util.saveData


enum class State {
    MAIN,
    DETAIL_FRAGMENT
}

/**
 * Main Activity Class
 */
class MainActivity : AppCompatActivity() {

    var actionBarExpanded: Boolean = false
    lateinit var g: MainActivityBinding
    lateinit var items: MutableList<Item>
    lateinit var sharedPreferences: SharedPreferences
    var state: State = State.MAIN

    private val purchasesUpdatedListener =
        PurchasesUpdatedListener { billingResult, purchases ->
            // To be implemented in a later section.
        }

    lateinit var billingClient: BillingClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getSharedPreferences(this)?.let {
            sharedPreferences = it
        }
        items = loadData(this, sharedPreferences)

        g = setContentView(this, R.layout.main_activity)
        g.toolbarLayout.title = getTitleString()

        /**
         * Listener for action bar if expanded
         */
        g.appBarLayout.addOnOffsetChangedListener(OnOffsetChangedListener { _, verticalOffset ->
            actionBarExpanded = verticalOffset == 0
        })

        billingClient = BillingClient.newBuilder(this)
            .setListener(purchasesUpdatedListener)
            .enablePendingPurchases()
            .build()


        setSupportActionBar(g.toolbar)
    }

    /**
     * Delete the item at a particular index
     */
    fun deleteItemAt(index: Int) {
        items.removeAt(index)
    }

    /**
     * Save the current adapter information to the persistent data storage
     */
    fun mutateData(run: () -> Unit) {
        dp{println("BEFORE " + items)}
        run()
        dp{println("AFTER" + items)}
        saveData(items, sharedPreferences, getString(R.string.sp_items))
        g.toolbarLayout.title = getTitleString()
    }

    /**
     * Gets the title string counting how many items there are
     * @return How many items there are as a formatted string
     */
    private fun getTitleString(): String {
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
     * TODO Replace with new way
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        supportFragmentManager.primaryNavigationFragment?.childFragmentManager?.fragments?.let {
            it.forEach { fragment ->
                fragment.onActivityResult(requestCode, resultCode, data)
            }
        }
    }


    /**
     * When the back button is pressed
     */
    override fun onBackPressed() {
        when {
            state == State.DETAIL_FRAGMENT -> {
                g.fab.shrink()
                Handler(Looper.getMainLooper()).postDelayed({
                    g.fab.extend()
                }, resources.getInteger(R.integer.fab_animation_duration).toLong())
                state = State.MAIN
                g.appBarLayout.setExpanded(true)
                super.onBackPressed()
            }
            actionBarExpanded == false -> {
                g.appBarLayout.setExpanded(true)
            }
            else -> {
                super.onBackPressed()
            }
        }
    }

}