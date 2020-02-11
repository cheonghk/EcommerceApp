package com.example.myapplication.Category

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.FireBase.ItemInfo_Firebase_Model
import com.example.myapplication.R
import com.example.myapplication.Retrofit.Request.Responses.ItemInfo
import kotlinx.android.synthetic.main.cardview_category.view.*
import kotlinx.android.synthetic.main.recyclerview_category.view.*


class CategoryContoller(val view: View) {

    init {
        view.apply {
            recyclerview_category.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }

    class CategoryRecyclerviewAdapter(val itemList: MutableList<ItemInfo_Firebase_Model>) :
        RecyclerView.Adapter<ViewHolder>() {

        private var mItemInfo: List<ItemInfo>? = null

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.cardview_category,
                    parent,
                    false
                )
            )

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bindItemList(itemList[position])
            holder.selectedNumCalcalculate()

                holder.view.product_image_category.setOnClickListener {
                    when (position) {
                        else -> {holder.showImageSlideDialog(
                            itemList.get(position))
                            Log.i("position", " ${ itemList.get(position).url} + ${itemList.get(position).size}")
                        }
                    }
                }


            /*************retrofit_internal**********
            holder.itemView.also {
            it.product_name_category.text =
            contactList?.products_info?.accessories?.get(position)?.name
            it.product_price_category.text =
            "$" + contactList?.products_info?.accessories?.get(position)?.price
            Glide.with(it.context)
            .load(contactList?.products_info?.accessories?.get(position)?.url?.get(0))
            .into(it.product_image_category)*/

        }
        override fun getItemCount(): Int {
            return itemList!!.size
        }

        fun setItemInfo(itemInfo: List<ItemInfo>) {
            mItemInfo = itemInfo
            notifyDataSetChanged()
        }
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bindItemList(itemListModel: ItemInfo_Firebase_Model) {
            view.apply {
            product_name_category.text = itemListModel.name
            product_price_category.text = "$" + itemListModel.price.toString()
            Glide.with(context).load(itemListModel.url_forRecyclerview).into(product_image_category)
        }}

        fun selectedNumCalcalculate(){
            var num = 0
            view.apply {
            numberOfItem.text = num.toString()
            minusBttn.setOnClickListener {if(num>0){
            num--
            numberOfItem.text = num.toString()}
            }
            plusBttn.setOnClickListener {if(num>=0){
                num++
                numberOfItem.text = num.toString()}
            }
        }
            }


        fun showImageSlideDialog(itemList:ItemInfo_Firebase_Model){

              val mProductImageSlideFragment = ProductImageSlideFragment(itemList)
                mProductImageSlideFragment.show((view.context as CategoryActivity).getSupportFragmentManager(), "")

        }
    }
}