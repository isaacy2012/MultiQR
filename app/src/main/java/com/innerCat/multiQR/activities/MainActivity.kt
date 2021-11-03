package com.innerCat.multiQR.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.innerCat.multiQR.R
import com.innerCat.multiQR.databinding.MainActivityBinding
import com.innerCat.multiQR.util.getShouldShowOnboarding
import com.innerCat.multiQR.viewmodels.MainViewModel
import com.innerCat.multiQR.viewmodels.MainViewModelFactory


enum class State {
    MAIN,
    DETAIL_FRAGMENT
}

/**
 * Main Activity Class
 */
class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainViewModel

    var actionBarExpanded: Boolean = false
    lateinit var g: MainActivityBinding
    var state: State = State.MAIN

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        g = setContentView(this, R.layout.main_activity)

        val factory = MainViewModelFactory(application)
        viewModel = ViewModelProvider(this, factory).get(MainViewModel::class.java)

        g.toolbarLayout.title = viewModel.getTitleString()

        /**
         * Listener for action bar if expanded
         */
        g.appBarLayout.addOnOffsetChangedListener(OnOffsetChangedListener { _, verticalOffset ->
            actionBarExpanded = verticalOffset == 0
        })


//        sharedPreferences.edit {
//            putBoolean(getString(R.string.sp_should_show_onboarding), true)
//        }
        if (viewModel.sharedPreferences.getShouldShowOnboarding(this)) {
            showOnboarding()
        }

        setSupportActionBar(g.toolbar)
    }

    private fun showOnboarding() {
        val intent = Intent(this, OnboardingActivity::class.java)
        startActivity(intent)

        /*
        // Use the Builder class for convenient dialog construction
        val limit = resources.getInteger(R.integer.max_items_limit)
        val builder = MaterialAlertDialogBuilder(
            this,
            R.style.MaterialAlertDialog_Rounded
        )
        builder.setTitle("Welcome")
            .setMessage(
                Html.fromHtml(
                    "" +
                            "Thank you for downloading <b>MultiQR</b>." +
                            "<br><br>" +
                            "This is a trial version of the app, and <b>you can only add up to $limit items</b>. " +
                            "<br><br>" +
                            "An update will be coming soon with an in-app purchase to enable adding unlimited items." +
                            "<br><br>" +
                            "Thanks for your understanding!" +
                            "", Html.FROM_HTML_MODE_COMPACT
                )
            )
            .setPositiveButton("I Understand") { _: DialogInterface?, _: Int ->
                sharedPreferences.edit {
                    putBoolean(
                        getString(R.string.sp_should_show_onboarding),
                        false
                    )
                }
            }
        val dialog = builder.create()
        dialog.show()

         */
    }

    /**
     * Save the current adapter information to the persistent data storage
     */
    fun mutateData(run: () -> Unit) {
        viewModel.mutateData(run);
        g.toolbarLayout.title = viewModel.getTitleString()
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