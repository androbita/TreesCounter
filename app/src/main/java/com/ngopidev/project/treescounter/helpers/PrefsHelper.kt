package com.ngopidev.project.treescounter.helpers

import android.content.Context
import android.content.SharedPreferences


/**
 *   created by Irfan Assidiq on 2019-08-28
 *   email : assidiq.irfan@gmail.com
 **/
class PrefsHelper {
    private val appsName = "TREES_COLLECTOR"
    private val loginStatus = "LOGINSTAT"
    private val saveToken = "TOKENS"

    var mContext : Context
    private var sharedSet : SharedPreferences

    constructor(mContext: Context){
        this.mContext = mContext
        sharedSet =
                mContext.getSharedPreferences(appsName, Context.MODE_PRIVATE)
    }

    //using for set Login Status
    fun setLoginStat(bool : Boolean){
        val editor = sharedSet.edit()
        editor.putBoolean(loginStatus, bool)
        editor.apply()
    }

    //using for get Login Status
    fun getLoginStat(): Boolean{
        return sharedSet.getBoolean(loginStatus, false)
    }

    //using for set token
    fun setToken(token : String){
        val editor = sharedSet.edit()
        editor.putString(saveToken, token)
        editor.apply()
    }

    //using for get token
    fun getToken() : String?{
        return sharedSet.getString(saveToken, "emptytoken")
    }
}