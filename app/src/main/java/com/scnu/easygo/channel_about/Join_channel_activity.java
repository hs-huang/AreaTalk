package com.scnu.easygo.channel_about;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.scnu.easygo.barrage_channel.JDBC.JDBCUtils;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.scnu.easygo.MainActivity;
import com.scnu.easygo.R;
import com.scnu.easygo.login.LoginActivity;
import com.scnu.easygo.userID.UserID;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Join_channel_activity extends AppCompatActivity implements View.OnClickListener{
    EditText editText_join_channel_num;
    EditText editText_join_channel_password;
    Button button_join_channel;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_channel);
        editText_join_channel_num=(EditText) findViewById(R.id.join_channel_num);;
        editText_join_channel_password=(EditText) findViewById(R.id.join_channel_password);
        button_join_channel=(Button) findViewById(R.id.join_channel_Button);
        button_join_channel.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.join_channel_Button:
                join_channel();
                break;

        }
    }


    public void join_channel() {
        final String channel_num2=editText_join_channel_num.getText().toString().trim();
        final String channel_password2=editText_join_channel_password.getText().toString().trim();
        System.out.println(channel_num2+"channel_num2");
        System.out.println(channel_password2+"channel_password2");

        new Thread() {
            public void run() {
                if(channel_if_exit(channel_num2,channel_password2)==false){
                      runOnUiThread(
                           new Runnable() {
                              @Override
                              public void run() {
                                    Toast.makeText(Join_channel_activity.this,"请输入正确频道信息！",Toast.LENGTH_SHORT).show();
                             }
                      });
                return;
            }
            else{
                add_user_to_pd(channel_num2);

            }
                runOnUiThread(
                        new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Join_channel_activity.this,"创建成功！",Toast.LENGTH_SHORT).show();
                            }
                        });
                 Intent starter = new Intent(Join_channel_activity.this, MainActivity.class);
                 startActivity(starter);
            }
        }.start();
    }

    /*
     * 时间：2020年3月20日22:28:04
     * 作者：黄鑫峻
     * 根据频道号获得频道名称
     * */
    public String get_pdname(String channel_num) {
        String a = null;
/*
*
* 获取不到频道号可能是没加线程的原因！！！！！，但是实际上不是！！！
*
* */
//        int i=0;
        try {
            Connection connection = JDBCUtils.getConnection();
            String sql = "select pd_name from  pd where pd=?";
            java.sql.PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, channel_num);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("sql=" + preparedStatement.toString());//打印sql语句

            int col = resultSet.getMetaData().getColumnCount();
            while (resultSet.next()) {
                for (int i = 1; i <= col; i++) {
//                    System.out.println(resultSet.getString(i));
                    a = (String) resultSet.getString(i);
                }
            }

//            System.out.println("resultSet.next()"+a);

//            i=pstmt.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return a;
    }


    /**
     * 判断输入的频道和密码是否对应
     *时间：2020年3月21日10:03:59
     * 作者：黄鑫峻
     * */
    public boolean channel_if_exit(String a,String b){
        String channel_num=a;
        String channel_password = b;
        boolean channel_exit=false;
        try {
            Connection connection = JDBCUtils.getConnection();
            String sql = "select * from pd where pd = ? and password= ? ";//原来这里写成了中文符号，难怪一直报错！
            java.sql.PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, channel_num);
            preparedStatement.setString(2, channel_password);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){    // 依次取出数据
                String pdname = resultSet.getString("pd_name") ;    // 取出name列的内容
                System.out.print("姓名：" + pdname + "；") ;
                System.out.println("-----------------------") ;
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



    /*
     * 将用户加入频道-用户关系
     * 时间：2020年3月21日09:59:45
     * */
    public void add_user_to_pd(String a){
        final String channel_num2=a;
        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                ResultSet resultSet = null;
                try {
                    Connection connection = JDBCUtils.getConnection();
                    PreparedStatement pstmt;
                    String pd_name= get_pdname(channel_num2);
                    String sql = "insert into userpd (userid,pd,pd_name) values (?,?,?)";
                    pstmt = (PreparedStatement) connection.prepareStatement(sql);
                    //"INSERT INTO pd (pd,pd_name,password) VALUES (channel_name,channel_num,editText_channel_passsword)"
                    pstmt.setString(1, UserID.userID);
                    pstmt.setString(2, channel_num2);
                    pstmt.setString(3, pd_name);
                    i = pstmt.executeUpdate();
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

}







