package com.scnu.easygo.barrage_channel.JDBC;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * �����ݿ�Ľ���
 * @author Jiang ZengShun
 *
 */
public class UserDao {
	private static Connection conn=null;
	private static PreparedStatement pstmt=null;
	private static ResultSet rs=null;

	/**
	 * ͨ��Ƶ��ID��ȡ�û�ID
	 * @param string
	 * @return
	 */
	public  HashMap<String, String> getUserID(String string) {
		HashMap<String, String> hashMap = new HashMap<>();
		try {
			Connection connection=JDBCUtils.getConnection();
			pstmt=(PreparedStatement) connection.prepareStatement("select pd,pd_name from userpd where userid=?");
			pstmt.setString(1, string);
			rs=pstmt.executeQuery();
			while(rs.next()){
				hashMap.put(rs.getString("pd"),rs.getString("pd_name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return hashMap;
	}

	public  void close(){
		if(rs!=null) {
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				rs=null;
			}
		}

		if(pstmt!=null) {
			try {
				pstmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally {
				pstmt=null;
			}
		}

		if (conn!=null) {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				conn=null;
			}

		}

	}


	public HashMap<String, String> getPdMap(String userid){
		HashMap<String, String> map=getUserID(userid);
		close();
		return map;
	}

	/*
	public static void main(String[] args) {
		UserDao userDao = new UserDao();
		List<String> titles=userDao.getPd_name("b123");
		for (int i=0;i<titles.size();i++) {
			System.out.println("titles:"+titles.get(i));
		}
	}
	*/

}
