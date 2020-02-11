package com.example.myapplication.Category


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.FireBase.ItemInfo_Firebase_Model
import com.example.myapplication.R
import kotlinx.android.synthetic.main.fragment_category_dialog.*


class ProductImageSlideFragment(val itemList:ItemInfo_Firebase_Model) : DialogFragment() {

    var adapter : ProductImageSlideAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_category_dialog, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ProductImageSlideAdapter(itemList.url, view)
       recyclerview_diglog_small.adapter = adapter
        recyclerview_diglog_small.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
    }
}
