package com.scnu.easygo.login;

import com.scnu.easygo.userID.UserID;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Login {
    public static String loginByGet(String email,String password)  {
        String path="http://47.102.154.192:8080/DaChuang/servlet/LoginServlet?email="+email+"&password="+password;
//        String path="http://47.102.154.192:8080/DaChuang/index,jsp";
        URL url= null;
        try {
            url = new URL(path);
            HttpURLConnection conn= (HttpURLConnection) url.openConnection();

            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            int code=conn.getResponseCode();
            if(code==200){
                //请求成功
                InputStream is=conn.getInputStream();
                String text=StreamTools.readInputStream(is);

                return text;
            }else{
                //请求失败
                return "0&登录失败！";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
