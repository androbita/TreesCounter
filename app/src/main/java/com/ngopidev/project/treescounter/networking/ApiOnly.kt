package com.ngopidev.project.treescounter.networking

import com.ngopidev.project.treescounter.Model.SaveToken
import com.ngopidev.project.treescounter.Model.Trees
import com.ngopidev.project.treescounter.Model.Users
import com.ngopidev.project.treescounter.activities.UploadActivity
import com.ngopidev.project.treescounter.helpers.AllConstHere
import retrofit2.Call
import retrofit2.http.*


/**
 *   created by Irfan Assidiq on 2019-08-28
 *   email : assidiq.irfan@gmail.com
 **/
interface ApiOnly {
    @GET(AllConstHere.showtreeData)
    fun getAllTrees() : Call<List<Trees>>

    @FormUrlEncoded
    @POST(AllConstHere.showtreeData)
    fun getDetailTree(@Field("id") id : Int) : Call<List<Trees>>

    @FormUrlEncoded
    @POST(AllConstHere.register)
    fun register(@Field("username") username : String,
                 @Field("password") password : String,
                 @Field("name") name : String,
                 @Field("email") email : String) : Call<Users>

    @FormUrlEncoded
    @POST(AllConstHere.login)
    fun goLogin(@Field("username") username : String,
                @Field("password") password : String) : Call<SaveToken>

    //sampel singkat parameter dengan object
    @FormUrlEncoded
    @POST(AllConstHere.addTreeData)
    fun uploadData(@Field("tree_number") treeNumber : String,
                   @Field("tree_type") treeType : String,
                   @Field("tree_height") treeHeight : String,
                   @Field("tree_diameter") treeDiameter : String,
                   @Field("tree_location") treeLocation : String,
                   @Field("tree_image") treeImage : String,
                   @Field("tree_injection") treeInjection : String,
                   @Field("injection_executor") injectionExecutor : String,
                   @Field("tree_owner") treeOwner : String,
                   @Field("url_qr") urlQr : String) : Call<List<UploadActivity>>
}