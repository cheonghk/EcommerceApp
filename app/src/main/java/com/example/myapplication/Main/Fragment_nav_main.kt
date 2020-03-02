package com.example.myapplication.Main

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
//import butterknife.Unbinder
import com.example.myapplication.R
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.imageslide_main.*
import kotlinx.android.synthetic.main.content_main.*


class Fragment_nav_main() : Fragment(R.layout.content_main) {

    private var mContext: Context? = null
    var images: ArrayList<String> = arrayListOf(
        "https://firebasestorage.googleapis.com/v0/b/myproject-891fa.appspot.com/o/MyApplication%2Fproduct_image%2Fbag%2Fbag1%2Fbag1_black_display_500X500.jpg?alt=media&token=32b6f8b1-abc7-4d69-b76a-2209b634594d",
        "https://firebasestorage.googleapis.com/v0/b/myproject-891fa.appspot.com/o/MyApplication%2Fproduct_image%2Fjacket%2Fjacket1%2FJacket1_display_500X500.jpg?alt=media&token=511157de-2327-4d67-8bd4-b46de159b86b",
        "https://firebasestorage.googleapis.com/v0/b/myproject-891fa.appspot.com/o/MyApplication%2Fproduct_image%2Fbag%2Fbag2_white%2Fbag2_white_display_500X500.jpg?alt=media&token=85c4be93-cc14-47b6-bda3-a3cec7a20f67",
        "https://firebasestorage.googleapis.com/v0/b/myproject-891fa.appspot.com/o/MyApplication%2Fproduct_image%2Fbag%2Fbag2_white%2Fbag2_white_display_500X500.jpg?alt=media&token=85c4be93-cc14-47b6-bda3-a3cec7a20f67"
    )

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        LoopingViewpager2Controller(images, view)
        CardViewController(view)
        RecyclerViewController_bottom(view, activity!!.findViewById(R.id.nestedScrollView))
    }

    override fun onDetach() {
        super.onDetach()
        mContext = null
    }


}

