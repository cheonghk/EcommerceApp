package com.example.myapplication.Category


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.example.myapplication.Login.LoginActivity
import com.example.myapplication.Main.CardViewController
import com.example.myapplication.R
import com.example.myapplication.ShoppingCart.ShoppingCartActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.recyclerview_category.*
import kotlinx.android.synthetic.main.recyclerview_category.view.*


class CategoryFragment : Fragment(R.layout.recyclerview_category) {
    private lateinit var mAuth: FirebaseAuth
    private var mAuthListener: FirebaseAuth.AuthStateListener? = null
    var user: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mAuth = FirebaseAuth.getInstance()


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val intent = activity!!.intent
        var bundle : Bundle?=intent.extras
        val getString = bundle!!.getString(CardViewController.ToolBarTitle)
        view.toolbar_category.title = getString

        CategoryContoller(view)

        bttn_to_shoppingcart_from_category.setOnClickListener {
            if (mAuth.currentUser != null) {
                val intent = Intent(it.context, ShoppingCartActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(it.context, LoginActivity::class.java)
                startActivity(intent)

            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (mAuthListener != null) {
            mAuth.addAuthStateListener(mAuthListener!!)
        }
    }

    override fun onStart() {
        super.onStart()
        mAuthListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            user = firebaseAuth.currentUser
            ///updateUI(user)
        }
    }

    override fun onStop() {
        super.onStop()
        FirebaseAuth.getInstance().removeAuthStateListener { this }
    }


}
