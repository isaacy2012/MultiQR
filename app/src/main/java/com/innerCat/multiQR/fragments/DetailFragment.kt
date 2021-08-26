package com.innerCat.multiQR.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.innerCat.multiQR.databinding.FragmentDetailBinding
import com.innerCat.multiQR.databinding.FragmentMasterBinding
import com.innerCat.multiQR.factories.getSharedPreferences
import com.innerCat.multiQR.util.loadData

class DetailFragment : Fragment() {

    private lateinit var g: FragmentDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        g = FragmentDetailBinding.inflate(layoutInflater)
        g.tv.setText(requireArguments().getString("strToPrint"))
//        (requireActivity() as AppCompatActivity).setSupportActionBar(g.toolbar)
//        setHasOptionsMenu(true)

//        getSharedPreferences(requireActivity())?.let {
//            sharedPreferences = it
//        }
//
//        // Create adapter from sharedPreferences
//        val items = loadData(requireActivity(), sharedPreferences)
//        adapter = if (items.isEmpty() == false) {
//            itemAdapterFromList(
//                items
//            )
//        } else {
//            emptyItemAdapter()
//        }
//        g.toolbarLayout.title = getTitleString()
//
//        // Attach the adapter to the recyclerview to populate items
//        g.rvItems.adapter = adapter
//        // Set layout manager to position the items
//        g.rvItems.layoutManager = LinearLayoutManager(requireActivity())
//
//        g.fab.setOnClickListener {
//            initiateScan()
//        }
//
//        refresh()

        return g.root
    }
}