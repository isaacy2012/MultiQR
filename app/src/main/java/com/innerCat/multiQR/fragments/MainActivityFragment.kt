package com.innerCat.multiQR.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.innerCat.multiQR.activities.MainActivity
import com.innerCat.multiQR.databinding.MainActivityBinding


open class MainActivityFragment : Fragment() {
    lateinit var mainG: MainActivityBinding
    val mainActivity: MainActivity
        get() {
            return (requireActivity() as MainActivity)
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        mainG = (requireActivity() as MainActivity).g
        return view
    }


}