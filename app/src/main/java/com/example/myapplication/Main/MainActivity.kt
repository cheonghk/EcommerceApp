package com.example.myapplication.Main

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.ui.AppBarConfiguration
import com.example.myapplication.Login.LoginActivity
import com.example.myapplication.R
import com.example.myapplication.ShoppingCart.ShoppingCartActivity
import com.example.myapplication.AppUtils.NetWorkConnection
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.appbar.*
import org.jetbrains.anko.startActivity


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    //val vb = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    //vb.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE));
    private var context: Context? = null
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var mAuth: FirebaseAuth
    private var mAuthListener: FirebaseAuth.AuthStateListener? = null
    private lateinit var loginStatusText: TextView
    private var backPress = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.context = this
        NetWorkConnection.checkNetWorkStatus(this)
        mAuth = FirebaseAuth.getInstance()

        val navigationView_header = navigationView.getHeaderView(0)
        loginStatusText = navigationView_header.findViewById(R.id.loginStatusText) as TextView

        navigationView.setNavigationItemSelectedListener(this)

        setSupportActionBar(toolbar)

        val toggle =
            ActionBarDrawerToggle(this, drawerlayout, toolbar, R.string.open, R.string.close)
        drawerlayout.addDrawerListener(toggle)
        toggle.syncState()

        swipeRefreshLayout.setColorSchemeResources(R.color.DarkGrey)
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white)

       swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
        }

       shoppingcart.setOnClickListener {
           if (mAuth.currentUser != null) {
               startActivity<ShoppingCartActivity>()
           } else {
               startActivity<LoginActivity>()
           }
       }
    }

    override fun onBackPressed() {
        if (drawerlayout.isDrawerOpen(GravityCompat.START)) {
            drawerlayout.closeDrawer(GravityCompat.START)}
        else{
            super.onBackPressed()
            return
        }
        //double click to exit app
     /*   if (backPress) {
            super.onBackPressed()
            return
        }
        backPress = true
        Toast.makeText(this, "Press return again to exit", Toast.LENGTH_SHORT).show()
        drawerlayout.closeDrawers()
        Handler().postDelayed({
            backPress = false
        }, 2000)*/
    }

    override fun onResume() {
        super.onResume()
        if (mAuthListener != null) {
            mAuth.addAuthStateListener(mAuthListener!!)
        }
    }

    override fun onStart() {
        super.onStart()
        initLoginStatus()
    }

    override fun onStop() {
        super.onStop()
        FirebaseAuth.getInstance().removeAuthStateListener { this }
    }



    fun initLoginStatus() {
        mAuthListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user: FirebaseUser? = firebaseAuth.currentUser
            updateUI(user)
        }
    }

    fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            // Name, email address, and profile photo Url
            // val name = user.displayName
            val email = user.email
            // val photoUrl = user.photoUrl
            // Check if user's email is verified
            val emailVerified = user.isEmailVerified

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            //val uid = user.uid
            loginStatusText.text = email
            loginStatusText.textSize = "20".toFloat()
            // navigationView.menu.findItem(R.id.navigation_home).isVisible = false
            navigationView.menu.findItem(R.id.navigation_logout).isVisible = true
            navigationView.menu.findItem(R.id.navigation_login).isVisible = false
        } else {
            loginStatusText.text = "Guest"
            loginStatusText.textSize = "23".toFloat()
            // navigationView.menu.findItem(R.id.navigation_home).isVisible = false
            navigationView.menu.findItem(R.id.navigation_logout).isVisible = false
            navigationView.menu.findItem(R.id.navigation_login).isVisible = true
        }
    }

    private fun signOut() {
        FirebaseAuth.getInstance().signOut()
        updateUI(null)
        Toast.makeText(this, "Signed out", Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val TAG = "Log_MainActivity"
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_home -> {
                startActivity<MainActivity>()
                finish()
                /* val changeFragment =
                     getSupportFragmentManager().beginTransaction().replace(
                         R.id.fragment_container, Fragment_nav_main()
                     )
                 changeFragment.commit()*/
            }
            R.id.navigation_login -> {
                startActivity<LoginActivity>()
            }
            R.id.navigation_logout -> {
                signOut()
            }
        }
        drawerlayout.closeDrawers()
        return true
    }

}

/*** retrofit
protected fun loadContactsData() {
val api = RetrofitClient.getApiService()
val call = api.bag
call.enqueue(object : Callback<ItemList> {
override fun onFailure(call: Call<ItemList>, t: Throwable) {
}
override fun onResponse(call: Call<ItemList>, response: Response<ItemList>) {
if (response.isSuccessful()) {
itemList = response.body()!!.bag1
}}
})

}*/















