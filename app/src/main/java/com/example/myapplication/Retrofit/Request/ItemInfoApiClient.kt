/*package com.example.myapplication.Retrofit.Request

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.Retrofit.AppExecutors
import com.example.myapplication.Retrofit.Request.Responses.ItemInfo
import retrofit2.Call
import retrofit2.Response
import java.util.*
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit

class ItemInfoApiClient private constructor() {
    private val mItemInfo: MutableLiveData<ArrayList<ItemInfo>>
    private var mRetrieveItemInfoRunnable: RetrieveItemInfoRunnable? = null
    private val mItemInfoRequestTimeout: MutableLiveData<Boolean?> = MutableLiveData()
    private val NETWORK_TIMEOUT :Long = 3000

    init {
        mItemInfo = MutableLiveData()
    }


    val itemInfo: LiveData<ArrayList<ItemInfo>>
        get() = mItemInfo

    val isItemInfoRequestTimedOut: LiveData<Boolean?>
        get() = mItemInfoRequestTimeout

    fun searchItemInfoApi(category: String) {
        if (mRetrieveItemInfoRunnable != null) {
            mRetrieveItemInfoRunnable = null
        }
        mRetrieveItemInfoRunnable = RetrieveItemInfoRunnable(category)
        val handler: Future<*> = AppExecutors.instance!!.networkIO().submit(mRetrieveItemInfoRunnable)
        mItemInfoRequestTimeout.setValue(false)
        AppExecutors.instance!!.networkIO().schedule(Runnable {
            mItemInfoRequestTimeout.postValue(true)
            handler.cancel(true)
        }, NETWORK_TIMEOUT, TimeUnit.MILLISECONDS)
    }


    private inner class RetrieveItemInfoRunnable(private val category: String) :
        Runnable {
        internal var cancelRequest: Boolean
        override fun run() {
            if(category!=null) {
                val response: Response<*> = getInfo(category)!!.execute()
                if (cancelRequest) {
                    return
                }
                if (response.code() == 200) {
                    val itemInfo: ArrayList<ItemInfo> = response.body() as ArrayList<ItemInfo>
                    mItemInfo.postValue(itemInfo)
                } else {
                    val error: String = response.errorBody()!!.string()
                    Log.e(TAG, "run: $error")
                    mItemInfo.postValue(null)
                }
            }
        }

        private fun getInfo(category : String): Call<ArrayList<ItemInfo?>>? {
            return RetrofitInstance.itemInfoApi.iteminfo
        }

        fun cancelRequest() {
            Log.d(TAG, "cancelRequest: canceling the search request.")
            cancelRequest = true
        }

        init {
            cancelRequest = false
        }
    }

    fun cancelRequest() {
        if (mRetrieveItemInfoRunnable != null) {
            mRetrieveItemInfoRunnable!!.cancelRequest()
        }
    }

    companion object {
        private const val TAG = "ItemInfoApiClient"
        var instance: ItemInfoApiClient? = null
            get() {
                if (field == null) {
                    field =
                        ItemInfoApiClient()
                }
                return field
            }
            private set
    }

}*/