package com.example.android_demo_application.fragment_adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_demo_application.MyApplication;
import com.example.android_demo_application.R;
import com.example.android_demo_application.utities.ShouyeItem;

import java.util.ArrayList;
import java.util.List;

public class FavoriteArticlesAdapter extends RecyclerView.Adapter<FavoriteArticlesAdapter.ViewHolder> {

    public List<ShouyeItem>list=new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.shouye_item_2,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
         ShouyeItem item = list.get(position);
         holder.author.setText(item.getAuthor());
         holder.publishTime.setText(item.getPublishTime());
         holder.title.setText(item.getTitle());
         holder.type.setText(item.getSuperChapterName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView author;
        public TextView publishTime;
        public TextView title;
        public TextView type;
        public ImageButton button;

        public ViewHolder(@NonNull View itemView) {
           super(itemView);
           author = itemView.findViewById(R.id.authorText);
           publishTime = itemView.findViewById(R.id.publishTimeText);
           title = itemView.findViewById(R.id.titleText);
           type  = itemView.findViewById(R.id.typeText);
           button = itemView.findViewById(R.id.likeBtn);
        }
    }
}
