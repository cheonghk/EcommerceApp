package com.example.myapplication.Retrofit.Request.Responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Size {

    @SerializedName("L")
    @Expose
    var large: String? = null
    @SerializedName("M")
    @Expose
    var medium: String? = null
    @SerializedName("S")
    @Expose
    var small: String? = null

}
