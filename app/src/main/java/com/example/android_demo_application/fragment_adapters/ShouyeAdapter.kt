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
import androidx.viewpager.widget.ViewPager
import com.example.android_demo_application.MyApplication
import com.example.android_demo_application.R
import com.example.android_demo_application.activities.DetailActivity
import com.example.android_demo_application.animators.MyButtonAnimatorHelper
import com.example.android_demo_application.fragments.ShouyeBannerFragment
import com.example.android_demo_application.entities.ShouyeItem
import com.example.android_demo_application.utils.HttpUtils
import kotlinx.android.synthetic.main.shouye_item_1.view.*
import kotlinx.android.synthetic.main.shouye_item_2.view.*
import java.text.SimpleDateFormat

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
                // 因为favoriteSet已经在发送时修改过了，所以handler中只用处理ui逻辑
                addFavoriteSuccess -> {
                    val btn = msg.obj as ImageButton
                    MyButtonAnimatorHelper.addToFavorite(btn)
                }

                addFavoriteFail -> {
                    val btn = msg.obj as ImageButton
                    Toast.makeText(btn.context, "add favorite fail", Toast.LENGTH_SHORT).show()
                }

                removeFavoriteSuccess -> {
                    val btn = msg.obj as ImageButton
                    MyButtonAnimatorHelper.removeFromFavorite(btn)
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
            val adapter = ShouyeBannerAdapter(fragmentManager, fragmentList)
            holder.itemView.bannerViewPager.also {
                it.adapter = adapter
                it.autoScroll(3000)
            }
        } else {
            val currItem = itemList[position-1]
            holder.itemView.apply {
                authorText.text = currItem.author
                publishTimeText.text = currItem.publishTime
                titleText.text = currItem.title
                contentText.text = currItem.content
                typeText.text = currItem.superChapterName
                setOnClickListener {
                    // 点击打开详情页，将相关信息发送到detail activity
                    val intent = Intent(context, DetailActivity::class.java)
                    val flag = favoriteSet.contains(currItem.articleId)
                    intent.putExtra("url", currItem.link)
                    intent.putExtra("flag", flag) // 用来标记文章收藏与否
                    intent.putExtra("articleId", currItem.articleId)
                    context.startActivity(intent)
                }
            }

            // 设置收藏图标初始图案
            if (favoriteSet.contains(currItem.articleId)) {
                holder.itemView.likeBtn.setImageResource(R.drawable.hard_heart)
            } else {
                holder.itemView.likeBtn.setImageResource(R.drawable.empty_heart)
            }

            holder.itemView.likeBtn.setOnClickListener {
                if (currItem.articleId != null) {
                    // 按钮防抖
                    it.isClickable = false

                    val message = Message()
                    message.obj = holder.itemView.likeBtn
                    if (favoriteSet.contains(currItem.articleId)) {
                        // 发送添加/移除收藏请求，并根据接口的返回值交由handler处理后续逻辑
                        MyApplication.pools.execute {
                            val ret = HttpUtils.cancelLike(currItem.articleId)
                            if (ret) {
                                favoriteSet.remove(currItem.articleId) // 在这里处理favoriteSet是因为将articleId用message发送到handler显得有点麻烦
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
                            if (ret) {
                                favoriteSet.add(currItem.articleId)
                                message.what = addFavoriteSuccess
                            } else {
                                Log.d("favorite", "fail")
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

    private fun ViewPager.autoScroll(interval: Long) {
        val handler = Handler()
        var scrollPosition = 0

        val runnable = object : Runnable {
            override fun run() {
                val count = adapter?.count ?: 0
                if (count != 0) {
                    setCurrentItem(scrollPosition++ % count, true)
                    handler.postDelayed(this, interval)
                }
            }
        }

        addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageSelected(position: Int) {
                scrollPosition = position + 1
            }

            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }
        })

        handler.post(runnable)
    }
}

class ShouyeBannerAdapter(fragmentManager: FragmentManager, private val bannerFragmentList: List<Fragment>) : FragmentStatePagerAdapter(fragmentManager) {
    override fun getItem(position: Int) = bannerFragmentList[position]

    override fun getCount() = bannerFragmentList.size
}