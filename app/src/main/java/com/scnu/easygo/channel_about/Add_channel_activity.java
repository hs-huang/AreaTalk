package com.scnu.easygo.channel_about;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.scnu.easygo.barrage_channel.JDBC.JDBCUtils;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.scnu.easygo.MainActivity;
import com.scnu.easygo.R;
import com.scnu.easygo.friend_info.Look_friend_info_activity;
import com.scnu.easygo.friend_info.Look_friend_info_activity2;
import com.scnu.easygo.login.LoginActivity;
import com.scnu.easygo.login.RegistActivity;
import com.scnu.easygo.userID.UserID;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Add_channel_activity extends AppCompatActivity implements View.OnClickListener {

    EditText editText_channel_name;
    EditText editText_channel_num;
    EditText editText_channel_password;
    Button button_add_channel;
    TextView button_join_channel;
    String channel_num_uuid = UUID.randomUUID().toString().substring(0, 8);

//    String channel_name;
//    String channel_num;
//    String channel_password;

//    private static PreparedStatement pstmt=null;
//    private static ResultSet rs=null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addgroup);
        editText_channel_name = (EditText) findViewById(R.id.new_channel_name);
//        editText_channel_num = (EditText) findViewById(R.id.new_channel_num);
        editText_channel_password = (EditText) findViewById(R.id.new_channel_password);
        button_add_channel = (Button) findViewById(R.id.add_channel_Button);
        button_add_channel.setOnClickListener(this);
        button_join_channel= findViewById(R.id.go_add_channel_Button);
        button_join_channel.setOnClickListener(this);

    }

    /**/
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_channel_Button:
                beforre_add_channel();
                break;
            case R.id.go_add_channel_Button:
                go_join_channel();
                break;

        }
    }

    /*
    * 时间：2020年3月21日09:57:48
    * 作者：黄鑫峻
    * 跳转到加入频道页面
    * */
    public void go_join_channel() {
        Intent starter = new Intent(Add_channel_activity.this, Join_channel_activity.class);
        startActivity(starter);

    }

    /*
    * 添加之前进行数据校验
    * 时间：2020年3月21日09:57:34
    * 作者：黄鑫峻
    * */
    public void beforre_add_channel() {
        final String channel_name=editText_channel_name.getText().toString().trim();
//        final String channel_num=editText_channel_num.getText().toString().trim();
        final String channel_password=editText_channel_password.getText().toString().trim();

        new Thread() {
            public void run() {
                if(channel_name==null||"".equals(channel_name)){
                    runOnUiThread(
                            new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(Add_channel_activity.this,"请填齐除密码外所有数据！",Toast.LENGTH_SHORT).show();
                                }
                            });
                    return;
                }
//                else if(channel_if_exit(channel_num)==true){
//                    runOnUiThread(
//                            new Runnable() {
//                                @Override
//                                public void run() {
//                                    Toast.makeText(Add_channel_activity.this,"频道已经存在！",Toast.LENGTH_SHORT).show();
//                                }
//                            });
//                    return;
//                }
                else{
                    add_channel(channel_name,channel_password);
                }

            }
        }.start();

    }

    /*
    * 添加频道
    * 时间：2020年3月21日00:36:07
    * */
    public void add_channel(String a,String c) {
        final String channel_name=a;
//        final String channel_num=b;
        final String channel_password=c;

        System.out.println("第一次："+channel_num_uuid);
        new Thread(new Runnable() {
                @Override
                public void run() {

                    int i=0;
                    try {
                        Connection connection= JDBCUtils.getConnection();
                        PreparedStatement pstmt;
                        String sql = "insert into pd (pd,pd_name,password,userid,username) values (?,?,?,?,?)";
//                        String sql = "insert into pd (pd,pd_name,password) values (?,?,?,?,?)";
                        pstmt = (PreparedStatement) connection.prepareStatement(sql);
//                        String channel_num_uuid = UUID.randomUUID().toString().substring(0, 8);
                        //"INSERT INTO pd (pd,pd_name,password) VALUES (channel_name,channel_num,editText_channel_passsword)"
                        pstmt.setString(1, channel_num_uuid);
                        pstmt.setString(2, channel_name);
                        pstmt.setString(3, channel_password);
                        pstmt.setString(4, UserID.userID);
                        pstmt.setString(5, UserID.userName);
                        i=pstmt.executeUpdate();

                        pstmt.close();
                        connection.close();
                    } catch (SQLException e) {
                        System.out.println("数据库插入有问题！");
                        e.printStackTrace();
                    }
                    runOnUiThread(
                            new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(Add_channel_activity.this,"创建成功！",Toast.LENGTH_SHORT).show();
                                }
                            });
                }
        }).start();
        join_channel(channel_num_uuid,channel_name);
        System.out.println("第二次："+channel_num_uuid);
        Intent starter = new Intent(Add_channel_activity.this, MainActivity.class);
        startActivity(starter);
    }

/*
* 新建频道同时加入频道
* 时间：2020年3月21日00:36:41
* */
    public void join_channel(String a,String b) {
        final String channel_num=a;
        System.out.println("第三次："+channel_num);
        final String channel_name=b;

        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                ResultSet resultSet = null;
                try {
                    Connection connection = JDBCUtils.getConnection();
                    PreparedStatement pstmt;
                    String sql = "insert into userpd (userid,pd,pd_name) values (?,?,?)";
                    pstmt = (PreparedStatement) connection.prepareStatement(sql);
                    //"INSERT INTO pd (pd,pd_name,password) VALUES (channel_name,channel_num,editText_channel_passsword)"
                    pstmt.setString(1, UserID.userID);
                    pstmt.setString(2, channel_num);
                    pstmt.setString(3, channel_name);
                    i = pstmt.executeUpdate();
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 判断频道是否已经存在
     * 时间：2020年3月21日00:37:05
     */
    public boolean channel_if_exit(String a){
        String channel_num=a;
        boolean channel_exit=false;
        try {
            Connection connection = JDBCUtils.getConnection();
            String sql = "select pd_name from pd where pd = ?";

            java.sql.PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, channel_num);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String pdname = resultSet.getString("pd_name") ;
                channel_exit=true;
                }
            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return channel_exit;
    }
}
