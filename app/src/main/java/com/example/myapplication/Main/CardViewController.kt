package com.example.myapplication.Main

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.Category.CategoryActivity
import com.example.myapplication.R
import kotlinx.android.synthetic.main.cardview_main.view.*
import kotlinx.android.synthetic.main.content_main.view.*


class CardViewController(val view:View) {

    val bag = view.context.getDrawable(R.drawable.bag)
    val bag1 = "https://firebasestorage.googleapis.com/v0/b/myproject-891fa.appspot.com/o/MyApplication%2Fproduct_image%2Fbag%2Fbag2_white%2Fbag2_white_display_500X500.jpg?alt=media&token=85c4be93-cc14-47b6-bda3-a3cec7a20f67"
   val shoe = "https://firebasestorage.googleapis.com/v0/b/myproject-891fa.appspot.com/o/MyApplication%2Fproduct_image%2Fshoe%2Fshoe1_display_500X500.jpg?alt=media&token=975e262b-2f2d-4c9d-97be-3cf530aac950"
    val clothe = "https://firebasestorage.googleapis.com/v0/b/myproject-891fa.appspot.com/o/MyApplication%2Fproduct_image%2Fjacket%2Fjacket1%2FJacket1_display_500X500.jpg?alt=media&token=511157de-2327-4d67-8bd4-b46de159b86b"

    var defaultImages: ArrayList<Any?> = arrayListOf(
        bag, clothe, shoe , bag1
    )

    init {
        view.apply {
           recyclerview_horizontal_slide.adapter = Cardview_RecyclerviewAdapter(defaultImages)
           recyclerview_horizontal_slide.layoutManager =
                LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        }
    }

    class Cardview_RecyclerviewAdapter(val images: ArrayList<Any?>) :
        RecyclerView.Adapter<CardViewHolder>() {

        override fun getItemCount(): Int {
            return images.size
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder =
            CardViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.cardview_main,
                    parent,
                    false
                )
            )

        override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
            holder.passingCategory(position)
            holder.itemView.also {
                Glide.with(it.context).load(images[position]).into(it.image_category)

                it.category_name.setText(
                    when (position) {
                        0 -> "New Arrivals"
                        1 -> "Clothings"
                        2 -> "Shoes"
                        3 -> "Bags"
                        else -> null
                    }
                )

                /*   var bundle = Bundle()
                bundle.putString("category", "1")
                bundle.putString("category", "2")
                bundle.putString("category", "3")
                bundle.putString("category", "4")*/
            }
        }
    }

        class CardViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
            fun passingCategory(position : Int) {
                view.cardview_main.setOnClickListener {
                    var intent = Intent(it.context, CategoryActivity::class.java)
                    when (position) {
                        0 -> intent.putExtra(category, "4")
                        1 -> intent.putExtra(category, "2")
                        2 -> intent.putExtra(category, "3")
                        3 -> intent.putExtra(category, "1")
                    }
                    view.context.startActivity(intent)
                }
            }
        }

        companion object {
            const val category = "category"
        }
    }

