package com.example.myapplication.Login

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.myapplication.R

class LoginActivity : AppCompatActivity() {


    private var mForgotResetPasswordFragment  =  ForgotResetPasswordFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_login_host)
        if(savedInstanceState!=null){
            mForgotResetPasswordFragment = supportFragmentManager.getFragment(savedInstanceState, KeyForgotResetPasswordFragment) as ForgotResetPasswordFragment
        }
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        supportFragmentManager.putFragment(outState, KeyForgotResetPasswordFragment, mForgotResetPasswordFragment)
    }

    companion object {val TAG = "LoginActivity"
    val KeyForgotResetPasswordFragment = "ForgotResetPasswordFragment" }

}

