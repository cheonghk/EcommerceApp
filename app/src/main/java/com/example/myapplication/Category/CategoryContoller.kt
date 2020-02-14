package com.example.myapplication.Category

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.FireBase.ItemInfo_Firebase_Model
import com.example.myapplication.Login.LoginActivity
import com.example.myapplication.R
import com.example.myapplication.Retrofit.Request.Responses.ItemInfo
import com.example.myapplication.ShoppingCart.ShoppingCartModel
import com.example.myapplication.ShoppingCart.Utils.FireStoreUtils
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.cardview_category.view.*
import kotlinx.android.synthetic.main.recyclerview_category.view.*


class CategoryContoller(val view: View) {

    init {
        view.apply {
            recyclerview_category.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }

    class CategoryRecyclerviewAdapter(
        val itemList: MutableList<ItemInfo_Firebase_Model>,
        val category: Int
        //val currentUser : FirebaseUser?
    ) :
        RecyclerView.Adapter<ViewHolder>() {

        private var mItemInfo: List<ItemInfo>? = null
        var currentUser = FirebaseAuth.getInstance().currentUser


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.cardview_category,
                    parent,
                    false
                )
            )

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.also {
                it.bindItemList(itemList[position])
                it.selectedNumCalcalculate()

                it.view.product_image_category.setOnClickListener {
                    when (position) {
                        else -> {
                            holder.showImageSlideDialog(
                                itemList.get(position)
                            )
                        }
                    }
                }

                it.view.bttn_addtocart.setOnClickListener {
                    currentUser = FirebaseAuth.getInstance().currentUser
                    if (currentUser != null) {
                        holder.addToCart(itemList.get(position), category, currentUser!!, position)
                    } else {
                        var intent = Intent(it.context, LoginActivity::class.java)
                        holder.view.context.startActivity(intent)
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
        var num = 0
        fun bindItemList(itemListModel: ItemInfo_Firebase_Model) {
            view.apply {
                product_name_category.text = itemListModel.name
                product_price_category.text = "$" + itemListModel.price.toString()
                Glide.with(context).load(itemListModel.url_forRecyclerview)
                    .into(product_image_category)
            }
        }

        fun selectedNumCalcalculate() {
            view.apply {
                numberOfItem.text = num.toString()
                minusBttn.setOnClickListener {
                    if (num > 0) {
                        num--
                        if (num <= 0) {
                            view.bttn_addtocart.isClickable = false
                            view.bttn_addtocart.alpha = 0.3f
                        }
                        numberOfItem.text = num.toString()
                    }
                }
                plusBttn.setOnClickListener {
                    if (num >= 0) {
                        num++
                        if (num > 0) {
                            view.bttn_addtocart.isClickable = true
                            view.bttn_addtocart.alpha = 1f
                        }
                        numberOfItem.text = num.toString()
                    }
                }
            }
        }


        fun showImageSlideDialog(itemList: ItemInfo_Firebase_Model) {

            val mProductImageSlideFragment = ProductImageSlideFragment(itemList)
            mProductImageSlideFragment.show(
                (view.context as CategoryActivity).getSupportFragmentManager(),
                ""
            )

        }

        fun addToCart(
            itemList: ItemInfo_Firebase_Model,
            category: Int,
            currentUser: FirebaseUser,
            position: Int
        ) {
            if (num > 0) {
                val unicode = itemList.unicode
                val uid = currentUser!!.uid
                val position = position
                val category = category
                val storeNum = num


                val itemInfo2 = ShoppingCartModel()
                itemInfo2.unicode = itemList.unicode
                itemInfo2.sub_category = position
                itemInfo2.totalItems = storeNum
                itemInfo2.category = category

                FireStoreUtils.mFirebaseFirestore(uid).set(itemInfo2).addOnCompleteListener { task ->
                    // mFirebaseFirestore.set(itemInfo).addOnCompleteListener {task ->
                    if (task.isSuccessful) {
                        Snackbar.make(view, "Added to cart", Snackbar.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener { e ->
                    Log.w(TAG, "Error writing document", e)
                    Toast.makeText(view.context, "Failed", Toast.LENGTH_SHORT).show()
                }

            }
        }

        companion object

        val TAG = "CategoryController.CategoryRecyclerviewAdapter"
    }
}