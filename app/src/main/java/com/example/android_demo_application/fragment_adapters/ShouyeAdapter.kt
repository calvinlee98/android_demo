package com.example.android_demo_application.fragment_adapters

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android_demo_application.R
import com.example.android_demo_application.activities.DetailActivity
import com.example.android_demo_application.animators.AnimatorHelper
import com.example.android_demo_application.fragments.ShouyeBannerFragment
import com.example.android_demo_application.utities.ShouyeItem
import kotlinx.android.synthetic.main.shouye_item_1.view.*
import kotlinx.android.synthetic.main.shouye_item_2.view.*

class ShouyeAdapter(private val fragmentManager: FragmentManager,
                    private val itemList: List<ShouyeItem>,
                    private val fragmentList: List<ShouyeBannerFragment>,
                    private val favoriteSet: Set<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class BannerViewHolder(view: View) : RecyclerView.ViewHolder(view) {}

    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {}

    override fun getItemViewType(position: Int) = if (position == 0) 0 else 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = if (viewType == 0) {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.shouye_item_1, parent, false)
        BannerViewHolder(view)
    } else {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.shouye_item_2, parent, false)

        val imageButton = view.likeBtn
        imageButton.setOnClickListener {
           // AnimatorHelper.playFirstAnimator(imageButton)
        }

        ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is BannerViewHolder) {
            // TODO: when there is no banner pics
            val adapter = ShouyeBannerAdapter(fragmentManager, fragmentList)
            holder.itemView.bannerViewPager.adapter = adapter
        } else {
            holder.itemView.apply {
                authorText.text = itemList[position-1].author
                publishTimeText.text = itemList[position-1].publishTime
                titleText.text = itemList[position-1].title
                contentText.text = itemList[position-1].content
                typeText.text = itemList[position-1].superChapterName
                setOnClickListener {
                    val intent = Intent(context, DetailActivity::class.java)
                    intent.putExtra("url", itemList[position-1].link)
                    context.startActivity(intent)
                }
            }
            // TODO: favorite list
            if (favoriteSet.contains(itemList[position-1].articleId)) {
                Log.d("favorite", "${itemList[position-1].articleId}")
            }
        }
    }

    override fun getItemCount() = itemList.size + 1
}

class ShouyeBannerAdapter(fragmentManager: FragmentManager, private val bannerFragmentList: List<Fragment>) : FragmentStatePagerAdapter(fragmentManager) {
    override fun getItem(position: Int): Fragment {
        return bannerFragmentList[position]
    }
    override fun getCount() = bannerFragmentList.size
}