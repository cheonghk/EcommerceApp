package com.example.myapplication.FireBase.Utils

import com.example.myapplication.FireBase.FireBaseCollector
import com.example.myapplication.FireBase.ItemInfo_Firebase_Model

object ReadDataByCategory {

    private val mFireBaseCollector =
        FireBaseCollector()
    fun initialize(category:String, itemList : MutableList<ItemInfo_Firebase_Model>) : MutableList<ItemInfo_Firebase_Model>{
        mFireBaseCollector.readData_CategoryContoller(object :
            FireBaseCollector.DataStatus {
            override fun DataIsLoaded(theItemListModel: MutableList<ItemInfo_Firebase_Model>) {
                itemList.addAll(theItemListModel)
            }
        }, category)
        return itemList
    }
}