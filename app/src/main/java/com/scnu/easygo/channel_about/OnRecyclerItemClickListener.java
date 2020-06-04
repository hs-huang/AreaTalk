package com.scnu.easygo.channel_about;


import java.util.List;

public interface OnRecyclerItemClickListener {

    //RecyclerView的点击事件，将信息回调给view

    void onItemClick(int Position, List<UserChannel> ticketLists);

}

