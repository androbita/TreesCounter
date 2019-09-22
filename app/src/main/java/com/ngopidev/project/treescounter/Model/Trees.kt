package com.ngopidev.project.treescounter.Model

import com.google.gson.annotations.SerializedName


/**
 *   created by Irfan Assidiq on 2019-08-28
 *   email : assidiq.irfan@gmail.com
 **/
class Trees {
    @SerializedName("id")
    var id : Int? = null

    @SerializedName("tree_number")
    lateinit var treeNumber : String

    @SerializedName("tree_type")
    lateinit var treeType : String

    @SerializedName("tree_height")
    var treeHeight : Double? = null

    @SerializedName("tree_diameter")
    var treeDiameter : Double? = null

    @SerializedName("tree_location")
    lateinit var treeLocation : String

    @SerializedName("tree_image")
    lateinit var treeImage : String

    @SerializedName("tree_injection")
    lateinit var treeInjection : String

    @SerializedName("injection_executor")
    lateinit var injectionExecutor : String

    @SerializedName("tree_owner")
    lateinit var treeOwner :String

    @SerializedName("url")
    lateinit var url : String

    @SerializedName("url_qr")
    lateinit var urlQr : String

    constructor(){}
    //using for set tree
    constructor(treeNumber : String, treeType : String, treeHeight : Double, treeDiameter : Double,
            treeLocation : String, treeImage : String, treeInjection : String,
            injectionExecutor : String, treeOwner : String, url : String, urlQr : String){
            this.treeNumber = treeNumber
            this.treeType = treeType
            this.treeHeight = treeHeight
            this.treeDiameter = treeDiameter
            this.treeLocation = treeLocation
            this.treeImage = treeImage
            this.treeInjection = treeInjection
            this.injectionExecutor = injectionExecutor
            this.treeOwner = treeOwner
            this.url = url
            this.urlQr = urlQr
            }

    //using for update tree
    constructor(id: Int, treeNumber : String, treeType : String, treeHeight : Double, treeDiameter : Double,
            treeLocation : String, treeImage : String, treeInjection : String,
            injectionExecutor : String, treeOwner : String, url : String, urlQr: String){
            this.id = id
            this.treeNumber = treeNumber
            this.treeType = treeType
            this.treeHeight = treeHeight
            this.treeDiameter = treeDiameter
            this.treeLocation = treeLocation
            this.treeImage = treeImage
            this.treeInjection = treeInjection
            this.injectionExecutor = injectionExecutor
            this.treeOwner = treeOwner
            this.url = url
            this.urlQr = urlQr
            }
}