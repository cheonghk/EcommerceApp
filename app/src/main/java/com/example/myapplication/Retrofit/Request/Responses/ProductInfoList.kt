package com.example.myapplication.Retrofit.Request.Responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.ArrayList

class ProductInfoList {

    @SerializedName("products_info")
    @Expose
    var products_info: CategoryList? = null
}