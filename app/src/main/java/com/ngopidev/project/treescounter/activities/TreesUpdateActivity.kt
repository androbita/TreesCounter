package com.ngopidev.project.treescounter.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log.e
import android.view.View
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import com.justinnguyenme.base64image.Base64Image
import com.ngopidev.project.treescounter.R
import com.ngopidev.project.treescounter.helpers.AllConstHere
import kotlinx.android.synthetic.main.activity_detail_trees.*
import kotlinx.android.synthetic.main.input_data_trees.*


/**
 *   created by Irfan Assidiq on 2019-08-28
 *   email : assidiq.irfan@gmail.com
 **/
class TreesUpdateActivity : AppCompatActivity() {

    lateinit var list : ArrayList<String>
    val REQ_IMAGE = 1001
    lateinit var treeOwner : String
    lateinit var treeNumber : String
    lateinit var treeType : String
    lateinit var stringQrResult : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.input_data_trees)

        val windows = window
        // clear FLAG_TRANSLUCENT_STATUS flag:
        windows.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        windows.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        // finally change the color
        windows.setStatusBarColor(ContextCompat.getColor(this@TreesUpdateActivity,  R.color.colorPrimaryDark))

        stringQrResult = "not generated yet"
        val data = resources.getStringArray(R.array.list_data)
        list = ArrayList()
        list.addAll(data)
        val adapters : ArrayAdapter<String> = ArrayAdapter<String>(this@TreesUpdateActivity, R.layout.for_spinner, list)
        spintreeInjection.adapter = adapters
        btn_submit.setOnClickListener {
            Toast.makeText(this@TreesUpdateActivity, "on progress feature", Toast.LENGTH_SHORT).show()
            treeOwner = et_treeOwner.text.toString()
            treeNumber = et_treeNumbers.text.toString()
            treeType = et_treeNames.text.toString()

            executeAllData(treeOwner, treeNumber, treeType)
        }

        tv_addimage.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.setType("image/*")
            startActivityForResult(intent, REQ_IMAGE)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQ_IMAGE){
            if(resultCode == Activity.RESULT_OK){
                if(data != null){
                    val uri = data.data
                    val locImage = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                    img_addimage.visibility = View.VISIBLE
                    Glide.with(this@TreesUpdateActivity)
                        .load(locImage)
                        .error(R.mipmap.ic_launcher)
                        .into(img_addimage)
                    Base64Image.instance.encode(locImage){
                        e("ERROR_REPORT", "${it}")
                    }
                    e("ERROR_REPORT", "${locImage}")
                }
            }
        }

    }

    fun executeAllData(treeOwner : String, treeNumber : String, treeType : String){

        if(treeOwner.isNotEmpty() && treeNumber.isNotEmpty() && treeType.isNotEmpty()){
                val params = AllConstHere.baseUrlPDF+"${treeOwner}_${treeNumber}_${treeType}.pdf"
                val writer = QRCodeWriter()
                val bitMatrix = writer.encode(params, BarcodeFormat.QR_CODE, 512, 512)
                val width = bitMatrix.width
                val height = bitMatrix.height
                val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
                for (x in 0 until width) {
                    for (y in 0 until height) {
                        bitmap.setPixel(x, y, if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE)
                    }
                }
                //execute all data here
                Base64Image.instance.encode(bitmap){
                    stringQrResult = "data:image/jpeg;base64,${it!!}"
                    e("HAIERROR", stringQrResult)
                }

        }
    }


}