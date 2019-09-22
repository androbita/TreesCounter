package com.ngopidev.project.treescounter.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.WindowManager
import com.ngopidev.project.treescounter.R
import com.ngopidev.project.treescounter.helpers.PrefsHelper
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.log

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val windows = window
        // clear FLAG_TRANSLUCENT_STATUS flag:
        windows.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        windows.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        // finally change the color
        windows.setStatusBarColor(ContextCompat.getColor(this@MainActivity,  R.color.colorPrimaryDark))

        hello.setOnClickListener {
            startActivity(Intent(this@MainActivity, ShowTrees::class.java))
        }

        logout.setOnClickListener {
            PrefsHelper(this@MainActivity).setLoginStat(false)
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            finish()
        }

        scan_bar.setOnClickListener {
            startActivity(Intent(this@MainActivity, ScanQRAct::class.java))
        }
    }
}
