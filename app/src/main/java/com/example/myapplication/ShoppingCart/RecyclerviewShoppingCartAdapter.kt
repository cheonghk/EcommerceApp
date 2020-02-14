package com.example.myapplication.ShoppingCart

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.FireBase.FireBaseCollector
import com.example.myapplication.FireBase.ItemInfo_Firebase_Model
import com.example.myapplication.R
import com.example.myapplication.ShoppingCart.Utils.FireStoreUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.cardview_category.view.*
import kotlinx.android.synthetic.main.cardview_shoppingcart.view.*
import kotlinx.android.synthetic.main.shoppingcart.view.*

class RecyclerviewShoppingCartAdapter (val itemList: MutableList<ItemInfo_Firebase_Model>, val userShoppingCartList :MutableList<ShoppingCartModel>):
    RecyclerView.Adapter<RecyclerviewShoppingCartAdapter.ShoppingCartViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingCartViewHolder =
        ShoppingCartViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.cardview_shoppingcart,
                parent,
                false
            )
        )

    override fun getItemCount(): Int {
        return userShoppingCartList.size
    }

    override fun onBindViewHolder(holder: ShoppingCartViewHolder, position: Int) {
        //itemList.get(position)
        holder.loadUserCart(userShoppingCartList, position)
    }

    class ShoppingCartViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val mFireBaseCollector = FireBaseCollector()

        fun loadUserCart(userShoppingCartInfo :MutableList<ShoppingCartModel>
        , position :Int){
            var category = userShoppingCartInfo.get(position).category
            var subcategory_position = userShoppingCartInfo.get(position).sub_category
            var totalItems = userShoppingCartInfo.get(position).totalItems
            var unicode = userShoppingCartInfo.get(position).unicode

            mFireBaseCollector.readData_userShoppingCart(object:FireBaseCollector.ShoppingCartDataStatus{
                override fun ShoppingCartDataIsLoaded(userShoppingCartList: MutableList<MutableList<ItemInfo_Firebase_Model>>) {
                  fun subList():ItemInfo_Firebase_Model{
                        return userShoppingCartList.get(category!!-1).get(subcategory_position!!)
                    }
                    view.apply {
                        product_name_shoppingcart.text = subList().name
                        product_price_shoppingcart.text = "$" + subList().price.toString()
                        Glide.with(context).load(subList().url_forRecyclerview)
                            .into(product_image_shoppingcart)
                        numberOfItem_shoppingcart.text = totalItems.toString()
                    }
                }
            })
        }

        fun bindItemList(itemListModel: ItemInfo_Firebase_Model) {
            view.apply {
                product_name_shoppingcart.text = itemListModel.name
                product_price_shoppingcart.text = "$" + itemListModel.price.toString()
                Glide.with(context).load(itemListModel.url_forRecyclerview)
                    .into(product_image_shoppingcart)

            }
        }

        companion object const

        val TAG = "RecyclerviewShoppingCartAdapter.ShoppingCartViewHolder"
    }
}






