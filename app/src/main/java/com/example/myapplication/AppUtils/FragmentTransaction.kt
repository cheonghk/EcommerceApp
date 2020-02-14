package com.example.myapplication.AppUtils

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

object FragmentTransaction : FragmentActivity() {
    private var mFragmentManager: FragmentManager? = null
    private var fragmentTransaction: FragmentTransaction? = null

    fun changeFragment(
        appCompatActivity: AppCompatActivity,
        containerId: Int,
        fragment: Fragment
    ) {
        mFragmentManager = appCompatActivity.getSupportFragmentManager()
        fragmentTransaction = mFragmentManager!!.beginTransaction()
        fragmentTransaction!!.replace(containerId, fragment)
        fragmentTransaction!!.commit()

    }
}