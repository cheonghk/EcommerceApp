package com.example.myapplication.Main

import android.database.DataSetObservable
import android.database.DataSetObserver
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_UP
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.R
import java.util.*
import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.main.imageslide_main.*
import kotlinx.android.synthetic.main.imageslide_main.view.*


class LoopingViewpager2Controller(private val itemList: ArrayList<String>, val view:View
                                  ) {

    private val delay: Long = 3500
    private val period: Long = 3500
    val handler = Handler()
    val slideTimer = Timer()
    val initPage = itemList.size * 30

    init {
        view.apply {
            viewPager2.adapter = LoopingPagerAdapter(itemList)
            dotsIndicator.setViewPager(viewPager2, itemList)
            viewPager2.adapter?.registerAdapterDataObserver(dotsIndicator.adapterdataObserver)
            // viewPager2.setCurrentItem(initPage)
            setOnToughListener()
        }
    }


    fun setOnToughListener() = view.apply {
        viewPager2.setOnTouchListener { _, event ->
            if (event.action == ACTION_DOWN) {
                //stop autoSlide
            } else if (event.action == ACTION_UP) {
                //keep running autoslide
            }
            true
        }
    }



    class LoopingPagerAdapter(private val itemList: ArrayList<String>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun getItemCount(): Int {
            return Int.MAX_VALUE
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            PagerVH(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.custom_layout,
                    parent,
                    false
                )
            )


        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            (holder.itemView as ImageView).also {
                Picasso.get().load(itemList[position % itemList.size]).into(it)
                }

            /*       it.setOnTouchListener(object : View.OnTouchListener {
                    override fun onTouch(v: View, event: MotionEvent): Boolean {
                        if (event.action == ACTION_DOWN) {
                            it.isInTouchMode

                        }
                        if (event.action == ACTION_UP) {
                            when (position) {
                                0 -> it.getContext().startActivity<login_fragment>()
                                1 -> it.getContext().startActivity<MainActivity>()
                                2 -> it.getContext().startActivity<login_fragment>()
                                3 -> it.getContext().startActivity<MainActivity>()
                                else -> it.getContext().startActivity<login_fragment>()
                            }
                        }
                        return true
                    }
                })*/
        }

        /*
                    when (position) {
                        0 -> it.getContext().startActivity<login_fragment>()
                        1 -> it.getContext().startActivity<MainActivity>()
                        2 -> it.getContext().startActivity<login_fragment>()
                        3 -> it.getContext().startActivity<MainActivity>()
                        else -> it.getContext().startActivity<login_fragment>()
                    }*/
        override fun getItemViewType(position: Int): Int {
            return super.getItemViewType(position)
        }


        class PagerVH(val view: View) : RecyclerView.ViewHolder(view)
    }
}
