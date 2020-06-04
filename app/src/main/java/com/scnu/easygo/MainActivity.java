package com.scnu.easygo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.scnu.easygo.area_talk_client.ChatManager;
import com.scnu.easygo.barrage_channel.TypeLeftAdapter;
import com.scnu.easygo.barrage_channel.TypeRightAdapter;
import com.scnu.easygo.barrage_channel.mSurfaceView;
import com.scnu.easygo.barrage_channel.mText;
import com.scnu.easygo.login.Login;
import com.scnu.easygo.login.LoginActivity;
import com.scnu.easygo.nav.BNaviMainActivity;
import com.scnu.easygo.ui.home.HomeFragment;
import com.scnu.easygo.userID.UserID;
import com.yinglan.scrolllayout.ScrollLayout;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    private ImageView imageView;

    private TextView anv_textView_userId;
    private TextView anv_textView_username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(UserID.userID==null){
            Intent starter = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(starter);
            this.finish();
        }
        UserID.mainActivity=this;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_hotel, R.id.nav_taxi,
                R.id.nav_route, R.id.nav_order, R.id.nav_set)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        NavigationView navigationView1=findViewById(R.id.nav_view);
        View headView=navigationView1.getHeaderView(0);


        /**
         * 菜单栏选项跳转
         */
        //注销
        navigationView1.getMenu().findItem(R.id.nav_set).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){

            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                jumpToLogin();
                return true;
            }
        });

        anv_textView_userId=headView.findViewById(R.id.anv_textView_userId);
        anv_textView_userId.setText("ID："+UserID.userID);
        anv_textView_username=headView.findViewById(R.id.anv_textView_username);
        anv_textView_username.setText("昵称："+UserID.userName);
        //这个是头像跳转的
        imageView=headView.findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent starter = new Intent(MainActivity.this, BNaviMainActivity.class);
//                startActivity(starter);
            }
        });

    }

    /**
     * 菜单注销栏
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch(item.getItemId()) //得到被点击的item的itemId
        {
            case  R.id.action_settings :  //对应的ID就是在add方法中所设定的Id
                jumpToLogin();
                break;

        }
        return true;
    }
    public void jumpToLogin(){
        UserID.userID=null;
        UserID.userName=null;
        UserID.homeFragment=null;
        UserID.mainActivity=null;
        new Thread() {
            public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this,"注销成功！",Toast.LENGTH_SHORT).show();
                        }
                    });
                ChatManager.getChatManager().close();
            }
        } .start();
        Intent starter = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(starter);
        this.finish();
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    /**
     * 每次返回来的数据
     * @param returnInfo
     */
    public void getReturnInfo(String returnInfo){
        String[] result=returnInfo.split("&");
//        获取经纬度

//        while(UserID.homeFragment==null||UserID.homeFragment.getMyLatitudeAndmyLongitude()==null);
//        UserID.homeFragment.getMyLatitudeAndmyLongitude();
    }

    /**
     * 判断是否保持连接
     */
    public void isConnection(){
        new Thread(){
            public  void run(){
                while (ChatManager.getChatManager().isConnection());
                ChatManager.getChatManager().close();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this,"服务器断开连接！",Toast.LENGTH_SHORT).show();
                    }
                });
                UserID.userID=null;
                UserID.userName=null;
                UserID.homeFragment=null;
                UserID.mainActivity=null;
                Intent starter = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(starter);
                MainActivity.this.finish();
            }
        }.start();
    }


}
