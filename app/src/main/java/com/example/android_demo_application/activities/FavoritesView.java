package com.example.android_demo_application.activities;

import com.example.android_demo_application.utities.ShouyeItem;

import java.util.List;

public interface FavoritesView {
    //第一次进入 ui  或下拉更新
    void initRecyclerView(List<ShouyeItem> list);

    // 获取更多
    void addMoreViewsToRecyclerView(List<ShouyeItem> list);
}
