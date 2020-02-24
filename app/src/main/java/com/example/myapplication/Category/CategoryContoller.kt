package com.example.myapplication.Category

import android.content.Intent
import android.os.Handler
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
import com.example.myapplication.ShoppingCart.ShoppingCartModel
import com.example.myapplication.ShoppingCart.Utils.FireStoreRetrivalUtils
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ListenerRegistration
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
    ) :
        RecyclerView.Adapter<CategoryRecyclerviewAdapter.CategoryViewHolder>() {

        var currentUser = FirebaseAuth.getInstance().currentUser


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder =
            CategoryViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.cardview_category,
                    parent,
                    false
                )
            )

        override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
            holder.also {
                it.bindItemList(itemList[position])
                it.selectedNumCalcalculate(itemList[position])
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
            return itemList.size
        }


        class CategoryViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

            var num: Int = 0

            fun bindItemList(itemListModel: ItemInfo_Firebase_Model) {
                view.apply {
                    product_name_category.text = itemListModel.name
                    product_price_category.text = "$" + itemListModel.price!!.toDouble().toString()
                    Glide.with(context).load(itemListModel.url_forRecyclerview)
                        .into(product_image_category)
                }
            }

            fun selectedNumCalcalculate(itemListModel: ItemInfo_Firebase_Model) {
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
                        setTotalPrice(itemListModel.price!!)
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
                        setTotalPrice(itemListModel.price!!)
                    }
                }
            }

            fun setTotalPrice(price: Long) {
                var totalAmount: Double = num * price.toDouble()
                if (totalAmount == 0.0) {
                    view.product_totalPrice_category.text = ""
                    return
                }
                view.product_totalPrice_category.text = "$" + totalAmount.toString()
            }


            fun showImageSlideDialog(itemList: ItemInfo_Firebase_Model) {
                val mProductImageSlideFragment = ProductImageSlideDialogFragment(itemList)
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
                view.bttn_addtocart.isClickable=false
                    if (num > 0) {
                        var registration: ListenerRegistration? = null
                        val unicode = itemList.unicode
                        val uid = currentUser.uid

                        var itemsRef = FireStoreRetrivalUtils.mFirebaseFirestore(uid)
                        var unicodeRef =
                            FireStoreRetrivalUtils.mFirebaseFirestore(uid).document(unicode!!)


                        registration = unicodeRef.addSnapshotListener { documentSnapshot, exce ->
                            if (documentSnapshot!!.exists()) {//run if item is already exist

                                //find out the oringal number
                                val shoppingModelObj =
                                    documentSnapshot.toObject(ShoppingCartModel::class.java)
                                val origTotalItems = shoppingModelObj?.totalItems

                                var updatedNum = num + origTotalItems!!

                                //update of item num insdtead of creating
                                unicodeRef.update("totalItems", updatedNum)
                                    .addOnSuccessListener {
                                        Snackbar.make(
                                            view,
                                            "The cart is updated",
                                            Snackbar.LENGTH_SHORT
                                        ).show()
                                        Log.i(TAG, "Update item succesfully")
                                    }.addOnFailureListener { e ->
                                        Toast.makeText(
                                            view.context,
                                            "${e.message}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                registration?.remove()

                            } else{
                                //create new node if it is new item
                                val itemInfo = ShoppingCartModel()
                                itemInfo.unicode = itemList.unicode
                                itemInfo.sub_category = position
                                itemInfo.totalItems = num
                                itemInfo.category = category



                                //add to cart
                                itemsRef.document(itemList.unicode!!).set(itemInfo)
                                    .addOnSuccessListener {
                                        Toast.makeText(
                                            view.context,
                                            "Added to cart",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }.addOnFailureListener { e ->
                                    Log.w(TAG, "Error writing document", e)
                                    Toast.makeText(view.context, "Failed", Toast.LENGTH_SHORT)
                                        .show()
                                }
                                registration?.remove()
                            }

                        }

                    }
                Handler().postDelayed(Runnable { view.bttn_addtocart.isClickable=true },1500)

            }

            companion object
            val TAG = "CategoryController.CategoryRecyclerviewAdapter"
        }
    }
}