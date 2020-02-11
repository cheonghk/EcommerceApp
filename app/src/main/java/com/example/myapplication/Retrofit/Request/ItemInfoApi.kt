package com.example.myapplication.Retrofit.Request

import com.example.myapplication.Retrofit.Request.Responses.CategoryList
import retrofit2.Call
import retrofit2.http.GET

interface ItemInfoApi {

    @get:GET("/product_info/")
    val iteminfo : Call<CategoryList>

}
