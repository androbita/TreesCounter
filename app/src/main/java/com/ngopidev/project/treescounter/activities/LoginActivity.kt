package com.ngopidev.project.treescounter.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.ngopidev.project.treescounter.R
import kotlinx.android.synthetic.main.login_act.*
import android.support.v4.content.ContextCompat
import android.util.Log.e
import android.view.WindowManager
import com.google.gson.GsonBuilder
import com.ngopidev.project.treescounter.Model.SaveToken
import com.ngopidev.project.treescounter.Model.Users
import com.ngopidev.project.treescounter.helpers.AllConstHere
import com.ngopidev.project.treescounter.helpers.PrefsHelper
import com.ngopidev.project.treescounter.networking.ApiOnly
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
 *   created by Irfan Assidiq on 2019-08-28
 *   email : assidiq.irfan@gmail.com
 **/
class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.ngopidev.project.treescounter.R.layout.login_act)


        val windows = window
        // clear FLAG_TRANSLUCENT_STATUS flag:
        windows.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        windows.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        // finally change the color
        windows.setStatusBarColor(ContextCompat.getColor(this@LoginActivity,  R.color.colorPrimaryDark))


        btn_login.setOnClickListener {
            val email = et_email.text.toString()
            val pass = et_pass.text.toString()
            loginAct(email, pass)
        }

        tv_register.setOnClickListener {
//            Toast.makeText(this@LoginActivity, "Using For Registers", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@LoginActivity, RegistActivity::class.java))
        }

    }

    fun loginAct(email : String, pass: String){
        if(email.isEmpty() || pass.isEmpty()){
            Toast.makeText(this@LoginActivity, "Username or Password cannot be blank", Toast.LENGTH_SHORT).show()
        }else{

            val gson = GsonBuilder().setLenient().create()
            val retro = Retrofit.Builder().baseUrl(AllConstHere.baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

            val apiexecute = retro.create(ApiOnly::class.java)

            val loginUsers = apiexecute.goLogin(email, pass)
            loginUsers.enqueue(object : Callback<SaveToken>{
                override fun onFailure(call: Call<SaveToken>, t: Throwable) {
                    Toast.makeText(this@LoginActivity, "Login Failed", Toast.LENGTH_SHORT).show()
                    e("TAG_ERROR", "error is :\n ${t.message}")
                }

                override fun onResponse(call: Call<SaveToken>, response: Response<SaveToken>) {
                    if(response.isSuccessful){
                        val takeOnlyToken : SaveToken = response.body()!!
                        Toast.makeText(this@LoginActivity, "Welcome ${email}", Toast.LENGTH_SHORT).show()
                        PrefsHelper(this@LoginActivity).setLoginStat(true)
                        PrefsHelper(this@LoginActivity).setToken(takeOnlyToken.token)
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    }else{
                        Toast.makeText(this@LoginActivity, "Login Failed, ${response.message()} User", Toast.LENGTH_SHORT).show()
                        e("TAG_ERROR", "hi this is error : ${response.message()}")
                    }

                }

            })
        }
    }
}