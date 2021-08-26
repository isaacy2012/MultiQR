package com.innerCat.multiQR.activities

import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.innerCat.multiQR.Item
import com.innerCat.multiQR.R
import com.innerCat.multiQR.databinding.MainActivityBinding
import com.innerCat.multiQR.factories.getSharedPreferences
import com.innerCat.multiQR.util.loadData


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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getSharedPreferences(this)?.let {
            sharedPreferences = it
        }
        items = loadData(this, sharedPreferences)
        g = setContentView(this, R.layout.main_activity)

        /**
         * Listener for action bar if expanded
         */
        g.appBarLayout.addOnOffsetChangedListener(OnOffsetChangedListener { _, verticalOffset ->
            actionBarExpanded = verticalOffset == 0
        })

        setSupportActionBar(g.toolbar)
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