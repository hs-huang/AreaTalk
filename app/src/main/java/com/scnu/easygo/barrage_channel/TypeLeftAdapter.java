package com.scnu.easygo.barrage_channel;

/**
 * Created by qiangc7 on 2019/7/5.
 */
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.scnu.easygo.R;

import java.util.ArrayList;
import java.util.List;

public class TypeLeftAdapter extends BaseAdapter{
    private Context mContext;
    private int mSelect = 0;//选中项

    public List<String> titles=new ArrayList<String>();//频道名称
    public List<String> pdId=new ArrayList<String>();//频道Id

    public TypeLeftAdapter(Context mContext,List<String> list,List<String> list1) {
        this.mContext = mContext;
        this.pdId=list;
        this.titles=list1;
    }

    public List<String> getTitles() {
        return titles;
    }

    //添加频道
    public void addChannel(String pId,String pname) {
        this.pdId.add(pId);
        this.titles.add(pname);
        notifyDataSetChanged();
    }
    //删除频道
    public void cutChannel(int position){
        this.pdId.remove(position);
        this.titles.remove(position);
        notifyDataSetChanged();
    }

    public int getIndex(String pdid){
        return pdId.indexOf(pdid);
    }

    public String getDeletePdId(int position){
        return pdId.get(position);
    }

    //获取当前频道名称
    public String getPdName() {
        return titles.get(mSelect);
    }
    //获取当前频道id
    public String getPdId() {
        return pdId.get(mSelect);
    }
    public int getmSelect() { return mSelect; }
    @Override
    public int getCount() {
        return titles.size();
    }

    @Override
    public Object getItem(int position) {
        return titles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_type, null);
            holder = new ViewHolder();
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_title);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_name.setText(titles.get(position));

        if (mSelect == position) {
            convertView.setBackgroundResource(R.drawable.type_item_background_selector);  //选中项背景
            convertView.getBackground().setAlpha(50);
            holder.tv_name.setTextColor(Color.parseColor("#fd3f3f"));
        } else {
            convertView.setBackgroundResource(R.drawable.bg2);  //其他项背景
            convertView.getBackground().setAlpha(50);
            holder.tv_name.setTextColor(Color.parseColor("#323437"));
        }
        return convertView;
    }

    public void changeSelected(int positon) { //刷新方法
        if (positon != mSelect) {
            mSelect = positon;
            notifyDataSetChanged();
        }
    }


    static class ViewHolder {
        private TextView tv_name;
    }

}
