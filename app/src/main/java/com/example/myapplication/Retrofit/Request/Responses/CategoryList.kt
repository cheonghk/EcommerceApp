package com.example.myapplication.Retrofit.Request.Responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.ArrayList

class CategoryList {
    @SerializedName("new_arrivals")
    @Expose
    var new_arrivals: ArrayList<ItemInfo>? = null

    @SerializedName("accessories")
    @Expose
    var accessories: ArrayList<ItemInfo>? = null

    @SerializedName("clothes")
    @Expose
    var clothes: ArrayList<ItemInfo>? = null

    @SerializedName("shoes")
    @Expose
    var shoes: ArrayList<ItemInfo>? = null



}