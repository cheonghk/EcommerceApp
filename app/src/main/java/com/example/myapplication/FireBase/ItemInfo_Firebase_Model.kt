package com.example.myapplication.FireBase

data class ItemInfo_Firebase_Model(
    var unicode : String? = null,
    var name: String? = null,
    var price: Long? = null,
    var size: MutableList<String>,
    var url: MutableList<String>,
    var url_forRecyclerview: String? = null
) {


}