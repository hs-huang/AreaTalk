package com.scnu.easygo.login;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class StreamTools {
    /**
     * 把输入流的内容转换为字符串
     */
    public static String readInputStream(InputStream is){
        try {
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            int len=0;
            byte[] buffer=new byte[1024];
            while((len=is.read(buffer))!=-1){
                baos.write(buffer,0,len);
            }
            is.close();
            baos.close();
            byte[] result=baos.toByteArray();
            String temp=new String(result);
            if(temp.contains("utf-8")){
                return temp;
            }else if(temp.contains("gb2312")){
                return new String(result,"gb2312");
            }
            return temp;
        }catch (Exception e){
            e.printStackTrace();
            return "获取失败";
        }
    }
}
