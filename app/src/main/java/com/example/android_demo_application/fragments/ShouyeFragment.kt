package com.example.android_demo_application.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android_demo_application.R
import com.example.android_demo_application.fragment_adapters.ShouyeAdapter
import com.example.android_demo_application.fragment_adapters.ShouyeItem
//import kotlinx.android.synthetic.main.shouye.view.*

class ShouyeFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.shouye, container, false).also {
            recyclerView = it.findViewById(R.id.shouyeRecyclerView)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager
        val adapter = ShouyeAdapter(childFragmentManager, getArticles())
        recyclerView.adapter = adapter
    }

    private fun getArticles() : List<ShouyeItem> {
        return listOf(ShouyeItem("k1t", "2020/11/30", "Test", "Test", "Test", "Test"))
    }
}