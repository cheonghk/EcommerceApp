package com.example.myapplication.ShoppingCart

import android.app.Dialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.FireBase.FireBaseCollector
import com.example.myapplication.FireBase.ItemInfo_Firebase_Model
import com.example.myapplication.R
import kotlinx.android.synthetic.main.cardview_shoppingcart.view.*


class RecyclerviewShoppingCartAdapter (val userShoppingCartList :MutableList<ShoppingCartModel>):
    RecyclerView.Adapter<RecyclerviewShoppingCartAdapter.ShoppingCartViewHolder>() {

    var callBack : CallBack? = null

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

    fun setMyCallBack(callBack: CallBack) {
        this.callBack = callBack
    }

    interface CallBack {
        fun updateTotalAmount(updatePrice : Double)
    }

    override fun onBindViewHolder(holder: ShoppingCartViewHolder, position: Int) {
        holder.initUserCart(userShoppingCartList, position)
        holder.selectedNumCalcalculate(callBack!!)
        holder.view.delItem.setOnClickListener {
            Toast.makeText(holder.view.context, "not defined yet", Toast.LENGTH_SHORT).show()
        }
    }

    class ShoppingCartViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        var totalAmount :Double= 0.0
        val mFireBaseCollector = FireBaseCollector()

        var num :Int =0
        var price :Long = 0


        fun initUserCart(userShoppingCartInfo :MutableList<ShoppingCartModel>
        , position :Int){
            var category = userShoppingCartInfo.get(position).category
            var subcategory_position = userShoppingCartInfo.get(position).sub_category
            var totalItems = userShoppingCartInfo.get(position).totalItems
            var unicode = userShoppingCartInfo.get(position).unicode

            mFireBaseCollector.readData_userShoppingCart(object:FireBaseCollector.ShoppingCartDataStatus{
                override fun ShoppingCartDataIsLoaded(retriveListByCategoryPosition: MutableList<MutableList<ItemInfo_Firebase_Model>>) {

                    fun dataProvider():ItemInfo_Firebase_Model{
                        return retriveListByCategoryPosition.get(category!!-1).get(subcategory_position!!)
                    }

                    price = dataProvider().price!!
                    num = totalItems!!

                    view.apply {
                        product_name_shoppingcart.text = dataProvider().name
                        product_price_shoppingcart.text = "$" + price.toString()
                        Glide.with(context).load(dataProvider().url_forRecyclerview)
                            .into(product_image_shoppingcart)
                        numberOfItem_shoppingcart.text = totalItems.toString()

                        setTextTotalPrice()
                    }
                }
            })
        }

        fun selectedNumCalcalculate(callBack : CallBack) {
            view.apply {
                numberOfItem_shoppingcart.text = num.toString()
                minusBttn_shoppingcart.setOnClickListener {
                    if (num > 1) { //at least 1 item in the list of shoppingcart, use del button instead if destroy
                        num--
                        numberOfItem_shoppingcart.text = num.toString()
                        updateAllItemsTotalAmount(callBack, -price.toDouble())
                        setTextTotalPrice()
                    }

                }
                plusBttn_shoppingcart.setOnClickListener {
                        num++
                        numberOfItem_shoppingcart.text = num.toString()
                        updateAllItemsTotalAmount(callBack, price.toDouble())
                        setTextTotalPrice()
                }
            }
        }

        fun setTextTotalPrice(){
            var totalAmount : Double=num*price.toDouble()
            if(totalAmount ==0.0){
                view.product_totalPrice_shoppingcart.text = "$" + totalAmount.toInt().toString()
                return
            }
            view.product_totalPrice_shoppingcart.text = "$" + totalAmount.toString()
        }

        fun updateAllItemsTotalAmount(callBack : CallBack, price:Double){
          callBack.updateTotalAmount(price)
        //   totalAmount.text = "$"
        }

        fun deleteItem(){
            view.delItem.setOnClickListener { Toast.makeText(view.context, "not defined yet",Toast.LENGTH_SHORT).show() }
        }

        companion object const
        val TAG = "RecyclerviewShoppingCartAdapter.ShoppingCartViewHolder"
    }
}






