package com.scnu.easygo.barrage_channel;

/**
 * Created by qiangc7 on 2019/9/19.
 */

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.scnu.easygo.MainActivity;

import java.util.List;
import java.util.Map;

/**
 * author：lmx
 * date：2018/3/7
 * description：
 */

public class ListRiskAreaListsDemoAdapter extends BaseAdapter {

    private Activity mContext;
    //单行的布局
    private int mResource;
    //列表展现的数据
    private List<? extends Map<String, ?>> mData;
    //Map中的key
    private String[] mFrom;
    //view的id
    private int[] mTo;

    //点击变色
    private int selectedPosition = -1;// 选中的位置

    /**
     * 构造方法
     * @param context
     * @param data 列表展现的数据
     * @param resource 单行的布局
     * @param from Map中的key
     * @param to view的id
     */
    public ListRiskAreaListsDemoAdapter(Activity context, List<? extends Map<String, ?>> data,
                                        int resource, String[] from, int[] to){
        mContext = context;
        mData = data;
        mResource = resource;
        mFrom = from;
        mTo = to;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    //点击变色
    public void setSelectedPosition(int position) {
        selectedPosition = position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        final ViewHolder holder;
        if(convertView == null){
            //使用自定义的list_items作为Layout
            convertView = LayoutInflater.from(mContext).inflate(mResource, parent, false);
            //使用减少findView的次数
            holder = new ViewHolder();
            holder.tvAreaItem = ((TextView) convertView.findViewById(mTo[0]));

            //设置标记
            convertView.setTag(holder);

        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        //设置数据
        final Map<String, ?> dataSet = mData.get(position);
        if (dataSet == null) {
            return null;
        }
        //获取该行数据
        final Object tvAreaItem = dataSet.get(mFrom[0]);

        holder.tvAreaItem.setText(tvAreaItem.toString());

        return convertView;
    }

    /**
     * ViewHolder类
     */
    static class ViewHolder {
        TextView tvAreaItem;//频道操作选项
    }

}