package com.scnu.easygo.channel_about;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.scnu.easygo.R;

import java.util.List;



public class ChannelListAdatper extends RecyclerView.Adapter<ChannelListAdatper.MyViewHolder> implements View.OnClickListener {
    private List<UserChannel> channeltLists;
    private Context context;

    private UserChannel listBean=null;
    //声明自定义的监听接口

//    private OnRecyclerItemClickListener monItemClickListener;

    //    //提供set方法供Activity或Fragment调用
//    public void setRecyclerItemClickListener(OnRecyclerItemClickListener listener){
//        monItemClickListener=listener;
//    }
    //定义接口
    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    //声明自定义的监听接口
    private OnItemClickListener monItemClickListener=null;
    //提供set方法
    public void setonItemClickListener(OnItemClickListener listener){
        this.monItemClickListener=listener;
    }



    public ChannelListAdatper(List<UserChannel> channeltLists, Context context) {
        this.channeltLists = channeltLists;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.channel_list_item, viewGroup, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int i) {
        listBean = channeltLists.get(i);
        viewHolder.channelName.setText(listBean.getPd_name());
        viewHolder.channelID.setText(listBean.getPd());
        //将position保存在itemView的Tag中，以便点击时进行获取
        viewHolder.itemView.setTag(i);
        if(listBean.getPassword()==null||"".equals(listBean.getPassword())){
            viewHolder.lockChannel.setVisibility(View.GONE);
        }else{
            viewHolder.unlockChannel.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return channeltLists.size();
    }

    @Override
    public void onClick(View view) {
        if (channeltLists!=null){

            //这里使用getTag方法获取position

            monItemClickListener.onItemClick(view,(int)view.getTag());
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        //布局中的id
        TextView channelName;
        TextView channelID;
        ImageView lockChannel;
        ImageView unlockChannel;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            channelID=itemView.findViewById(R.id.channelID);
            channelName=itemView.findViewById(R.id.channelName);
            lockChannel=itemView.findViewById(R.id.lockChannel);
            unlockChannel=itemView.findViewById(R.id.unlockChannel);


//            lockChannel.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                }
//            });
//            unlockChannel.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                }
//            });

//            //将监听传递给自定义接口
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (monItemClickListener!=null){
//                        monItemClickListener.onItemClick(getAdapterPosition(),ticketLists);
//                    }
//                }
//            });

        }
    }
}
