package com.innerCat.multiQR.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.innerCat.multiQR.R
import com.innerCat.multiQR.activities.State
import com.innerCat.multiQR.databinding.FragmentDetailBinding
import com.innerCat.multiQR.factories.cellAdapterFromList
import com.innerCat.multiQR.factories.emptyCellAdapter
import com.innerCat.multiQR.strAdapter.CellAdapter
import java.util.*

class DetailFragment : MainActivityFragment() {

    private lateinit var g: FragmentDetailBinding
    private lateinit var adapter: CellAdapter

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
            adapter.addString("hi")
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
                    mainActivity.items[requireArguments().getInt("index")].strList
                )
            } else {
                emptyCellAdapter()
            }

        // Attach the adapter to the recyclerview to populate items
        g.rvCells.adapter = adapter
        // Set layout manager to position the items
        g.rvCells.layoutManager = LinearLayoutManager(requireActivity())
    }

}