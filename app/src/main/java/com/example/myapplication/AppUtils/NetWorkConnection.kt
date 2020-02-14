package com.example.myapplication.AppUtils

import android.content.Context
import android.net.ConnectivityManager
import android.widget.Toast

object NetWorkConnection {
    fun checkNetWorkStatus(context: Context)
    {
        val connMgr =
            context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connMgr.activeNetwork
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        if (activeNetwork != null) {
            // Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "No Network Connection", Toast.LENGTH_LONG).show()
        }
    }
}