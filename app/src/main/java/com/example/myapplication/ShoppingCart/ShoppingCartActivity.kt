package com.example.myapplication.ShoppingCart

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Adapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.Category.CategoryContoller
import com.example.myapplication.FireBase.FireBaseCollector
import com.example.myapplication.FireBase.ItemInfo_Firebase_Model
import com.example.myapplication.R
import com.example.myapplication.ShoppingCart.Utils.FireStoreUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.recyclerview_category.*
import kotlinx.android.synthetic.main.recyclerview_category.view.*
import kotlinx.android.synthetic.main.shoppingcart.*
import kotlinx.android.synthetic.main.shoppingcart.view.*

class ShoppingCartActivity:  AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private var mAuthListener: FirebaseAuth.AuthStateListener? = null
    private val mFireBaseCollector = FireBaseCollector()
    private var userShoppingCartList = mutableListOf<ShoppingCartModel>()
    private var adapter :RecyclerviewShoppingCartAdapter? = null
    private var updateAllTotalAmount = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.shoppingcart)
        mAuth = FirebaseAuth.getInstance()
        recyclerview_shoppingcart.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        //recyclerview_shoppingcart.adapter = adapter
      //  recyclerview_shoppingcart.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    }


    override fun onStart() {
        super.onStart()
        initUserStatus()
    }


    fun updateAllTotalAmountFromAdapter(){
                adapter!!.setMyCallBack(object : RecyclerviewShoppingCartAdapter.CallBack{
                    override fun updateTotalAmount(updatePrice : Double) {
                        updateAllTotalAmount += updatePrice
                        val updatedAmount = updateAllTotalAmount
                        totalAmountText.text = "$" + updatedAmount.toString()
                        Log.i("updateAllTotalAmount","${updatedAmount}" )
                    }
                })}


    fun initTotalAmount(){
        var allTotalAmount : Double = 0.0
        var num :Int =0
        var price :Long = 0

        for(position in 0 until userShoppingCartList.size) {
            Log.i("position  - i", "${position}")
            var category = userShoppingCartList.get(position).category
            var subcategory_position = userShoppingCartList.get(position).sub_category
            var totalItems = userShoppingCartList.get(position).totalItems
            var unicode = userShoppingCartList.get(position).unicode
            mFireBaseCollector.readData_userShoppingCart(object :
                FireBaseCollector.ShoppingCartDataStatus {
                override fun ShoppingCartDataIsLoaded(retriveListByCategoryPosition: MutableList<MutableList<ItemInfo_Firebase_Model>>) {
                    //retrivedListByCategoryPosition.add(retriveListByCategoryPosition)
                    fun dataProvider(): ItemInfo_Firebase_Model {
                        return retriveListByCategoryPosition.get(category!! - 1)
                            .get(subcategory_position!!)
                    }

                    price = dataProvider().price!!
                    num = totalItems!!
                    val singleItemTotalPrice = price * num
                    allTotalAmount += singleItemTotalPrice
                    if (position == userShoppingCartList.size-1)
                        totalAmountText.text = "$" + allTotalAmount.toString()
                    updateAllTotalAmount = allTotalAmount
                }
            })
        }
        }


        fun initUserStatus(){
            mAuthListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user: FirebaseUser? = firebaseAuth.currentUser
                updateUI(user)
        }
        }


    fun updateUI(user: FirebaseUser?) {
        if (user != null) {   // Name, email address, and profile photo Url
            // val name = user.displayName
            //val email = user.email
            // val photoUrl = user.photoUrl
            // Check if user's email is verified
            //val emailVerified = user.isEmailVerified
            val uid = user.uid
            FirebaseFirestore.getInstance().collection("shoppingcart").document(uid).collection("Items")
                .get().addOnSuccessListener { userItems ->
                    if (!userItems!!.isEmpty) {
                        val userItemList = userItems.documents
                        for (i in userItemList) {
                            val cartItems = i.toObject(ShoppingCartModel::class.java)
                            userShoppingCartList.add(cartItems!!)

                            adapter = RecyclerviewShoppingCartAdapter(userShoppingCartList)
                            recyclerview_shoppingcart.adapter = adapter

                            recyclerview_shoppingcart.visibility = View.VISIBLE
                           recyclerview_shoppingcart.adapter?.notifyDataSetChanged()

                            initTotalAmount()
                            updateAllTotalAmountFromAdapter()

                        }
                    }else{
                        textview_cartempty.visibility = View.VISIBLE
                       Log.i("Retrival failed", "${userShoppingCartList!=null}")
                    }
                }.addOnFailureListener {
                    Log.i(TAG, "fail")
                }
        }}


    override fun onResume() {
        super.onResume()
        if (mAuthListener != null) {
            mAuth.addAuthStateListener(mAuthListener!!)
        }
    }

    override fun onStop() {
        super.onStop()
        FirebaseAuth.getInstance().removeAuthStateListener { this }
    }

    companion object const val TAG = "ShoppingCartActivity"
}