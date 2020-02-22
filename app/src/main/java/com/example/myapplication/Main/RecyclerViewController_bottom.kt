package com.example.myapplication.Main


import android.util.Log
import android.view.*
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.example.myapplication.FireBase.FireBaseCollector
import com.example.myapplication.FireBase.ItemInfo_Firebase_Model
import com.example.myapplication.R
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.cardview_main_bottom.view.*
import kotlinx.android.synthetic.main.content_main.view.*
import java.util.*


class RecyclerViewController_bottom(val view: View, val mNestedScrollView : NestedScrollView) {
    private var itemList = mutableListOf<ItemInfo_Firebase_Model>()
    private val databaseHelper = FireBaseCollector()
    private var mDownY: Int = 0
    private var isLoading = false
    private var itemList_toBeDisplayed = mutableListOf<ItemInfo_Firebase_Model>()
    //private var itemList_refresh_isDisplayed = mutableListOf<ItemInfo_Firebase_Model>()
    private var itemList_refresh_beingDisplayed = mutableListOf<ItemInfo_Firebase_Model>()
    private var adapter : RecyclerviewAdapter_bottom? = null

    init {
        view.apply {
            databaseHelper.readAllData(object :
                FireBaseCollector.DataStatus {
                override fun DataIsLoaded(theItemListModel: MutableList<ItemInfo_Firebase_Model>) {
                    itemList.addAll(theItemListModel)
                    initialiseItemList(2)
                }
            })
            recyclerview_main_bottom.layoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

           refreshItemList_dragUp(2)
        }
    }



    fun checkDataIfAvailable(): Boolean {
        view.apply {
            if (itemList.size > 0) {
                return true
            } else if (itemList.size <= 0) {
                textView_bottom.text =
                    "Data retrive failed : itemList = ${itemList.size} \n itemList_refresh_isDisplayed = ${itemList.size}"
                textView_bottom.visibility = View.VISIBLE
            }
            return false
        }
    }

    fun initialiseItemList(size: Int) {
        if (!checkDataIfAvailable()) {
            return
        }
        itemList_refresh_beingDisplayed.addAll(itemList)

        for (i in 0 until size) {
            if (itemList_refresh_beingDisplayed.size >= 1) {
                var randomItem = Math.random() * itemList_refresh_beingDisplayed.size
                itemList_toBeDisplayed.add(itemList_refresh_beingDisplayed[randomItem.toInt()])
                itemList_refresh_beingDisplayed.removeAt(randomItem.toInt())
            }
        }
        adapter = RecyclerviewAdapter_bottom(itemList_toBeDisplayed)
        view.recyclerview_main_bottom.adapter = adapter
    }

    fun refreshItemList_dragUp(sizeAdd: Int) {
            view.apply {
                recyclerview_main_bottom.setOnTouchListener { v, event ->
                   var itemList_toBeAdded = mutableListOf<ItemInfo_Firebase_Model>()
                    //val touchSlop = ViewConfiguration.get(context).getScaledTouchSlop()
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            mDownY = event.rawY.toInt()
                        }
                        MotionEvent.ACTION_MOVE -> {
                            if (mDownY != 0 && mDownY > event.rawY.toInt()) {
                                if (!isLoading && !mNestedScrollView.canScrollVertically(1)) {
                                    textView_bottom.visibility = View.GONE
                                    isLoading(true)
                                    for (i in 0 until sizeAdd) {
                                        val random = Random()
                                        if (itemList_refresh_beingDisplayed.size >= 1) {
                                            var randomItem =
                                                random.nextInt(itemList_refresh_beingDisplayed.size)
                                            itemList_toBeAdded.add(
                                                itemList_refresh_beingDisplayed[randomItem]
                                            )
                                            itemList_refresh_beingDisplayed.removeAt(randomItem)
                                        }
                                    }
                                    v.postDelayed({
                                        adapter!!.setData(itemList_toBeAdded)
                                        isLoading(false)
                                        if (itemList_refresh_beingDisplayed.size == 0) {
                                            textView_bottom.visibility = View.VISIBLE
                                        }
                                    }, 500)
                                }
                            }
                            mDownY = 0
                        }
                    }
                    true
                }
            }
        }


        fun isLoading(recyclerViewisLoading: Boolean) {
            view.apply {
                if (recyclerViewisLoading) {
                    isLoading = recyclerViewisLoading
                    progressBar.visibility = View.VISIBLE
                } else {
                    isLoading = false
                    progressBar.visibility = View.GONE
                }
            }
        }


            class RecyclerviewAdapter_bottom(private val itemList_refreshModels: MutableList<ItemInfo_Firebase_Model>) :
                RecyclerView.Adapter<RecyclerviewAdapter_bottom.TheView>() {

                override fun getItemCount(): Int {
                    return itemList_refreshModels.size
                }


                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TheView =
                    TheView(
                        LayoutInflater.from(parent.context).inflate(
                            R.layout.cardview_main_bottom, parent, false
                        )
                    )

                override fun onBindViewHolder(holder: TheView, position: Int) {
                    holder.bindItemList(itemList_refreshModels[position])
                    /***for retrofit
                    holder.txtName.setText(list.get(position).getName())
                    holder.txtEmail.setText(list.get(position).getEmail())
                    Picasso.get().load(itemList.get(position).).into(image_swipe)*/
                }


                fun setData(itemList: MutableList<ItemInfo_Firebase_Model>) {
                    val diffCallback =
                        RecyclerView_bottom_DiffCallback(itemList_refreshModels, itemList)
                    val diffResult = DiffUtil.calculateDiff(diffCallback)
                    //itemList_refreshModels.clear()
                    itemList_refreshModels.addAll(itemList)
                    diffResult.dispatchUpdatesTo(this)
                }


                class TheView(val view: View) : RecyclerView.ViewHolder(view) {
                    fun bindItemList(itemListModel: ItemInfo_Firebase_Model) {
                        view.product_name_main.text = itemListModel.name
                        view.product_price_main.text = "$" + itemListModel.price.toString()
                        Glide.with(view.context).load(itemListModel.url_forRecyclerview)
                            .into(view.image_swipe)
                    }
                }
            }


        class RecyclerView_bottom_DiffCallback(
            private val oldList: MutableList<ItemInfo_Firebase_Model>,
            private val newList: MutableList<ItemInfo_Firebase_Model>
        ) : DiffUtil.Callback() {


            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition].unicode == newList[newItemPosition].name
            }

            override fun getOldListSize(): Int {
                return oldList.size
            }

            override fun getNewListSize(): Int {
                return newList.size
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList.get(oldItemPosition) == newList.get(newItemPosition)
            }

        }
    }
