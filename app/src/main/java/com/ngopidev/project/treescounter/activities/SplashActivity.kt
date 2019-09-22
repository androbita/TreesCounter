package com.ngopidev.project.treescounter.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import com.ngopidev.project.treescounter.R
import com.ngopidev.project.treescounter.helpers.PrefsHelper

/**
 *   created by Irfan Assidiq on 2019-08-27
 *   email : assidiq.irfan@gmail.com
 **/
class SplashActivity : AppCompatActivity() {

    private var handler : Handler? = null
    private val counterSplash = 5000L;

    internal val runnable = Runnable {
        if (!isFinishing) {
            val loginStat = PrefsHelper(this).getLoginStat()
            if(!loginStat){
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                finish()
            }else{
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                finish()
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_act)

        val windows = window
        // clear FLAG_TRANSLUCENT_STATUS flag:
        windows.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        windows.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        // finally change the color
        windows.setStatusBarColor(ContextCompat.getColor(this@SplashActivity,  R.color.greenseed))

        handler = Handler()
        handler!!.postDelayed(runnable, counterSplash)
    }

    override fun onDestroy() {
        if (handler != null){
            handler!!.removeCallbacks(runnable)
        }
        super.onDestroy()
    }
}


