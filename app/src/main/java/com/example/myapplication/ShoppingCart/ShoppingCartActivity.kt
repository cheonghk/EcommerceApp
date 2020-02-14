package com.example.myapplication.ShoppingCart

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
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
    //val adapter = RecyclerviewShoppingCartAdapter()
    private val mFireBaseCollector = FireBaseCollector()
    private var itemList = mutableListOf<ItemInfo_Firebase_Model>()
    private var userShoppingCartList = mutableListOf<ShoppingCartModel>()
    //var adapter : RecyclerviewShoppingCartAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.shoppingcart)
       // initialize()

        mAuth = FirebaseAuth.getInstance()
        recyclerview_shoppingcart.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        //recyclerview_shoppingcart.adapter = adapter
      //  recyclerview_shoppingcart.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    }


    override fun onStart() {
        super.onStart()
        initUserStatus()
        initialize()
    }

    fun initialize(){
        mFireBaseCollector.readAllData(object : FireBaseCollector.DataStatus {
            override fun DataIsLoaded(theItemListModel: MutableList<ItemInfo_Firebase_Model>) {
                itemList.addAll(theItemListModel)
                recyclerview_shoppingcart.adapter = RecyclerviewShoppingCartAdapter(itemList, userShoppingCartList)
                Log.i("adapter", "${userShoppingCartList.size}")
            }
        })

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
                            recyclerview_shoppingcart.visibility = View.VISIBLE
                            recyclerview_shoppingcart.adapter?.notifyDataSetChanged()
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