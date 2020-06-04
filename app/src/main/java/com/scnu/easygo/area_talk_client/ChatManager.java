package com.scnu.easygo.area_talk_client;

import com.scnu.easygo.userID.UserID;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ChatManager {
    /*
    单例模式
     */
    private ChatManager(){}
    private static final ChatManager instance =new ChatManager();
    public static ChatManager getChatManager(){
        return instance;
    }
    Socket socket;
    BufferedReader br;										//接收信息
    PrintWriter pw;											//发送信息
    public void connect(){
        new Thread(){
            @Override
            public void run() {
                try {
                    //死循环
                    while(UserID.homeFragment==null||UserID.homeFragment.getMyLatitudeAndmyLongitude()==null);
                    socket=new Socket("47.102.154.192", 12345);//创建客户端，连接的端口是ServerSocket的端口
                    pw=new PrintWriter(
                            new OutputStreamWriter(
                                    socket.getOutputStream(),"UTF-8"));
                    br=new BufferedReader(
                            new InputStreamReader(
                                    socket.getInputStream(),"UTF-8"));
                    String line;
                    ChatManager.getChatManager().send(UserID.userID +"&"+UserID.homeFragment.getMyLatitudeAndmyLongitude()+"&"+UserID.homeFragment.getMyCircleRadius());
                    UserID.mainActivity.isConnection();
                    while ((line=br.readLine())!=null) {
                        UserID.homeFragment.marketAndInfomation(line);
                        UserID.homeFragment.receivedMessage(line);
                    }
                }catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }finally{
                    try {
                        if(socket!=null){
                            socket.close();
                            socket=null;
                        }
                        if(br!=null){
                            br.close();
                        }
                        if(pw!=null){
                            pw.close();
                            pw=null;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
    public void close(){
        try {
            if(socket!=null){
                socket.close();
                socket=null;
            }
            if(br!=null){
                br.close();
            }
            if(pw!=null){
                pw.close();
                pw=null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void send(String out){
        if(pw!=null){
            pw.write(out+"\n");
            pw.flush();
        }
    }

    /**
     * 判断是否保持连接
     * @return
     */
    public boolean isConnection(){
        if(socket!=null){
            return true;
        }else {
            return false;
        }
    }
}
