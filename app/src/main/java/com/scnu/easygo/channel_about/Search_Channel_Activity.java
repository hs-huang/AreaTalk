package com.scnu.easygo.channel_about;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mysql.jdbc.Connection;
import com.scnu.easygo.R;
import com.scnu.easygo.barrage_channel.JDBC.JDBCUtils;
import com.scnu.easygo.login.LoginActivity;
import com.scnu.easygo.ui.home.HomeFragment;
import com.scnu.easygo.userID.UserID;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Search_Channel_Activity extends AppCompatActivity {
    private List<UserChannel> channelList= null;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private ChannelListAdatper adapter;
    private TextView otherUsername;
    private TextView otherUserID;

    private String userID;
    private String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search__channel);
        otherUsername=findViewById(R.id.otherUsername);
        otherUserID=findViewById(R.id.otherUserID);
        Intent intent=getIntent();
        userID=intent.getStringExtra("userID");
        username=intent.getStringExtra("username");
        System.out.println("11111111111"+userID);
        otherUserID.setText(userID);
        otherUsername.setText(username);
        channelList=getUserChannel(userID);
        setUserChannel();
    }

    /**
     * 打印Channel
     */
    public void setUserChannel(){
        new Thread(){
            public void run(){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView=findViewById(R.id.rView);
                        layoutManager=new LinearLayoutManager(Search_Channel_Activity.this);
                        recyclerView.setLayoutManager(layoutManager);
                        adapter=new ChannelListAdatper(channelList, Search_Channel_Activity.this);
                        adapter.setonItemClickListener(new ChannelListAdatper.OnItemClickListener(){
                            //                        跳转到详细信息
                            @Override
                            public void onItemClick(View view, final int position) {
                                final String[] string = new String[1];
                                new Thread(){
                                    public void run(){
                                        final UserChannel userChannel=channelList.get(position);
                                        if(getUserChannel(UserID.userID,userChannel.getPd())){
                                            runOnUiThread(
                                                    new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Toast.makeText(Search_Channel_Activity.this,"已加入",Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            return;
                                        }
                                        if(userChannel.getPassword()==null||"".equals(userChannel.getPassword())){
                                            addChannel(userChannel);

                                            runOnUiThread(
                                                    new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            UserID.homeFragment.addChannel(userChannel.getPd(),userChannel.getPd_name());
                                                            Toast.makeText(Search_Channel_Activity.this,"加入成功！",Toast.LENGTH_SHORT).show();
                                                        }
                                            });
                                        }else{
                                            runOnUiThread(
                                                    new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            final EditText inputServer = new EditText(Search_Channel_Activity.this);
                                                            final AlertDialog.Builder builder = new AlertDialog.Builder(Search_Channel_Activity.this);
                                                            builder.setTitle("频道密码").setIcon(android.R.drawable.ic_dialog_info).setView(inputServer)
                                                                    .setNegativeButton("取消", null);
                                                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    String password = inputServer.getText().toString();
                                                                    if(password!=null && !password.isEmpty())
                                                                    {
                                                                        string[0] =password;
                                                                        if(string[0].equals(userChannel.getPassword())){
                                                                            addChannel(userChannel);
                                                                            UserID.homeFragment.addChannel(userChannel.getPd(),userChannel.getPd_name());
                                                                            Toast.makeText(Search_Channel_Activity.this,"加入成功！",Toast.LENGTH_SHORT).show();
                                                                        }else{
                                                                            Toast.makeText(Search_Channel_Activity.this,"密码错误！",Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }
                                                                    else
                                                                    {
                                                                        Toast.makeText(Search_Channel_Activity.this,"密码不能为空",Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });
                                                            builder.show();
                                                        }
                                                    });

                                        }

                                    }
                                }.start();
                            }
                        } );
                        recyclerView.setAdapter(adapter);

                    }
                });

            }
        }.start();
    }
    /**
     * 查询是否已存在
     */
    public boolean getUserChannel(final String userID,final String pd) {
        final List<UserChannel> list=new ArrayList<>();
        final boolean[] isDone = new boolean[1];
        final int[] i = new int[1];
        Thread t=new Thread(new Runnable() {
            @Override
            public void run() {
                Connection connection = null;
                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;
                try {
                    connection = JDBCUtils.getConnection();
                    String sql = "select count(*) from userpd where userid=? and pd=?";
                    preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, userID);
                    preparedStatement.setString(2, pd);
                    resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {    // 依次取出数据
                        i[0] =resultSet.getInt(1);
                    }
                    isDone[0] =true;
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (resultSet != null) {
                            resultSet.close();
                        }
                        if (preparedStatement != null) {
                            preparedStatement.close();
                        }
                        if (connection != null) {
                            connection.close();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        while(isDone[0]!=true)
//            System.out.println();
        if(i[0]>0){
            return true;
        }else{
            return false;
        }
    }
    /**
     * 查询Channel
     */
    public List<UserChannel> getUserChannel(final String userID) {
        final List<UserChannel> list=new ArrayList<>();
        final boolean[] isDone = new boolean[1];
        Thread t=new Thread(new Runnable() {
            @Override
            public void run() {
                Connection connection = null;
                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;
                try {
                    connection = JDBCUtils.getConnection();
                    String sql = "select * from pd where userid=?";
                    preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, userID);
                    resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {    // 依次取出数据
                        UserChannel userChannel = new UserChannel();
                        userChannel.setPd(resultSet.getString("pd"));
                        userChannel.setPd_name((resultSet.getString("pd_name")));
                        userChannel.setPassword(resultSet.getString("password"));
                        userChannel.setUsername(resultSet.getString("username"));
                        userChannel.setUserid(userID);
                        list.add(userChannel);
                    }
                    isDone[0] =true;
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (resultSet != null) {
                            resultSet.close();
                        }
                        if (preparedStatement != null) {
                            preparedStatement.close();
                        }
                        if (connection != null) {
                            connection.close();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        while(isDone[0]!=true)
//            System.out.println();

        return list;
    }

    /**
     * 加入频道
     * @param userChannel
     */
    public void addChannel(final UserChannel userChannel){
        new Thread(new Runnable() {
            @Override
            public void run() {
                 Connection connection = null;
                 PreparedStatement preparedStatement = null;
                 ResultSet resultSet = null;
             try {
                 connection = JDBCUtils.getConnection();
                  String sql = "insert into userpd (userid,pd,pd_name) values (?,?,?)";
                 preparedStatement = connection.prepareStatement(sql);
                 preparedStatement.setString(1, UserID.userID);
                 preparedStatement.setString(2, userChannel.getPd());
                 preparedStatement.setString(3, userChannel.getPd_name());
                 preparedStatement.executeUpdate();
             } catch (SQLException e) {
                    e.printStackTrace();
             } finally {
                    try {
                       if (resultSet != null) {
                          resultSet.close();
                       }
                     if (preparedStatement != null) {
                          preparedStatement.close();
                    }
                    if (connection != null) {
                        connection.close();
                     }
                 } catch (SQLException e) {
                     e.printStackTrace();
                   }
               }
            }
        }).start();
    }
}
