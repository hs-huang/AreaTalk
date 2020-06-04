package com.scnu.easygo.barrage_channel;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.scnu.easygo.MainActivity;
import com.scnu.easygo.R;

import com.scnu.easygo.channel_about.Search_Channel_Activity;
import com.scnu.easygo.friend_info.Look_friend_info_activity;
import com.scnu.easygo.friend_info.Look_friend_info_activity2;
import com.scnu.easygo.userID.UserID;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by qiangc7 on 2019/7/6.
 */

public class TypeRightAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static Context mContext;
    private final LayoutInflater mLayoutInflater;

    public List<String> userId=new ArrayList<String>();
    public List<String> namesArray=new ArrayList<String>();
    public List<String> contents=new ArrayList<String>();


    public void setContents(List<String> contents) {
        this.contents = contents;
    }
    public void setNames(List<String> names) {

        List<String> uname=new ArrayList<String>();
        List<String> uid=new ArrayList<String>();
        for(int i=0;i<names.size();i++){
            String ss=names.get(i);
            String[] s=ss.split("\\^");
            System.out.println(s[0]);
            System.out.println(s[1]);
            uid.add(s[0]);
            uname.add(s[1]);
        }
        this.userId=uid;
        this.namesArray = uname;
    }

    class ContentViewHolder extends RecyclerView.ViewHolder{
        public TextView names;
        public TextView contents;
        public ContentViewHolder(View itemView){
            super(itemView);
            names=(TextView) itemView.findViewById(R.id.names);
            names.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            names.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    //点击自己没反应
                    if(UserID.userID.equals(userId.get(getAdapterPosition()))){
                        return;
                    }
                    Intent user = new Intent(mContext, Search_Channel_Activity.class);
                    user.putExtra("userID",userId.get(getAdapterPosition()));
                    user.putExtra("username",namesArray.get(getAdapterPosition()));
                    mContext.startActivity(user);
                    //Toast.makeText(mContext,"第"+getAdapterPosition()+"用户："+namesArray.get(getAdapterPosition()), Toast.LENGTH_SHORT).show();
                }
            });
            contents=(TextView) itemView.findViewById(R.id.contents);
        }
        public void setData(String name,String content){
            names.setText(name);
            contents.setText(content);
        }
    }
    public TypeRightAdapter(Context mContext){
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContentViewHolder(mLayoutInflater.inflate(R.layout.item_content, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ContentViewHolder cholder=(ContentViewHolder)holder;
        cholder.setData(namesArray.get(position),contents.get(position));
    }

    @Override
    public int getItemCount() {
        return namesArray.size();
    }

    public void addName(int position, String name,String id) {
        namesArray.add(position, name);
        userId.add(id);
        notifyItemInserted(position);//通知演示插入动画
        notifyItemRangeChanged(position,namesArray.size()-position);//通知数据与界面重新绑定
    }

    public void addContent(int position, String content) {
        contents.add(position, content);
        notifyItemInserted(position);//通知演示插入动画
        notifyItemRangeChanged(position,contents.size()-position);//通知数据与界面重新绑定
    }
    public int getLocation(){
        return namesArray.size();
    }

    public void addMessage(String name,String content){

    }

}
