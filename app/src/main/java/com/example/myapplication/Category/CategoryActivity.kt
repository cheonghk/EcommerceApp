package com.example.myapplication.Category

//import com.example.myapplication.ViewModel.ItemInfoViewModel

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.FireBase.FireBaseCollector
import com.example.myapplication.FireBase.ItemInfo_Firebase_Model
import com.example.myapplication.R
import com.example.myapplication.Retrofit.Request.Responses.ProductInfoList
import com.example.myapplication.Utils.FragmentTransactionUtils
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import kotlinx.android.synthetic.main.cardview_category.*
import kotlinx.android.synthetic.main.cardview_category.view.*
import kotlinx.android.synthetic.main.recyclerview_category.*
import java.io.IOException
import java.lang.reflect.Type

class CategoryActivity : AppCompatActivity() {

    private val mFireBaseCollector = FireBaseCollector()
    private var itemList = mutableListOf<ItemInfo_Firebase_Model>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_category)
        var bundle : Bundle?=intent.extras
        val getString = bundle!!.getString("category")
        initialize(getString!!)


        FragmentTransactionUtils.changeFragment(this,
            R.id.fragment_container_category_dialog,
            CategoryFragment()
        )

                //getAssetJsonData_internal(this)

        //getRetrofitData() //for retrofit

       // recyclerview_category.adapter = CategoryContoller.CategoryRecyclerView_Adapter(getAssetJsonData_internal(this))
    }

    fun initialize(category:String){
        mFireBaseCollector.readData_CategoryContoller(object : FireBaseCollector.DataStatus {
            override fun DataIsLoaded(theItemListModel: MutableList<ItemInfo_Firebase_Model>) {
                itemList.addAll(theItemListModel)
                recyclerview_category.adapter =
                    CategoryContoller.CategoryRecyclerviewAdapter(
                        itemList
                    )
            }
        }, category)

    }


    @Throws(IOException::class)
    fun getAssetJsonData_internal(context: Context):ProductInfoList {
        val json: String
            val inputStream = context.getAssets().open("category.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.use { it.read(buffer) }
            json = String(buffer)
        val data: String? = json
        val type: Type = object : TypeToken<ProductInfoList>(){}.getType()
        val productInfoList: ProductInfoList = Gson().fromJson(data, type)
        return productInfoList
    }

   /* fun getRetrofitData() {

        val api: ItemInfoApi = RetroClient.apiService

        val call: Call<CategoryList> = api.iteminfo
        Log.i("see", "outttttt")
        call.enqueue(object : Callback<CategoryList> {
            override fun onResponse(
                call: Call<CategoryList>,
                response: Response<CategoryList>
            ) {Log.i("see", "out")
                if (response.isSuccessful()) {
                    Log.i("see", "in")
                    contactList = response.body()?.bag


                    recyclerview_category.adapter =
                        CategoryContoller.CategoryRecyclerView_Adapter(contactList)
                }
            }

            override fun onFailure(
                call: Call<CategoryList>,
                t: Throwable
            ) {
            }
        })

    }*/
}

  /*      val category = intent.getStringExtra(CardViewController.Cardview_RecyclerviewAdapter.category)


        mItemInfoViewModel = ViewModelProviders.of(this).get(ItemInfoViewModel::class.java)

        recyclerview_category.adapter = CategoryContoller.CategoryRecyclerView_Adapter()
        recyclerview_category?.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        
        subscribeObservers()
*/

    /*fun subscribeObservers() {
        mItemInfoViewModel?.getItemInfo()?.observe(this, object : Observer<List<ItemInfo>> {
            override fun onChanged(@Nullable itemInfo: List<ItemInfo>) {
                if (itemInfo != null && mItemInfoViewModel != null) {
                    if (mItemInfoViewModel!!.isViewingRecipes()) {
                        //mItemInfoViewModel.setIsPerformingQuery(false)
                        categoryAdapter?.setItemInfo(itemInfo)
                    }
                }
            }
        })
    }
}*/