package com.example.myapplication.ShoppingCart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class RecyclerviewShoppingCartAdapter (): RecyclerView.Adapter<ShoppingCartViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingCartViewHolder =
        ShoppingCartViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.cardview_shoppingcart,
                parent,
                false
            )
        )

    override fun getItemCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(holder: ShoppingCartViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}





    class ShoppingCartViewHolder(val view : View) : RecyclerView.ViewHolder(view){



    }






