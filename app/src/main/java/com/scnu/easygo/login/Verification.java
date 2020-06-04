package com.scnu.easygo.login;

import java.net.HttpURLConnection;
import java.net.URL;

public class Verification {
    private static String [] sessionId;
    public static String[] getSessionId(){
            return sessionId;
    }
    public static boolean verificationByGet(String email){
        String path="http://47.102.154.192:8080/DaChuang/servlet/VerificationServlet?email="+email;
        URL url= null;
        try {
            url = new URL(path);
            HttpURLConnection conn= (HttpURLConnection) url.openConnection();
            //获取session信息
            String session_value = conn.getHeaderField("Set-Cookie");
             sessionId = session_value.split(";");
            //保存session信息
//            conn.setRequestProperty("Cookie", sessionId[0]);

            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            int code=conn.getResponseCode();
            if(code==200){
                //请求成功
                return true;
            }else{
                //请求失败
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;


    }
}
