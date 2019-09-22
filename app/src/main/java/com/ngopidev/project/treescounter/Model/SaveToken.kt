package com.ngopidev.project.treescounter.Model

import com.google.gson.annotations.SerializedName


/**
 *   created by Irfan Assidiq on 2019-09-10
 *   email : assidiq.irfan@gmail.com
 **/
class SaveToken {
    @SerializedName("token")
    lateinit var token : String

    @SerializedName("expired")
    lateinit var expired : String

    @SerializedName("refresh")
    lateinit var refresh : String

}