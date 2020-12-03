package com.example.android_demo_application.fragment_adapters;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_demo_application.MyApplication;
import com.example.android_demo_application.R;
import com.example.android_demo_application.animators.AnimatorHelper;
import com.example.android_demo_application.utils.HttpUtils;
import com.example.android_demo_application.utities.ShouyeItem;

import java.util.ArrayList;
import java.util.List;

public class FavoriteArticlesAdapter extends RecyclerView.Adapter<FavoriteArticlesAdapter.ViewHolder> {

    public List<ShouyeItem>list=new ArrayList<>();

    Handler handler;
    //构造方法传入 主线程的handler
    public FavoriteArticlesAdapter(Handler handler){
        this.handler = handler;
    }
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
         holder.article_id = item.getArticleId();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{
        public String article_id;
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

           button.setOnClickListener(v -> {
               AnimatorHelper.playFirstAnimator(button);
               //取消收藏的逻辑
               Log.d("TAG","开始取消收藏"+article_id);
               MyApplication.getPools().execute(new Runnable() {
                   @Override
                   public void run() {
                       String s = HttpUtils.cancelLike(article_id);
                       Log.d("TAG",s);
                       List<ShouyeItem>list = HttpUtils.getFavorites(0);
                       Message message = Message.obtain();
                       message.setTarget(handler);
                       message.obj = list;
                       handler.sendMessage(message);
                   }
               });
           });
        }
    }
}
