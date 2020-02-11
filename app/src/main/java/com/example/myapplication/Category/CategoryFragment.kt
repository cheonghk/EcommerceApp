package com.example.myapplication.Category


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.example.myapplication.R

class CategoryFragment : Fragment(R.layout.recyclerview_category) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        CategoryContoller(view)
    }




}
