package com.example.myapplication.ShoppingCart


import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.FireBase.FireBaseCollector
import com.example.myapplication.FireBase.ItemInfo_Firebase_Model
import com.example.myapplication.R
import com.example.myapplication.ShoppingCart.Utils.FireStoreRetrivalUtils
import kotlinx.android.synthetic.main.cardview_main.*
import kotlinx.android.synthetic.main.cardview_shoppingcart.view.*
import kotlinx.android.synthetic.main.deleteitem_dialog.view.*

class RecyclerviewShoppingCartAdapter(
    val userShoppingCartList: MutableList<ShoppingCartModel>,
    val uid: String
) :
    RecyclerView.Adapter<RecyclerviewShoppingCartAdapter.ShoppingCartViewHolder>() {

    var  mCallBack:  CallBack? = null
   // var mCallBackAfterDeleteItem:  CallBackToUpdateAmount? = null

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

    fun setCallBack(theCallBack:  CallBack) {
        this.mCallBack= theCallBack
    }



    interface CallBack {
        fun updateTotalAmount(updatePrice: Double)
        fun updateUIAfterDeletedItem()
    }


    override fun onBindViewHolder(holder: ShoppingCartViewHolder, position: Int) {
        holder.also {
        it.initUserCart(userShoppingCartList, position)
        it.selectedNumCalcalculate(mCallBack!!, uid)
       }
        holder.view.delItem.setOnClickListener {
            holder.deleteItemAlertDialog(mCallBack!!, uid)
        }
    }

    fun setData(itemList: MutableList<ShoppingCartModel>) {
        val diffCallback =
            ShoppingCartActivity_DiffCallback(userShoppingCartList, itemList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        userShoppingCartList.clear()
        userShoppingCartList.addAll(itemList)
        diffResult.dispatchUpdatesTo(this)
    }


    class ShoppingCartViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        var totalAmount: Double = 0.0
        val mFireBaseCollector = FireBaseCollector()
        var num: Int = 0
        var price: Long = 0
        val itemInfo = ShoppingCartModel()
        var unicode: String? = null
        var alertDialog: AlertDialog? = null

        fun initUserCart(
            userShoppingCartInfo: MutableList<ShoppingCartModel>
            , position: Int
        ) {
            var category = userShoppingCartInfo.get(position).category
            var subcategory_position = userShoppingCartInfo.get(position).sub_category
            var totalItems = userShoppingCartInfo.get(position).totalItems
            var unicode = userShoppingCartInfo.get(position).unicode

            this.num = totalItems!!
            this.unicode = unicode


            mFireBaseCollector.readData_userShoppingCart(object :
                FireBaseCollector.ShoppingCartDataStatus {
                override fun ShoppingCartData(retriveListByCategoryPosition: MutableList<MutableList<ItemInfo_Firebase_Model>>) {

                    val dataProvider: ItemInfo_Firebase_Model =
                        retriveListByCategoryPosition.get(category!! - 1)
                            .get(subcategory_position!!)

                    price = dataProvider.price!!

                    view.apply {
                        product_name_shoppingcart.text = dataProvider.name
                        product_price_shoppingcart.text = "$" + price.toString()
                        Glide.with(context).load(dataProvider.url_forRecyclerview)
                            .into(product_image_shoppingcart)
                        numberOfItem_shoppingcart.text = num.toString()
                        setTextTotalPrice()
                    }

                }
            })
        }

        fun selectedNumCalcalculate(callBack: CallBack, uid: String) {
            view.apply {
                numberOfItem_shoppingcart.text = num.toString()
                minusBttn_shoppingcart.setOnClickListener {
                    if (num > 1) { //at least 1 item in the list of shoppingcart, use del button instead if destroy
                        num--
                        numberOfItem_shoppingcart.text = num.toString()
                        setTextTotalPrice()
                        callBack.updateTotalAmount(-price.toDouble())
                        updateItem(uid)
                    }

                }
                plusBttn_shoppingcart.setOnClickListener {
                    num++
                    numberOfItem_shoppingcart.text = num.toString()
                    setTextTotalPrice()
                    callBack.updateTotalAmount(price.toDouble())
                    updateItem(uid)
                }
            }
        }

        fun setTextTotalPrice() {
            var totalAmount: Double = num * price.toDouble()
            if (totalAmount == 0.0) {
                view.product_totalPrice_shoppingcart.text = "$" + totalAmount.toInt().toString()
                return
            }
            view.product_totalPrice_shoppingcart.text = "$" + totalAmount.toString()
        }

        fun updateItem(uid: String) {
            val ref = FireStoreRetrivalUtils.mFirebaseFirestore(uid).document(unicode!!)
            ref.update("totalItems", num).addOnSuccessListener {
                Log.i(TAG, "Update item succesfully")
            }.addOnFailureListener { e ->
                Toast.makeText(view.context, "${e.message}", Toast.LENGTH_SHORT).show()
            }
        }


        fun deleteItemAlertDialog(callBack: CallBack, uid: String) {
            val builder: AlertDialog.Builder = AlertDialog.Builder(view.context)
            //builder.setMessage("Delete the item?")
            val inflater =
                LayoutInflater.from(view.context).inflate(R.layout.deleteitem_dialog, null)
            builder.setView(inflater)
            builder.setCancelable(true)
            var alertDialog = builder.create()
            alertDialog.show()

            val bttn_confirmdelete = inflater.findViewById<Button>(R.id.bttn_confirmdelete)
            val bttn_canceldelete = inflater.findViewById<Button>(R.id.bttn_canceldelete)
              bttn_canceldelete.setOnClickListener { alertDialog.cancel()}
              bttn_confirmdelete.setOnClickListener{ operateDeleteItem(callBack, uid)
                      alertDialog.dismiss()
              }
          }


            fun operateDeleteItem(callBack: CallBack, uid: String) {
            val ref = FireStoreRetrivalUtils.mFirebaseFirestore(uid).document(unicode!!)
            ref.delete().addOnSuccessListener {
                callBack?.updateUIAfterDeletedItem()
            }.addOnFailureListener { e ->
                Toast.makeText(
                    view.context,
                    "${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
        }}

        companion object const

        val TAG = "RecyclerviewShoppingCartAdapter.ShoppingCartViewHolder"
    }

    class ShoppingCartActivity_DiffCallback(
        private val oldList: MutableList<ShoppingCartModel>,
        private val newList: MutableList<ShoppingCartModel>
    ) : DiffUtil.Callback() {


        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].unicode === newList[newItemPosition].unicode
        }

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList.get(oldItemPosition).equals(newList.get(newItemPosition))
        }

    }
}






