package com.example.android_demo_application.fragments

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.android_demo_application.R
import kotlinx.android.synthetic.main.shouye_banner_item.view.*

class ShouyeBannerFragment(private val bitmap: Bitmap) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.shouye_banner_item, container, false).also {
            it.imageView.setImageBitmap(bitmap)
        }
    }
}