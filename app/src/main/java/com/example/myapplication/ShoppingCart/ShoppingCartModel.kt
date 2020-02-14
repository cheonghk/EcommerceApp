package com.example.myapplication.ShoppingCart

class ShoppingCartModel() {

    var category : Int? = null
    var sub_category:Int? = null
    var totalItems:Int? = null
    var unicode :String? = null

    constructor(category : Int, sub_category:Int ,  totalItems:Int, unicode :String) : this(){
        this.category = category
        this.sub_category = sub_category
        this.totalItems = totalItems
        this.unicode = unicode
    }

}