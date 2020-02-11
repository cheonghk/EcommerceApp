package com.example.myapplication.Category

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.R
import kotlinx.android.synthetic.main.fragment_category_dialog.view.*
import kotlinx.android.synthetic.main.imageview_dialog_small.view.*



class ProductImageSlideAdapter(val itemUrlList: MutableList<String>, val hostView: View) : RecyclerView.Adapter<ProductImageSlideAdapter.ProductImageSlideViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductImageSlideViewHolder =
        ProductImageSlideViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.imageview_dialog_small,
                parent,
                false
            )
        )

    override fun getItemCount(): Int {
        return itemUrlList.size
    }

    override fun onBindViewHolder(holder: ProductImageSlideViewHolder, position: Int) {
        holder.also {
            it.bindItemList(itemUrlList.get(position))
           it.view.image_container_diglog_small.setOnClickListener {
                Glide.with(it.context).load(itemUrlList.get(position))
                        .into(hostView.imageview_categorydialog_large)
                }
            }
            if (position == 0) {
                Glide.with(holder.view.context).load(itemUrlList.get(position))
                    .into(hostView.imageview_categorydialog_large)
            }
        }

    class ProductImageSlideViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bindItemList(itemListModel: String) {
            Glide.with(view.context).load(itemListModel)
              .into(view.image_container_diglog_small)
            //Picasso.get().load(itemListModel).into(view.imageview_categorydialog_large)

        }
    }
}