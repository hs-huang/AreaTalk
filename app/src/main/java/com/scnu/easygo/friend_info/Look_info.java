package com.scnu.easygo.friend_info;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.scnu.easygo.barrage_channel.JDBC.JDBCUtils;
import com.scnu.easygo.userID.UserID;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Look_info {

    public int look_info() {
        try {
            Connection connection= JDBCUtils.getConnection();
            String sql = "select userid,username,email from  user where userid=?";
            java.sql.PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, "dd2ca4b9");
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){    // 依次取出数据
                String pdname = resultSet.getString("username") ;    // 取出name列的内容
                String pdid = resultSet.getString("userid") ;
                String pdemail = resultSet.getString("email") ;
                System.out.println("姓名：" + pdname + "；"+"id："+pdid+"；邮箱"+pdemail) ;
                System.out.println("-----------------------") ;
            }
            resultSet.close();

            String sql2 = "select pd,pd_name from  pd where userid=?";
            java.sql.PreparedStatement preparedStatement2 = connection.prepareStatement(sql2);
            preparedStatement2.setString(1, "dd2ca4b9");
            ResultSet resultSet2 = preparedStatement2.executeQuery();
            while(resultSet2.next()){    // 依次取出数据
                String pd_id = resultSet2.getString("pd") ;    // 取出name列的内容
                String pd_name = resultSet2.getString("pd_name") ;
                System.out.println("频道id：" + pd_id + "；"+"频道名"+pd_name) ;
                System.out.println("-----------------------") ;
            }
            resultSet2.close();

            preparedStatement.close();
            preparedStatement2.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public static void main(String[] args) {
        Look_info add_channel_activity = new Look_info();
        add_channel_activity.look_info();
    }


}
