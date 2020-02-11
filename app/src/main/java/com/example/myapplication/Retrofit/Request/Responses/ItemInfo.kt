package com.example.myapplication.Retrofit.Request.Responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ItemInfo{

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("price")
    @Expose
     var price: Long? = null

    @SerializedName("size")
    @Expose
    var size: Size? = null

    @SerializedName("url")
    @Expose
    var url: ArrayList<String>? = null

}




