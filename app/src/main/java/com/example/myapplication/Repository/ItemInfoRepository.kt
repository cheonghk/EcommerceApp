/*package com.example.myapplication.Repository

import androidx.annotation.Nullable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.myapplication.Retrofit.Request.ItemInfoApiClient
import com.example.myapplication.Retrofit.Request.Responses.ItemInfo

class ItemInfoRepository {


    private var mItemInfoApiClient: ItemInfoApiClient? = null
    private var mQuery: String? = null
    private var mPageNumber = 0
    private val mIsQueryExhausted: MutableLiveData<Boolean> = MutableLiveData()
    private val mItemInfo: MediatorLiveData<ArrayList<ItemInfo>> = MediatorLiveData()



    init{
        mItemInfoApiClient = ItemInfoApiClient.instance
        initMediators()
    }

    private fun initMediators() {
        val itemInfoListApiSource: LiveData<ArrayList<ItemInfo>> =
            mItemInfoApiClient!!.itemInfo
        mItemInfo.addSource(
            itemInfoListApiSource,
            object : Observer<ArrayList<ItemInfo>> {
                override fun onChanged(@Nullable itemInfo: ArrayList<ItemInfo>) {
                    if (itemInfo != null) {
                        mItemInfo.setValue(itemInfo)
                     //   doneQuery(itemInfo)
                 //   } else { // search database cache
                   //     doneQuery(null)
                    }
                }
            })
    }
/*
    private fun doneQuery(list: ArrayList<ItemInfo>?) {
        if (list != null) {
            if (list.size % 30 != 0) {
                mIsQueryExhausted.setValue(true)
            }
        } else {
            mIsQueryExhausted.setValue(true)
        }
    }

    fun isQueryExhausted(): LiveData<Boolean?>? {
        return mIsQueryExhausted
    }
*/

    fun getItemInfo(): LiveData<ArrayList<ItemInfo>> {
        return mItemInfo
    }

    fun getItemsInfo(): LiveData<ArrayList<ItemInfo>> {
        return mItemInfoApiClient!!.itemInfo
    }


    fun searchItemInfoApi(category: String) {
        mItemInfoApiClient?.searchItemInfoApi(category)
    }

/*  for update search
    fun searchItemInfoApi(query: String, pageNumber: Int) {
        var pageNumber = pageNumber
        if (pageNumber == 0) {
            pageNumber = 1
        }
        mQuery = query
        mPageNumber = pageNumber
        mIsQueryExhausted.setValue(false)
        mItemInfoApiClient?.searchItemInfoApi(query, pageNumber)
    }

    fun searchNextPage() {
        searchItemInfoApi(mQuery, mPageNumber + 1)
    }*/

    fun cancelRequest() {
        mItemInfoApiClient?.cancelRequest()
    }

    fun isItemInfoRequestTimedOut(): LiveData<Boolean?>? {
        return mItemInfoApiClient?.isItemInfoRequestTimedOut
    }

    companion object {
        var instance: ItemInfoRepository? = null
            get() {
                if (field == null) {
                    field = ItemInfoRepository()
                }
                return field
            }
            private set
    }

}*/