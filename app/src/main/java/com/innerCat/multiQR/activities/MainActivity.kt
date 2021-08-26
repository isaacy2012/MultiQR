package com.innerCat.multiQR.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import com.innerCat.multiQR.R
import com.innerCat.multiQR.databinding.MainActivityBinding


/**
 * Main Activity Class
 */
class MainActivity : AppCompatActivity() {

    lateinit val

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView<MainActivityBinding>(this, R.layout.main_activity)
    }

}