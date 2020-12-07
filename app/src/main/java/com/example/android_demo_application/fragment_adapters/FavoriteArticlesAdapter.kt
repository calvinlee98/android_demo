package com.example.android_demo_application.fragment_adapters

import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android_demo_application.MyApplication
import com.example.android_demo_application.R
import com.example.android_demo_application.animators.AnimatorHelper
import com.example.android_demo_application.fragment_adapters.FavoriteArticlesAdapter
import com.example.android_demo_application.utils.HttpUtils
import com.example.android_demo_application.utities.ShouyeItem
import java.util.*

class FavoriteArticlesAdapter //构造方法传入 主线程的handler
(var handler: Handler) : RecyclerView.Adapter<FavoriteArticlesAdapter.ViewHolder>() {
    @JvmField
    var list: ArrayList<ShouyeItem?> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.v(TAG, "onCreateViewHolder调用")
        val view = LayoutInflater.from(MyApplication.context).inflate(R.layout.shouye_item_2, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.v(TAG, "onBindViewHolder调用")
        val item = list[position]
        holder.author.text = item?.author
        holder.publishTime.text = item?.publishTime
        holder.title.text = item?.title
        holder.type.text = item?.superChapterName
        holder.article_id = item?.articleId
        holder.button.setBackgroundResource(R.drawable.hard_heart)
        holder.button.setOnClickListener { v: View? ->
            AnimatorHelper.playFirstAnimator(holder.button, this@FavoriteArticlesAdapter, list, position)
            MyApplication.pools.execute { holder.article_id?.let { HttpUtils.cancelLike(it) } }
        }
    }

    override fun getItemCount(): Int {
        Log.v(TAG, "getItemCount调用" + list.size)
        return list.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var article_id: String? = null
        var author: TextView
        var publishTime: TextView
        var title: TextView
        var type: TextView
        var button: ImageButton

        init {
            author = itemView.findViewById(R.id.authorText)
            publishTime = itemView.findViewById(R.id.publishTimeText)
            title = itemView.findViewById(R.id.titleText)
            type = itemView.findViewById(R.id.typeText)
            button = itemView.findViewById(R.id.likeBtn)
        }
    }

    companion object {
        private const val TAG = ".RecyclerView.Adapter"
    }

}