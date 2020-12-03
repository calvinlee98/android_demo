package com.example.android_demo_application.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_demo_application.MyApplication;
import com.example.android_demo_application.R;
import com.example.android_demo_application.fragment_adapters.FavoriteArticlesAdapter;
import com.example.android_demo_application.utils.HttpUtils;
import com.example.android_demo_application.utities.ShouyeItem;

import java.util.List;


public class FavoritesActivity extends AppCompatActivity {
    private int curr_page;
    RecyclerView recyclerView;
    FavoriteArticlesAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorites);
         init();
    }
    private void init(){
        recyclerView = findViewById(R.id.rv);
        adapter = new FavoriteArticlesAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            List<ShouyeItem>list  = (List<ShouyeItem>) msg.obj;
            adapter.list = list;
            adapter.notifyDataSetChanged();
            Log.d("TAG",list.size()+"");
         //   adapter.notify();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        //每次回到前台 都应该请求第一页
        curr_page = 0;
        MyApplication.getPools().execute(() -> {
            List<ShouyeItem>list = HttpUtils.getFavorites(curr_page++);
            Message message = Message.obtain();
            message.obj = list;
            message.setTarget(handler);
            handler.sendMessage(message);
        });
    }
}

