package com.example.myapplication.ShoppingCart

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.shoppingcart.*

class ShoppingCartActivity:  AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private var mAuthListener: FirebaseAuth.AuthStateListener? = null
    //val adapter = RecyclerviewShoppingCartAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.shoppingcart)
        mAuth = FirebaseAuth.getInstance()
        //recyclerview_shoppingcart.adapter = adapter
      //  recyclerview_shoppingcart.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    }


    override fun onStart() {
        super.onStart()
        initUserStatus()
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
            val email = user.email
            // val photoUrl = user.photoUrl
            // Check if user's email is verified
            val emailVerified = user.isEmailVerified

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            //val uid = user.uid
        } else {
        }
    }



    override fun onStop() {
        super.onStop()
        FirebaseAuth.getInstance().removeAuthStateListener { this }
    }
}