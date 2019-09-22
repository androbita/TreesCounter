package com.ngopidev.project.treescounter.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.google.zxing.Result
import com.ngopidev.project.treescounter.R
import kotlinx.android.synthetic.main.activity_scan_qr.*
import me.dm7.barcodescanner.zxing.ZXingScannerView

class ScanQRAct : AppCompatActivity(),
        ZXingScannerView.ResultHandler, View.OnClickListener{

    //zxing declaration and captured status
    private lateinit var mScannerView: ZXingScannerView
    private var isGranted = false
    private lateinit var urlAfterScan : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_qr)

        val windows = window
        // clear FLAG_TRANSLUCENT_STATUS flag:
        windows.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        windows.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        // finally change the color
        windows.setStatusBarColor(ContextCompat.getColor(this@ScanQRAct,  R.color.colorPrimaryDark))



        initScannerView()
        initDefaultView()
//        doRequestPermission()
//        mScannerView.startCamera()
        button_reset.setOnClickListener(this)

    }
    override fun onStart() {
        super.onStart()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                mScannerView.startCamera()
            }else{
                doRequestPermission()
            }
        }else{
            initScannerView()
        }
    }

    private fun initDefaultView() {
        text_view_qr_code_value.text = resources.getString(R.string.scan_tree_s_qr)
        button_reset.visibility = View.GONE
    }

    override fun handleResult(rawResult: Result?) {
        text_view_qr_code_value.text = rawResult?.text
        urlAfterScan = rawResult!!.text
        button_reset.visibility = View.VISIBLE
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.button_reset -> {
                if(urlAfterScan.startsWith("http", true)){
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(urlAfterScan)
                    startActivity(intent)
                }else{
                 Toast.makeText(this@ScanQRAct, "qr code not valid", Toast.LENGTH_SHORT).show()
                }
                mScannerView.resumeCameraPreview(this)
                initDefaultView()
            }
            else -> {
                /* nothing to do in here */
            }
        }
    }


    override fun onPause() {
        mScannerView.stopCamera()
        super.onPause()
    }

    private fun initScannerView() {
        mScannerView = ZXingScannerView(this)
        mScannerView.setAutoFocus(true)
        mScannerView.setResultHandler(this)
        frame_layout_camera.addView(mScannerView)
        mScannerView.startCamera()
    }

    //ask for permission
    private fun doRequestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.CAMERA), 100)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            100 -> {
                initScannerView()
            }
            else -> {
                /* nothing to do in here */
            }
        }
    }
}
