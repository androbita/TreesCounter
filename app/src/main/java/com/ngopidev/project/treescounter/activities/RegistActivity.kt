package com.ngopidev.project.treescounter.activities

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import android.widget.Toast
import com.google.gson.GsonBuilder
import com.ngopidev.project.treescounter.Model.Users
import com.ngopidev.project.treescounter.helpers.AllConstHere
import com.ngopidev.project.treescounter.networking.ApiOnly
import kotlinx.android.synthetic.main.registration_act.*
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.ngopidev.project.treescounter.helpers.PrefsHelper


/**
 *   created by Irfan Assidiq on 2019-09-09
 *   email : assidiq.irfan@gmail.com
 **/
class RegistActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.ngopidev.project.treescounter.R.layout.registration_act)

        val windows = window
        // clear FLAG_TRANSLUCENT_STATUS flag:
        windows.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        windows.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        // finally change the color
        windows.setStatusBarColor(ContextCompat.getColor(this@RegistActivity,  com.ngopidev.project.treescounter.R.color.colorPrimaryDark))



        btn_regis.setOnClickListener{
            val name = et_name.text.toString()
            val username = et_uname.text.toString()
            val password = et_pass.text.toString()
            val cekpass = et_passConfirm.text.toString()
            val email = et_email.text.toString()
            yesLogin(username, email, password, cekpass, name)
        }


    }

    fun yesLogin(usename : String, email: String, password : String, cekPassword : String, name : String){
        val gson  = GsonBuilder().setLenient().create()
        val retro = Retrofit.Builder()
            .baseUrl(AllConstHere.baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        val apiexecute = retro.create(ApiOnly::class.java)

        if(!usename.isNullOrEmpty() && !email.isNullOrEmpty() && !password.isNullOrEmpty()
            && !cekPassword.isNullOrEmpty() && !name.isNullOrEmpty()){
            if(password.equals(cekPassword)){
                val regisUser = apiexecute.register(usename, password, name, email)
                regisUser.enqueue(object : Callback<Users>{
                    override fun onFailure(call: Call<Users>, t: Throwable) {
                        Toast.makeText(this@RegistActivity, "Register Failed Please Check Your Internet Connection"
                            , Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<Users>, response: Response<Users>) {
                        Toast.makeText(this@RegistActivity, "Register Success"
                            , Toast.LENGTH_SHORT).show()
                        onBackPressed()
                    }

                })
            }else{
                Toast.makeText(this@RegistActivity, "password not match"
                    , Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(this@RegistActivity, "please fill all empty coloumn"
            , Toast.LENGTH_SHORT).show()
        }
    }
}