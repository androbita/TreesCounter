package com.ngopidev.project.treescounter.activities

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log.e
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import com.ngopidev.project.treescounter.Model.Trees
import com.ngopidev.project.treescounter.R
import com.ngopidev.project.treescounter.helpers.AllConstHere
import com.ngopidev.project.treescounter.helpers.PrefsHelper
import com.ngopidev.project.treescounter.networking.ApiOnly
import kotlinx.android.synthetic.main.activity_detail_trees.*
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DetailTrees : AppCompatActivity() {

    var dataFrom : Int? = null
    var urlQr : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_trees)

        dataFrom  = intent.getIntExtra("forDetail", 0)
        urlQr = intent.getStringExtra("urlQr")
        imgForQR.visibility = View.GONE
        executeDataForDetail()
        btnSeePdf.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(urlQr)
            startActivity(intent)

        }
        backOk.setOnClickListener {
            onBackPressed()
        }
    }

    fun executeDataForDetail(){
        val gson = GsonBuilder().setLenient().create()
        val token = PrefsHelper(this@DetailTrees).getToken()
        val httpClient = OkHttpClient.Builder().addInterceptor(object  : Interceptor {
            override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
                val request = chain.request()
                    .newBuilder()
                    .addHeader("Authorization", "Bearer ${token}" )
                    .build()
                return chain.proceed(request)
            }

        }).build()

        val retro = Retrofit.Builder()
            .baseUrl(AllConstHere.baseUrl)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        val apiexecute = retro.create(ApiOnly::class.java)
        apiexecute.getDetailTree(dataFrom!!).enqueue(object : Callback<List<Trees>>{
            override fun onFailure(call: Call<List<Trees>>, t: Throwable) {
                e("TAG_ERROR", t.message + call)
            }

            override fun onResponse(call: Call<List<Trees>>, response: Response<List<Trees>>) {
                if(response.isSuccessful){
                    val treeData = response.body()!!
                    val diameter = Math.round((treeData[0].treeDiameter!!)* 100)/100
                    val height = Math.round((treeData[0].treeHeight!!)* 100)/100
                    Glide.with(this@DetailTrees)
                        .load(treeData[0].treeImage)
                        .error(R.drawable.ic_leaf)
                        .into(img_detailImage)
                    tvTreesName.text = treeData[0].treeType
                    tvTreesNumbers.text = "Tree\'s number :\n"+treeData[0].treeNumber
                    tvTreesDiameter.text = "Tree\'s diameter :\n"+diameter.toString()
                    tvTreesHigh.text = "Tree\'s Height :\n"+height.toString()
                    tvLocation.text = "Tree\'s location :\n"+treeData[0].treeLocation
                    val statusInject : String
                    if(treeData[0].treeInjection.equals("0")){
                        statusInject = "Not Injection Yet"
                    }else{
                        statusInject = "Already Injection"
                    }
                    tvisInjection.text = "Tree\'s Injection Status :\n"+statusInject
                    tvInjector.text = "Tree\'s injector :\n"+treeData[0].injectionExecutor
                    tvOwner.text = "Tree\'s owner :\n"+treeData[0].treeOwner
                    if(treeData[0].url.isNotEmpty()){
                        generateQR(treeData[0].url)
                    }
                }else{
                    Toast.makeText(this@DetailTrees, "data is empty", Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

    fun generateQR(params : String){
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
        imgForQR.visibility = View.VISIBLE
        imgForQR.setImageBitmap(bitmap)
    }
}
