package com.scnu.easygo.channel_about;

import com.scnu.easygo.barrage_channel.JDBC.JDBCUtils;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.scnu.easygo.userID.UserID;

import java.sql.SQLException;

public class Add_channel {

//    private EditText editText_channel_name;
//    EditText editText_channel_num;
//    EditText editText_channel_passsword;
//    Button button_add_channel;
//
//    private static Connection conn=null;
//    private static PreparedStatement pstmt=null;
//    private static ResultSet rs=null;

//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        editText_channel_name = (EditText) findViewById(R.id.new_channel_name);
//        editText_channel_num = (EditText) findViewById(R.id.new_channel_num);
//        editText_channel_passsword = (EditText) findViewById(R.id.new_channel_password);
//        button_add_channel = (Button) findViewById(R.id.add_channel_Button);
//        button_add_channel.setOnClickListener(this);
//
//    }

//    @Override
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.add_channel_Button:
//                add_channel();
//                break;
//
//        }
//    }

//    public void add_channel() {
//        final String channel_name=editText_channel_name.getText().toString().trim();
//        final String channel_num=editText_channel_num.getText().toString().trim();
//        final String channel_password=editText_channel_passsword.getText().toString().trim();
//        addChannelID();
//    }

    public int addChannelID() {
        int i=0;
        try {
            Connection connection= JDBCUtils.getConnection();
            PreparedStatement pstmt;
            String sql = "insert into pd (pd,pd_name,password,userid,username) values (?,?,?,?,?)";
            pstmt = (PreparedStatement) connection.prepareStatement(sql);

            //"INSERT INTO pd (pd,pd_name,password) VALUES (channel_name,channel_num,editText_channel_passsword)"
            pstmt.setString(1, "pindaonum");
            pstmt.setString(2, "pingdaoname");
            pstmt.setString(3, "passwor");
            pstmt.setString(4, "passw");
            pstmt.setString(5, "pasor1929");

            i=pstmt.executeUpdate();
//            while(rs.next()){
//                list.add(rs.getString("userid"));
//            }
            pstmt.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return i;
    }


    public static void main(String[] args) {
        System.out.println(UserID.userID);
        Add_channel add_channel_activity = new Add_channel();
        add_channel_activity.addChannelID();
    }


}
