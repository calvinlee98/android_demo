package com.example.android_demo_application.fragment_adapters;

import android.app.Application;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_demo_application.MyApplication;
import com.example.android_demo_application.R;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class WodeAdapter extends RecyclerView.Adapter<WodeAdapter.WodeViewHolder> {
    List<Wode_Item>list = new ArrayList<>();
    public WodeAdapter(){
        list.add(new Wode_Item(R.drawable.wodejifen,"我的积分"));
        list.add(new Wode_Item(R.drawable.wodefenxiang,"我的分享"));
        list.add(new Wode_Item(R.drawable.wodeshoucang,"我的收藏"));
        list.add(new Wode_Item(R.drawable.shaoshouyuedu,"稍后阅读"));
        list.add(new Wode_Item(R.drawable.yuedulishi,"阅读历史"));
        list.add(new Wode_Item(R.drawable.kaiyuanxiangmu,"开源项目"));
        list.add(new Wode_Item(R.drawable.guanyuzuozhe,"关于作者"));
        list.add(new Wode_Item(R.drawable.xitongshezhi,"系统设置"));

    }
    @NonNull
    @Override
    public WodeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.wode_item,parent,false);
        return new WodeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WodeViewHolder holder, int position) {
        Wode_Item item = list.get(position);
        holder.imageView.setImageResource(item.imageSource);
        holder.textView.setText(item.text);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }





    public static class WodeViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;
        public WodeViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.left_icon);
            textView = itemView.findViewById(R.id.mid_text);
        }
    }
}
class Wode_Item{
    int imageSource;
    String text;
    Wode_Item(int imageSource,String text){
        this.imageSource = imageSource;
        this.text = text;
    }
}