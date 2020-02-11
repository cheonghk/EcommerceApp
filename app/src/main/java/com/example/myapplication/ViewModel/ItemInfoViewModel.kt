/*package com.example.myapplication.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.Repository.ItemInfoRepository
import com.example.myapplication.Retrofit.Request.Responses.ItemInfo

class ItemInfoViewModel : ViewModel() {

    private var mItemInfoRepository: ItemInfoRepository? = null
    private var mIsViewingRecipes = false
    private var mIsPerformingQuery = false

    fun RecipeListViewModel() {
        mItemInfoRepository = ItemInfoRepository.instance
        mIsPerformingQuery = false
    }

    fun getItemInfo(): LiveData<ArrayList<ItemInfo>> {
        return mItemInfoRepository!!.getItemInfo()
    }

    /*fun isQueryExhausted(): LiveData<Boolean?>? {
        return mItemInfoRepository.isQueryExhausted()
    }*/

    fun searchItemInfo(category : String) {
        mIsViewingRecipes = true
        mIsPerformingQuery = true
        mItemInfoRepository?.searchItemInfoApi(category)
    }

  /*  fun searchNextPage() {
        if (!mIsPerformingQuery
            && mIsViewingRecipes
          //  && !isQueryExhausted().getValue()
        ) {
           mItemInfoRepository.searchNextPage()
        }
    }
*/
    fun isViewingRecipes(): Boolean {
        return mIsViewingRecipes
    }

    fun setIsViewingRecipes(isViewingRecipes: Boolean) {
        mIsViewingRecipes = isViewingRecipes
    }

    fun setIsPerformingQuery(isPerformingQuery: Boolean?) {
        mIsPerformingQuery = isPerformingQuery!!
    }

    fun isPerformingQuery(): Boolean {
        return mIsPerformingQuery
    }

    fun onBackPressed(): Boolean {
        if (mIsPerformingQuery) { // cancel the query
            mItemInfoRepository?.cancelRequest()
            mIsPerformingQuery = false
        }
        if (mIsViewingRecipes) {
            mIsViewingRecipes = false
            return false
        }
        return true
    }


}*/