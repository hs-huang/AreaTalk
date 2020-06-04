package com.scnu.easygo.login;

import android.util.Log;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.content.ContentValues.TAG;

public class Regist {
    public static String registByPost(String username,String password,String email,String verification){
        String path="http://47.102.154.192:8080/DaChuang/servlet/RegistServlet";
        URL url= null;
        try {
            url = new URL(path);
            HttpURLConnection conn= (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("POST");
            Log.d(TAG, "registByPost: "+username);
            Log.d(TAG, "registByPost: "+password);
            Log.d(TAG, "registByPost: "+email);
            Log.d(TAG, "registByPost: "+verification);
            String data="username="+username+"&password="+password
                    +"&email="+email+"&verification="+verification;
            conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length",data.length()+"");
           // 获取session信息

            String sessionId = Verification.getSessionId()[0];
            //保存session信息
            conn.setRequestProperty("Cookie", sessionId);

            conn.setDoOutput(true);
            OutputStream os= conn.getOutputStream();
            os.write(data.getBytes());
            int code=conn.getResponseCode();

            if(code==200){
                //请求成功
                InputStream is=conn.getInputStream();
                String text=StreamTools.readInputStream(is);
                return text;
            }else{
                //请求失败
                return "0&注册失败";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
