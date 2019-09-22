package com.ngopidev.project.treescounter.activities

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.google.gson.GsonBuilder
import com.ngopidev.project.treescounter.Model.Trees
import com.ngopidev.project.treescounter.R
import com.ngopidev.project.treescounter.adapters.CvTreeAdapters
import com.ngopidev.project.treescounter.helpers.AllConstHere
import com.ngopidev.project.treescounter.helpers.PrefsHelper
import com.ngopidev.project.treescounter.networking.ApiOnly
import kotlinx.android.synthetic.main.show_trees.*
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
 *   created by Irfan Assidiq on 2019-09-10
 *   email : assidiq.irfan@gmail.com
 **/
class ShowTrees : AppCompatActivity() {

    var rcView : RecyclerView? = null
    var adapters :CvTreeAdapters? = null
    var listofTrees : List<Trees>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.show_trees)

        rcView = findViewById(R.id.rcView)
        rcView!!.setHasFixedSize(true)
        rcView!!.layoutManager = LinearLayoutManager(this@ShowTrees)

        val windows = window
        // clear FLAG_TRANSLUCENT_STATUS flag:
        windows.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        windows.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        // finally change the color
        windows.setStatusBarColor(ContextCompat.getColor(this@ShowTrees,  R.color.colorPrimaryDark))


        progressBar.visibility = View.VISIBLE
        executeDataforList()
        fab_addData.setOnClickListener {
            startActivity(Intent(this@ShowTrees, UploadActivity::class.java))
        }
    }

    fun executeDataforList(){
        val gson  = GsonBuilder().setLenient().create()
        val token = PrefsHelper(this@ShowTrees).getToken()
        val httpClient = OkHttpClient.Builder().addInterceptor(object  : Interceptor{
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

        listofTrees = ArrayList<Trees>()
        adapters = CvTreeAdapters()

        apiexecute.getAllTrees().enqueue(object : Callback<List<Trees>>{
            override fun onFailure(call: Call<List<Trees>>, t: Throwable) {
                Toast.makeText(this@ShowTrees, "${t.message}", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<List<Trees>>, response: Response<List<Trees>>) {
                if(response.isSuccessful){
                    listofTrees = response.body()
                    adapters = CvTreeAdapters(this@ShowTrees, listofTrees!!)
                    rcView!!.adapter = adapters
                    progressBar.visibility= View.GONE
                }else{
                    Toast.makeText(this@ShowTrees, "${response}", Toast.LENGTH_SHORT).show()
                }
            }

        })

    }
}