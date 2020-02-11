package com.example.myapplication.FireBase

import com.example.myapplication.Category.CategoryContoller
import kotlinx.android.synthetic.main.recyclerview_category.*

object ReadDataByCategory {

    private val mFireBaseCollector = FireBaseCollector()
    fun initialize(category:String, itemList : MutableList<ItemInfo_Firebase_Model>) : MutableList<ItemInfo_Firebase_Model>{
        mFireBaseCollector.readData_CategoryContoller(object : FireBaseCollector.DataStatus {
            override fun DataIsLoaded(theItemListModel: MutableList<ItemInfo_Firebase_Model>) {
                itemList.addAll(theItemListModel)
            }
        }, category)
        return itemList
    }
}