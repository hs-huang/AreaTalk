package com.scnu.easygo.channel_about;
import com.mysql.jdbc.Connection;
import com.scnu.easygo.barrage_channel.JDBC.JDBCUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
public class Select_channel {
    public String addChannelID(String user_id) {
        String a = null;

//        int i=0;
        try {
            Connection connection= JDBCUtils.getConnection();
            String sql = "select pd_name from  pd where pd=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, user_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("sql="+preparedStatement.toString());//打印sql语句

            int col = resultSet.getMetaData().getColumnCount();
            while(resultSet.next()){
                for(int i=1;i<=col;i++){
//                    System.out.println(resultSet.getString(i));
                    a = (String)resultSet.getString(i);
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
     * 判断频道是否已经存在
     * 时间：2020年3月21日00:37:05
     */
    public boolean channel_if_exit(String a,String b){
        String channel_password = b;
        String channel_num=a;
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

    public static void main(String[] args) {

//        Select_channel select_channel = new Select_channel();
//        System.out.println(select_channel.channel_if_exit("12345678","123456"));
//        System.out.println(select_channel.channel_if_exit("12345678",""));
//        System.out.println(select_channel.channel_if_exit("12345678","huang2"));
//        System.out.println(select_channel.channel_if_exit("fh",""));
//        System.out.println(select_channel.channel_if_exit("fh",""));
    }


}
