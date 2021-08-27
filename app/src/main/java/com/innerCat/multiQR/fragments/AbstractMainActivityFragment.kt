package com.innerCat.multiQR.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.innerCat.multiQR.R
import com.innerCat.multiQR.activities.MainActivity
import com.innerCat.multiQR.databinding.MainActivityBinding


abstract class AbstractMainActivityFragment : Fragment() {
    // Store a pointer to the main Activity Binding
    lateinit var mainG: MainActivityBinding
    // return a pointer to the mainActivity
    val mainActivity: MainActivity
        get() {
            return (requireActivity() as MainActivity)
        }


    // cache pointer if not null
    private var _navigationImageButton: ImageButton? = null
    // return a pointer to the "back" button
    val navigationImageButton: ImageButton?
        get() {
            if (_navigationImageButton == null) {
                // poll if it is still null
                _navigationImageButton = pollForNavigationImageButton()
            }
            // return after poll
            return _navigationImageButton
        }

    // poll for navigationImageButton
    private fun pollForNavigationImageButton(): ImageButton? {
        val size: Int = mainG.toolbar.childCount
        for (i in 0 until size) {
            val child: View = mainG.toolbar.getChildAt(i)
            if (child is ImageButton) {
                if (child.drawable === mainG.toolbar.navigationIcon) {
                    return child
                }
            }
        }
        return null
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

    /**
     * Fades in menu icons
     * @param menu The menu
     * @param icons The R.id.* of the icons
     */
    protected fun fadeInMenuIcons(menu: Menu, vararg icons: Int) {
        Handler(Looper.getMainLooper()).post {
            // Setup animation
            val fadeIn: Animation =
                AnimationUtils.loadAnimation(activity, android.R.anim.fade_in).apply {
                    interpolator = AccelerateInterpolator()
                    duration = resources.getInteger(R.integer.ith_animation_duration).toLong()
                }

            // Animate
            icons.forEach {
                menu.findItem(it).isVisible = true
                mainActivity.findViewById<View>(R.id.action_add_manually).startAnimation(fadeIn)
            }

            navigationImageButton?.visibility = View.VISIBLE
            navigationImageButton?.startAnimation(fadeIn)
        }
    }

}