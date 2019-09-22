package com.ngopidev.project.treescounter.Model

import com.google.gson.annotations.SerializedName


/**
 *   created by Irfan Assidiq on 2019-09-10
 *   email : assidiq.irfan@gmail.com
 **/
class Users {
    @SerializedName("username")
    lateinit var username : String

    @SerializedName("password")
    lateinit var password : String

    @SerializedName("name")
    lateinit var name : String

    @SerializedName("email")
    lateinit var email : String
}