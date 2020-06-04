package com.scnu.easygo.friend_info;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mysql.jdbc.Connection;
import com.scnu.easygo.R;
import com.scnu.easygo.barrage_channel.JDBC.JDBCUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Look_friend_info_activity extends AppCompatActivity {
    String userid;
    String username;
    String email;
    StringBuffer pd_info = new StringBuffer("");
    String userName_from_qiang = "dd2ca4b9";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_info_test);


        try {
            look_info(userName_from_qiang);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        look_info2(userName_from_qiang);
//        System.out.println("------黄鑫峻------------");

    }

    public void look_info(final String userName_from_qiang ) throws InterruptedException {
        final String a = userName_from_qiang;
//        System.out.println("------黄鑫峻2------------");
        final LinearLayout ll=(LinearLayout) findViewById(R.id.friend_info);
        TextView tv1=new TextView(this);
        TextView tv2=new TextView(this);
        TextView tv3=new TextView(this);
        TextView tv4=new TextView(this);

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
        try {
            Connection connection= JDBCUtils.getConnection();
            String sql = "select userid,username,email from  user where userid=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, userName_from_qiang);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){    // 依次取出数据
                userid = resultSet.getString("userid") ;
                username = resultSet.getString("username") ;    // 取出name列的内容
                email = resultSet.getString("email") ;
//                System.out.println("黄鑫峻姓名：" + username + "；"+"id："+userid+"；邮箱"+email) ;
//                System.out.println("-----------------------") ;

            }

            resultSet.close();
            String sql2 = "select pd,pd_name from  pd where userid=?";
            PreparedStatement preparedStatement2 = connection.prepareStatement(sql2);
            preparedStatement2.setString(1, userName_from_qiang);
            ResultSet resultSet2 = preparedStatement2.executeQuery();
            while(resultSet2.next()){    // 依次取出数据

                String pd_id = resultSet2.getString("pd") ;    // 取出name列的内容
                String pd_name = resultSet2.getString("pd_name") ;
                pd_info.append("频道号:");
                pd_info.append(pd_id );
                pd_info.append(" 频道名:");
                pd_info.append(pd_name);
                pd_info.append("\r\n");

                System.out.println(pd_info);
//                System.out.println("黄鑫峻频道id：" + pd_id + "；"+"频道名"+pd_name) ;
//                System.out.println("-----------------------") ;
            }



            resultSet2.close();

            preparedStatement.close();
            preparedStatement2.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
            }
        });

        t1.start();
        t1.join();
//        boolean a1 = true;
//        while(a1){
//            if(userid!=null&&username!=null&&email!=null&&pd_info!=null){
//                a1 = false;
//            }
//        }

//        System.out.println("黄鑫峻userID："+userid);
        tv1.setText("用户id:"+userid);
        tv2.setText("用户名:"+username);
        tv3.setText("邮箱："+email);
        tv4.setText("频道信息："+"\n\r"+pd_info);

        tv1.setTextSize(18);
        tv2.setTextSize(18);
        tv3.setTextSize(18);
        tv4.setTextSize(18);
        //3.把TextView设置成线性布局的子节点
        ll.addView(tv1);
        ll.addView(tv2);
        ll.addView(tv3);
        ll.addView(tv4);

    }

}
