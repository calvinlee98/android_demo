package com.example.android_demo_application.fragment_adapters

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.os.Message
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
import com.example.android_demo_application.MyApplication
import com.example.android_demo_application.R
import com.example.android_demo_application.activities.DetailActivity
import com.example.android_demo_application.fragments.ShouyeBannerFragment
import com.example.android_demo_application.entities.ShouyeItem
import com.example.android_demo_application.utils.HttpUtils
import kotlinx.android.synthetic.main.shouye_item_1.view.*
import kotlinx.android.synthetic.main.shouye_item_2.view.*

class ShouyeAdapter(private val fragmentManager: FragmentManager,
                    private val itemList: List<ShouyeItem>,
                    private val fragmentList: List<ShouyeBannerFragment>,
                    private val favoriteSet: MutableSet<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class BannerViewHolder(view: View) : RecyclerView.ViewHolder(view) {}

    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {}

    private val addFavoriteSuccess = 0
    private val addFavoriteFail = 1
    private val removeFavoriteSuccess = 2
    private val removeFavoriteFail = 3
    private val handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                addFavoriteSuccess -> {
                    val btn = msg.obj as ImageButton
                    btn.setImageResource(R.drawable.hard_heart)
                }

                addFavoriteFail -> {
                    val btn = msg.obj as ImageButton
                    Toast.makeText(btn.context, "add favorite fail", Toast.LENGTH_SHORT).show()
                }

                removeFavoriteSuccess -> {
                    val btn = msg.obj as ImageButton
                    btn.setImageResource(R.drawable.empty_heart)
                }

                removeFavoriteFail -> {
                    val btn = msg.obj as ImageButton
                    Toast.makeText(btn.context, "remove favorite fail", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun getItemViewType(position: Int) = if (position == 0) 0 else 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = if (viewType == 0) {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.shouye_item_1, parent, false)
        BannerViewHolder(view)
    } else {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.shouye_item_2, parent, false)
        ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is BannerViewHolder) {
            // TODO: when there is no banner pics
            val adapter = ShouyeBannerAdapter(fragmentManager, fragmentList)
            holder.itemView.bannerViewPager.adapter = adapter
        } else {
            val currItem = itemList[position-1]
            holder.itemView.apply {
                authorText.text = currItem.author
                publishTimeText.text = currItem.publishTime
                titleText.text = currItem.title
                contentText.text = currItem.content
                typeText.text = currItem.superChapterName
                setOnClickListener {
                    val intent = Intent(context, DetailActivity::class.java)
                    val flag = favoriteSet.contains(currItem.articleId)
                    intent.putExtra("url", currItem.link)
                    intent.putExtra("flag", flag)
                    intent.putExtra("articleId", currItem.articleId)
                    context.startActivity(intent)
                }
            }
            // TODO: favorite list
            if (favoriteSet.contains(currItem.articleId)) {
                holder.itemView.likeBtn.setImageResource(R.drawable.hard_heart)
            }
            holder.itemView.likeBtn.setOnClickListener {
                if (currItem.articleId != null) {
                    it.isClickable = false
                    val message = Message()
                    message.obj = holder.itemView.likeBtn
                    if (favoriteSet.contains(currItem.articleId)) {
                        MyApplication.pools.execute {
                            val ret = HttpUtils.cancelLike(currItem.articleId)
                            if (ret == "success") {
                                favoriteSet.remove(currItem.articleId)
                                message.what = removeFavoriteSuccess
                            } else {
                                message.what = removeFavoriteFail
                            }
                            handler.sendMessage(message)
                            it.isClickable = true
                        }
                    } else {
                        MyApplication.pools.execute {
                            val ret = HttpUtils.likeArticle(currItem.articleId)
                            if (ret == "success") {
                                favoriteSet.add(currItem.articleId)
                                message.what = addFavoriteSuccess
                            } else {
                                message.what = addFavoriteFail
                            }

                            handler.sendMessage(message)
                            it.isClickable = true
                        }
                    }
                }
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