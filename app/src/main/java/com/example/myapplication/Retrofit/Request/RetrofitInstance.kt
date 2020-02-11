package com.example.myapplication.Retrofit.Request

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class companion object RetrofitInstance {

   private val ROOT_URL = "https://myproject-891fa.firebaseio.com/"

    private val getRetrofitInstance: Retrofit
        get() = Retrofit.Builder()
            .baseUrl(ROOT_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    val itemInfoApi : ItemInfoApi = getRetrofitInstance.create(
        ItemInfoApi::class.java)

}
