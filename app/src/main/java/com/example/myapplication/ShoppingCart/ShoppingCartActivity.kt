package com.example.myapplication.ShoppingCart

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.FireBase.FireBaseCollector
import com.example.myapplication.FireBase.ItemInfo_Firebase_Model
import com.example.myapplication.R
import com.example.myapplication.ShoppingCart.Utils.FireStoreRetrivalUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.cardview_main.*
import kotlinx.android.synthetic.main.shoppingcart.*

class ShoppingCartActivity:  AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private var mAuthListener: FirebaseAuth.AuthStateListener? = null
    private val mFireBaseCollector = FireBaseCollector()
    private var userShoppingCartList = mutableListOf<ShoppingCartModel>()
    private var adapter :RecyclerviewShoppingCartAdapter? = null
    private var updateAllTotalAmount = 0.0
    private var user : FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.shoppingcart)
        mAuth = FirebaseAuth.getInstance()
        recyclerview_shoppingcart.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        }



    override fun onStart() {
        super.onStart()
        initUserStatus()
    }

    fun initUserStatus(){
        mAuthListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            user = firebaseAuth.currentUser
            updateUI(true)
        }
    }

    fun updateUI(isInitilize :Boolean) {
        if (user != null) {   // Name, email address, and profile photo Url
            // val name = user.displayName
            //val email = user.email
            // val photoUrl = user.photoUrl
            // Check if user's email is verified
            //val emailVerified = user.isEmailVerified
            val uid = user?.uid

            FireStoreRetrivalUtils.mFirebaseFirestore(uid!!)
                .get().addOnSuccessListener { userItems ->
                    if (!userItems!!.isEmpty) {
                        val userItemList = userItems.documents
                        userShoppingCartList.clear() //prevent repeatedly loading items when activity start
                        for (i in userItemList) {
                            val cartItems = i.toObject(ShoppingCartModel::class.java)
                            userShoppingCartList.add(cartItems!!)}

                        if(isInitilize) {
                            adapter = RecyclerviewShoppingCartAdapter(userShoppingCartList, uid)
                            //adapter!!.setData(userShoppingCartList)
                            recyclerview_shoppingcart.adapter = adapter
                            recyclerview_shoppingcart.visibility = View.VISIBLE
                        }else{
                            recyclerview_shoppingcart.adapter!!.notifyDataSetChanged()
                        }

                        initTotalAmount()
                        callBackFromAdapter()

                    }else{
                        userShoppingCartList.clear()
                        initTotalAmount()
                        textview_cartempty.visibility = View.VISIBLE
                        recyclerview_shoppingcart.visibility = View.INVISIBLE
                        Log.i(TAG, "Retrival failed or cart is empty, size = : ${userShoppingCartList.size}")
                    }
                }.addOnFailureListener {
                    Log.i(TAG, " updateUI fail")
                }
        }}

    fun initTotalAmount(){
        var allTotalAmount : Double = 0.0
        var num :Int =0
        var price :Long = 0

        if(userShoppingCartList.size==0){ //no item, empty cart
            totalAmountText.text = "$" + allTotalAmount.toString()
            checkOut(false)
            return
        }

        for(position in 0 until userShoppingCartList.size) {
            var category = userShoppingCartList.get(position).category
            var subcategory_position = userShoppingCartList.get(position).sub_category
            var totalItems = userShoppingCartList.get(position).totalItems
            var unicode = userShoppingCartList.get(position).unicode

            mFireBaseCollector.readData_userShoppingCart(object :
                FireBaseCollector.ShoppingCartDataStatus {
                override fun ShoppingCartData(retriveListByCategoryPosition: MutableList<MutableList<ItemInfo_Firebase_Model>>) {
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
        checkOut(true)
        }

    fun callBackFromAdapter(){
        adapter!!.setCallBack(object : RecyclerviewShoppingCartAdapter.CallBack{

            override fun updateTotalAmount(updatePrice : Double) {
                updateAllTotalAmount += updatePrice
                val updatedAmount = updateAllTotalAmount
                totalAmountText.text = "$" + updatedAmount.toString()
            }

            override fun updateUIAfterDeletedItem() {
                updateUI(false)
                Toast.makeText(this@ShoppingCartActivity, "Item is deleted", Toast.LENGTH_SHORT).show()
            }
        })}


    override fun onResume() {
        super.onResume()
        if (mAuthListener != null) {
            mAuth.addAuthStateListener(mAuthListener!!)
        }
    }

    fun checkOut(canbecheckout : Boolean){
        if(canbecheckout){
            checkoutBttn.isClickable = true
            checkoutBttn.alpha = 1f
            //checkoutBttn.setOnClickListener {  }
        }
        else{checkoutBttn.alpha = 0.3f
            checkoutBttn.isClickable = false}
    }




    override fun onStop() {
        super.onStop()
        FirebaseAuth.getInstance().removeAuthStateListener {this}
    }

    companion object const val TAG = "ShoppingCartActivity"
}