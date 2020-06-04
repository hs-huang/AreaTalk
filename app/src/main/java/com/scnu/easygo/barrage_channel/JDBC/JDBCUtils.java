package com.scnu.easygo.barrage_channel.JDBC;

import com.mysql.jdbc.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCUtils {
	static{
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	public static Connection getConnection() throws SQLException{

			return (Connection) DriverManager.getConnection("jdbc:mysql://47.102.154.192:3306/area_talk?characterEncoding=utf8", "root", "123456");
		
	}
}
