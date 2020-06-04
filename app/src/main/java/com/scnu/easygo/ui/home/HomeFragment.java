package com.scnu.easygo.ui.home;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.utils.DistanceUtil;
import com.mysql.jdbc.Connection;
import com.scnu.easygo.MainActivity;
import com.scnu.easygo.R;
import com.scnu.easygo.area_talk_client.ChatManager;
import com.scnu.easygo.barrage_channel.ACache;
import com.scnu.easygo.barrage_channel.JDBC.JDBCUtils;
import com.scnu.easygo.barrage_channel.JDBC.UserDao;
import com.scnu.easygo.barrage_channel.ListRiskAreaListsDemoAdapter;
import com.scnu.easygo.barrage_channel.TypeLeftAdapter;
import com.scnu.easygo.barrage_channel.TypeRightAdapter;
import com.scnu.easygo.barrage_channel.addgroup;
import com.scnu.easygo.barrage_channel.mSurfaceView;
import com.scnu.easygo.barrage_channel.mText;
import com.scnu.easygo.channel_about.Add_channel_activity;
import com.scnu.easygo.nav.BNaviMainActivity;
import com.scnu.easygo.nav.BNaviMainActivity_1;
import com.scnu.easygo.userID.UserID;
import com.yinglan.scrolllayout.ScrollLayout;

import org.json.JSONObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import static android.graphics.ColorSpace.Model.RGB;

public class HomeFragment extends Fragment implements OnGetGeoCoderResultListener {

    //地理编码，在下面onCreateView()方法初始化，在marketAndInfomation中使用
    GeoCoder geoCoder;
    ReverseGeoCodeOption reverseGeoCodeOption;

    //用户信息弹窗
    View viewOverlayItem;
    ImageView imageViewAddOverlayItem;
    TextView tvAddOverlayItemUserID;
    TextView tvAddOverlayItemDistance;
    TextView tvAddOverlayGeoCoder;
    TextView tvAddOverlayItemLatlng;
    Button walkNav;
    Button bikeNav;

    //设置半径
    public int radius = 10000;
    private Overlay overlayCircle;
    LinearLayout layoutSetRadius;
    private SeekBar seekBar;
    private String radiusSeeBar = String.valueOf(radius);
    private TextView radiusText;
    LinearLayout layoutAddOverlayRadarNearbyItem;

    //当前地理信息对象
    private BDLocation location=null;

    private LatLng ll;
    private List<Marker> markers=new LinkedList<>();
    private Map<String,Marker> markerMap=new HashMap<>();
    private int i=0;

    //黄晓强，变量内容（弹幕，聊天频道，缓存）
    private mSurfaceView msurfaceView;
    private Random random=new Random();
    private int[] colors={Color.BLACK,Color.MAGENTA,Color.CYAN,Color.RED,Color.BLUE,Color.rgb(0,139,139)};
    private ListView lv_left;
    private RecyclerView rv_right;
    private TypeLeftAdapter leftAdapter;
    private TypeRightAdapter rightAdapter;
    private TextView text_foot;
    private Button sendbtn;
    private EditText message;
    private LinearLayout myChannel;
    //频道按钮
    private Context mcontext=getContext();
    private TextView tvRiskArea;
    //菜单显示PopupWindow
    private PopupWindow mPopWindow;
    private List<Map<String, Object>> riskAreaList = null;
    private ACache acache;
    List<Integer> ChannelRecordId=new ArrayList<Integer>();
    private int DeletePosition;

/////////////////////////////////////////////////////////

//    private HomeViewModel homeViewModel;//暂时不用到
    public LocationClient mLocationClient;
    private MapView mapView;
    private BaiduMap baiduMap;
    private boolean isFirstLocate = true;
    private  Bundle bundle;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        homeViewModel =
//                ViewModelProviders.of(this).get(HomeViewModel.class);
        mLocationClient = new LocationClient(getActivity().getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());
        SDKInitializer.initialize(getActivity().getApplicationContext());
        View root = inflater.inflate(R.layout.fragment_home, container, false);
//        final TextView textView = root.findViewById(R.id.text_home);
        mapView =(MapView) root.findViewById(R.id.bmapView);
        baiduMap = mapView.getMap();
        baiduMap.setMyLocationEnabled(true);
        bundle = savedInstanceState;
//        homeViewModel.getText().observe(this, new Observer<String>() {//感觉好像用了观察者设计模式
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        //获取当前对象，并储存再UserID中
        getHomeFragment();

        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if(ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_PHONE_STATE)!=PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()){
            String [] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(getActivity(),permissions,1);
        }
        else
        {
          requestLocation();//地理位置定位
            // Log.d("进入时：", "onCreate: 授权过了，并成功了");
        }

        //地址编码
        geoCoder = GeoCoder.newInstance();
        geoCoder.setOnGetGeoCodeResultListener(this);
        reverseGeoCodeOption = new ReverseGeoCodeOption();




        //隐藏信息框
        // 我这里的解决方法是设置地图底图的点击事件，来隐藏marker
        mapView.getChildAt(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baiduMap.hideInfoWindow();
                myChannel.setVisibility(View.GONE);
                text_foot.setVisibility(View.VISIBLE);

            }
        });//mapView.getChildAt(0).setOnClickListener(new View.OnClickListener()

        //黄晓强////////////////////////////////
        acache=ACache.get(getActivity());
        msurfaceView = (mSurfaceView) root.findViewById(R.id.msv);
        lv_left = (ListView) root.findViewById(R.id.lv_left);
        rv_right = (RecyclerView) root.findViewById(R.id.rv_right);
        RelativeLayout relativeLayout = (RelativeLayout) root.findViewById(R.id.root);
        myChannel = (LinearLayout) root.findViewById(R.id.myChannel);
        text_foot = (TextView) root.findViewById(R.id.text_foot);
        sendbtn=(Button)root.findViewById(R.id.send);
        message=(EditText)root.findViewById(R.id.message);
        tvRiskArea = (TextView)root.findViewById(R.id.tvRiskArea);

//        new Thread(){
//            public void run(){
//                initSurfaceView();
//
//            }
//        }.start();
        initView();
        ////////////////////////////////////////
        return root;
    }

    //黄晓强///////////////////////////////////
//    private void initSurfaceView() {
//        msurfaceView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                myChannel.setVisibility(View.GONE);
//                text_foot.setVisibility(View.VISIBLE);
//            }
//        });
//    }
    //弹幕
    private void startBarrage(String mname,String mcontent) {
        mText mText=new mText();
        mText.setText(mname+"："+mcontent);
        mText.setSpeed(3);
        mText.setColor(colors[random.nextInt(colors.length)]);
        mText.setSize(50);
        msurfaceView.add(mText);
    }
    //增顺需要调用的方法
    public void receivedMessage(String message) {
        Message msg = new Message();
        msg.obj=message;
        handle.sendMessage(msg);
    }
    //消息接收处理
    Handler handle= new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String sst=(String)msg.obj;
            String[] s=sst.split("&");

            if(leftAdapter.getPdId().equals(s[0])){
                int position=rightAdapter.getLocation();
                rightAdapter.addName(position,s[3],s[1]);
                rightAdapter.addContent(position,s[5]);
                rv_right.scrollToPosition(position);
            }
            startBarrage(s[3],s[5]);
            try {
                int temp=leftAdapter.getIndex(s[0]);
                JSONObject obj=acache.getAsJSONObject(String.valueOf(temp));
                obj.put(String.valueOf(ChannelRecordId.get(temp)),s[1]+"^"+s[3]+"&"+s[5]);
                acache.put(String.valueOf(temp),obj,2 * ACache.TIME_DAY);
                ChannelRecordId.set(temp,ChannelRecordId.get(temp)+1);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    };
    //PopupWindow菜单详细内容显示
    private void showRiskAreaPopupWindow() {
        //设置contentView
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.newpages_activity_risk_area_popup_demo, null);
        mPopWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mPopWindow.setContentView(contentView);
        //获取实例，设置各个控件的点击响应
        //注意：PopupWindow中各个控件的所在的布局是contentView，而不是在Activity中，所以，要在findViewById(R.id.tv)前指定根布局

        ListView lvRiskArea = (ListView)contentView.findViewById(R.id.lvRiskArea);
        //区域列表加载
        riskAreaList = new ArrayList<Map<String, Object>>();
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("tvAreaItem", "新建" );
        riskAreaList.add(map);

        map = new HashMap<String,Object>();
        map.put("tvAreaItem", "删除" );
        riskAreaList.add(map);

        map = new HashMap<String,Object>();
        map.put("tvAreaItem", "显示" );
        riskAreaList.add(map);

        map = new HashMap<String,Object>();
        map.put("tvAreaItem", "隐藏" );
        riskAreaList.add(map);

        ListRiskAreaListsDemoAdapter listRiskAreaListsDemoAdapter = new ListRiskAreaListsDemoAdapter(getActivity(), riskAreaList,
                R.layout.newpages_risk_area_popup_list_item_demo, new String[] { "tvAreaItem"}, new int[] { R.id.tvAreaItem});
        lvRiskArea.setAdapter(listRiskAreaListsDemoAdapter);

        mPopWindow.setTouchable(true);
        ColorDrawable dw= new ColorDrawable(0xb0000000);
        mPopWindow.setBackgroundDrawable(dw);

        //解决5.0以下版本点击外部不消失问题
        mPopWindow.setOutsideTouchable(true);
        mPopWindow.setBackgroundDrawable(new BitmapDrawable());
        //显示方式
        mPopWindow.showAsDropDown(tvRiskArea,0,-580);

        lvRiskArea.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        mPopWindow.dismiss();
                        Intent intent=new Intent(getActivity(), Add_channel_activity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        mPopWindow.dismiss();
                        if(leftAdapter.getCount()==1){Toast.makeText(getActivity(), "暂无频道可以删除", Toast.LENGTH_SHORT).show();}
                        else{showDeleteChannel();}
                        break;
                    case 2:
                        mPopWindow.dismiss();
                        lv_left.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        mPopWindow.dismiss();
                        lv_left.setVisibility(View.GONE);
                        break;
                    default:
                        break;
                }
            }
        });


    }

    public void showDeleteChannel(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("请选择频道");
        String[] temp=new String[leftAdapter.getCount()-1];
        for(int i=1;i<leftAdapter.getCount();i++){
            temp[i-1]=leftAdapter.getTitles().get(i);
        }
        DeletePosition=1;

        builder.setSingleChoiceItems(temp, 0, new DialogInterface.OnClickListener() {

            @Override //which：点击位置
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(MainActivity.this, "这是"+items[which], Toast.LENGTH_SHORT).show();
                DeletePosition=which+1;
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //要删除的频道在数组中的位置：DeletePosition
                //频道名称：leftAdapter.getTitles().get(DeletePosition)
                //频道ID: leftAdapter.getDeletePdId(DeletePosition)
                //cutChannel(DeletePosition);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Connection connection = null;
                        PreparedStatement preparedStatement = null;
                        ResultSet resultSet = null;
                        try {
                            connection = JDBCUtils.getConnection();
                            String sql = "delete from userpd where userid=? and pd=?";
                            preparedStatement = connection.prepareStatement(sql);
                            preparedStatement.setString(1, UserID.userID);
                            preparedStatement.setString(2, leftAdapter.getDeletePdId(DeletePosition));
                            int i=preparedStatement.executeUpdate();

                            getActivity().runOnUiThread(new Runnable() {
                                public void run() {
                                    cutChannel(DeletePosition);
                                }
                            });
                        } catch (SQLException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                if (resultSet != null) {
                                    resultSet.close();
                                }
                                if (preparedStatement != null) {
                                    preparedStatement.close();
                                }
                                if (connection != null) {
                                    connection.close();
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
                Toast.makeText(getActivity(), "删除成功！", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(MainActivity.this, "这是取消", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }

    //删除频道
    public void cutChannel(int position){
        cutAcache(position);
        ChannelRecordId.remove(position);
        leftAdapter.cutChannel(position);

    }

    public void cutAcache(int position){
        int size=leftAdapter.getCount();
        if(position!=size-1){
            for(int i=position;i<size-1;i++){
                JSONObject obj = acache.getAsJSONObject(String.valueOf(i+1));
                acache.put(String.valueOf(i),obj,2 * ACache.TIME_DAY);
            }
        }
    }

    //增顺添加频道调用
    public void addChannel(String pId,String pname){
        int a=leftAdapter.getCount();
        LinkedHashMap<String,String> linkedHashMap=new LinkedHashMap<String,String>();
        acache.put(String.valueOf(a),new JSONObject(linkedHashMap),2 * ACache.TIME_DAY);
        ChannelRecordId.add(0);
        leftAdapter.addChannel(pId,pname);
    }

    private void initView() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<String> pdId = new ArrayList<String>();
                    List<String> pdName = new ArrayList<String>();
                    pdId.add("000");
                    pdName.add("公频");
                    HashMap<String, String> map = new UserDao().getPdMap(UserID.userID);
                    Set<String> keys=map.keySet();
                    for(String key:keys){
                        String value=map.get(key);
                        pdId.add(key);
                        pdName.add(value);
                        //System.out.println("key:"+key+"  and  value:"+value);
                    }
                    leftAdapter = new TypeLeftAdapter(getActivity(),pdId,pdName);
                    lv_left.setAdapter(leftAdapter);

                    //缓存初始化
                    for(int i=0;i<leftAdapter.getCount();i++)
                    {
                        LinkedHashMap<String,String> linkedHashMap=new LinkedHashMap<String,String>();
                        acache.put(String.valueOf(i),new JSONObject(linkedHashMap),2 * ACache.TIME_DAY);
                        ChannelRecordId.add(0);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        rightAdapter = new TypeRightAdapter(getActivity());
        rv_right.setAdapter(rightAdapter);
        //点击监听
        lv_left.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(MainActivity.this,"第"+position+"个item", Toast.LENGTH_SHORT).show();
                leftAdapter.changeSelected(position);//刷新
                leftAdapter.notifyDataSetChanged();

                try {
                    List<String> names=new ArrayList<String>();
                    List<String> contents=new ArrayList<String>();
                    JSONObject obj = acache.getAsJSONObject(String.valueOf(position));
                    Iterator<String> it = obj.keys();
                    while(it.hasNext()){
                        String key = it.next();
                        String value = obj.getString(key);
                        String[] sp=value.split("&");
                        names.add(sp[0]);
                        contents.add(sp[1]);

                        //int posi=rightAdapter.getLocation();
                        //rightAdapter.addName(posi,key);
                        //rightAdapter.addContent(posi,value);
                        //rv_right.scrollToPosition(position);
                    }
                    rightAdapter.setNames(names);
                    rightAdapter.setContents(contents);
                    rightAdapter.notifyDataSetChanged();
                    rv_right.scrollToPosition(rightAdapter.getItemCount()-1);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        //选中监听
        lv_left.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                leftAdapter.changeSelected(position);//刷新
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        LinearLayoutManager manager=new LinearLayoutManager(getActivity());
        rv_right.setLayoutManager(manager);
        lv_left.setVisibility(View.VISIBLE);
        myChannel.setVisibility(View.GONE);

        sendbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String name1=UserID.userName;
                String content1=message.getText().toString();
                if(content1==null||"".equals(content1)){
                    return;
                }
                int position=rightAdapter.getLocation();
                message.setText("");
                //界面显示消息
                rightAdapter.addName(position,name1,UserID.userID);
                rightAdapter.addContent(position,content1);
                rv_right.scrollToPosition(position);
                //弹幕发送
                startBarrage(name1,content1);
                //消息缓存
                try {
                    JSONObject obj=acache.getAsJSONObject(String.valueOf(leftAdapter.getmSelect()));
                    obj.put(String.valueOf(ChannelRecordId.get(leftAdapter.getmSelect())),UserID.userID+"^"+name1+"&"+content1);
                    acache.put(String.valueOf(leftAdapter.getmSelect()),obj,2 * ACache.TIME_DAY);
                    ChannelRecordId.set(leftAdapter.getmSelect(),ChannelRecordId.get(leftAdapter.getmSelect())+1);
                }catch (Exception e){
                    e.printStackTrace();
                }
                //服务器发送
                String m1=leftAdapter.getPdId();//频道ID
                String m2=UserID.userID;//用户ID
                String m3=getMyLatitudeAndmyLongitude();//经纬度
                String m4=UserID.userName;//用户名
                String m5=leftAdapter.getPdName();//频道名称
                String m6=content1;//内容
                String m7=getMyCircleRadius();
                final String send_message=m1+"&"+m2+"&"+m3+"&"+m4+"&"+m5+"&"+m6+"&"+m7;
                //开线程发送消息
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //调用增顺方法发消息
                            ChatManager.getChatManager().send(send_message);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        text_foot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myChannel.setVisibility(View.VISIBLE);
                text_foot.setVisibility(View.GONE);
            }
        });

        //频道按钮
        //context = this;
        //绑定点击事件
        tvRiskArea.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showRiskAreaPopupWindow();
            }
        });
    }

    ////////////////////////////////////////////



    //检测是否完全授权
    public void onReqeuestPermissionsResult(int requestCode,String [] permissions,int [] grantResults){
        switch (requestCode){
            case 1:
                if(grantResults.length>0 ){
                    for (int result : grantResults){
                        if (result != PackageManager.PERMISSION_DENIED){
                            Toast.makeText(getActivity(),"必须同意所有权限才能使用本程序",
                                    Toast.LENGTH_SHORT).show();
                            getActivity().finish();
                            return;
                        }
                    }
                    requestLocation();//地理位置定位
                    Log.d("检查授权", "onReqeuestPermissionsResult: 开始定位");
                } else {
                    Toast.makeText(getActivity(),"发生未知错误",Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
                break;
            default:
        }
    }

    //地理位置定位
    public void requestLocation(){
        initLocation();//实时更新当前位置
        mLocationClient.start();//该方法默认只定位一此
    }




    //
//定位监听器
public class MyLocationListener implements BDLocationListener {
    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        if (bdLocation.getLocType()==BDLocation.TypeGpsLocation
                ||bdLocation.getLocType()==BDLocation.TypeNetWorkLocation){
            navigateTo(bdLocation);
        }
    }
}
//
    //实时更新当前位置
    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setScanSpan(5000);//5秒更新一次
        option.setIsNeedAddress(true);
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        mLocationClient.setLocOption(option);
    }
//
    private void navigateTo(BDLocation location){
        this.location=location;
        if(isFirstLocate){
            ll = new LatLng(location.getLatitude(),location.getLongitude());
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
            baiduMap.animateMapStatus(update);//地图移动到当前位置
            update = MapStatusUpdateFactory.zoomTo(17f);//设置缩放级别
            baiduMap.animateMapStatus(update);//完成缩放功能
            isFirstLocate=false;



            //绘制我的圆
            OverlayOptions centrePolyine = new CircleOptions()
                    .fillColor(0x384d73b3)
                    .center(new LatLng(location.getLatitude(),location.getLongitude()))
                    .stroke(new Stroke(3, 0x784d73b3))
                    .radius(radius);//我的半径，可以修改,初次进入是20000

            overlayCircle =  baiduMap.addOverlay(centrePolyine);

        }//if(ifFirstLocate)

        //显示设备当前位置
        MyLocationData.Builder locationBuilder = new MyLocationData.Builder();
        locationBuilder.latitude(location.getLatitude());
        locationBuilder.longitude(location.getLongitude());
        MyLocationData locationData = locationBuilder.build();
        baiduMap.setMyLocationData(locationData);


        setMyRadius();


    }

    private void setMyRadius() {
        //地图定位图标点击响应事件，设置当前圆半径
        baiduMap.setOnMyLocationClickListener(new BaiduMap.OnMyLocationClickListener() {
            @Override
            public boolean onMyLocationClick() {
                viewOverlayItem = View.inflate(getContext(), R.layout.set_radius, null);
                layoutSetRadius = (LinearLayout) viewOverlayItem.findViewById(R.id.layoutSetRadius);
//                etRadius = (EditText) viewOverlayItem.findViewById(R.id.et_radius);//半径

                radiusText = (TextView) viewOverlayItem.findViewById(R.id.seekBar_tv);//半径

                seekBar = (SeekBar) viewOverlayItem.findViewById(R.id.seekBar);//半径
                seekBar.setMax(10000);
                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        // 当进度条改变时，改变文本框的显示内容
                        radiusSeeBar = String.valueOf(progress);
                        radiusText.setText("当前半径："+progress+"米");
                        Log.v("我的半径",radiusSeeBar);
                        try {
//                            viewOverlayItem.setVisibility(View.GONE);
                            if(!(radiusSeeBar.equals(null))){
                                Log.v("我的半径",radiusSeeBar);
                                radius = Integer.parseInt(radiusSeeBar);

                                overlayCircle.remove();
                                //绘制我的圆
                                OverlayOptions centrePolyine = new CircleOptions()
                                        .fillColor(0x384d73b3)
                                        .center(ll)
                                        .stroke(new Stroke(3, 0x784d73b3))
                                        .radius(radius);//我的半径，可以修改
                                overlayCircle = baiduMap.addOverlay(centrePolyine);

                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        Toast.makeText(getActivity(),"开始滑动",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        Toast.makeText(getActivity(),"结束滑动",Toast.LENGTH_SHORT).show();
                    }
                });

//                显示信息
//                 －130表示的是y轴的便宜量
                InfoWindow infoWindow =new InfoWindow(viewOverlayItem,ll,-130);
                //通过百度地图来显示view
                baiduMap.showInfoWindow(infoWindow);
                return false;
            }
        });


    }

    //晓强获取的经纬度的方法,返回经纬度
    public String getMyLatitudeAndmyLongitude(){
        if(location==null){
            return null;
        }
        String latitudeAndmyLongitude = String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude());
        Log.v("我的当前的经纬度：",latitudeAndmyLongitude);
        return  latitudeAndmyLongitude;
    }

    //服务端获取我当前的半径
    public String getMyCircleRadius(){
        return radiusSeeBar;
    }

    public void getHomeFragment(){
        UserID.homeFragment=this;
    }

    //显示批量客户端的点以及点击点弹出对应的信息框
    public void marketAndInfomation(String returnInfo){
//        //模拟数据
        final String[] result=returnInfo.split("&");
        if(!"000".equals(result[0])){
            return;
        }
        String[] location=result[2].split(",");
        LatLng point = new LatLng(Double.valueOf(location[0]),Double.valueOf(location[1]));

        //绘制信息Maker点
        //构建Marker图标
        BitmapDescriptor bd = BitmapDescriptorFactory
                .fromResource(R.drawable.location_ic_select);
        //创建OverlayOptions属性
        OverlayOptions option1 =  new MarkerOptions()
                .position(point)
                .icon(bd);
        //用的addOverlay
        Marker marker = (Marker)baiduMap.addOverlay(option1);
        if(markerMap.get(result[1])==null){
            markerMap.put(result[1],marker);
        }else{
            markerMap.get(result[1]).remove();
            markerMap.put(result[1],marker);
        }
        this.i++;
        //最大共存数量
        if(this.i>=3){
            Iterator<Map.Entry<String, Marker>> entries = markerMap.entrySet().iterator();
            Set<String> set=new LinkedHashSet<>();
            while(entries.hasNext()){
                Map.Entry<String, Marker> entry = entries.next();
                LatLng latLngshow = entry.getValue().getPosition();
//                //距离
                double distance = DistanceUtil. getDistance(latLngshow,ll);
                if(distance > radius) {
                    i--;
                    entry.getValue().remove();   //调用Marker对象的remove方法实现指定marker的删除几何图形覆盖物
                    set.add(entry.getKey());
                }
            }
            for(String key: set){
                markerMap.remove(key);
            }
        }
//        markers.add(marker);
//        this.i++;
//        if(this.i>=5){
//            new Thread(){
//                public void run(){
//                    for(int j=0;j<markers.size();j++){
//                        LatLng latLngshow = markers.get(j).getPosition();
//                        //距离
//                        double distance = DistanceUtil. getDistance(latLngshow,ll);
//                        if(distance > 5000){
//                            i--;
//                            markers.get(j).remove();   //调用Marker对象的remove方法实现指定marker的删除几何图形覆盖物
//                            markers.remove(j);
//                        }
//                    }
//                }
//            }.start();
//        }


        //在地图上批量添加
//        baiduMap.addOverlays(options);

        //以上是显示点
        //以下是点击点弹出的对应信息框的监听事件


        //绘制信息框，并显示信息框的经纬度，地址名字
        //每个图标都是一个Marker，通过百度地图的Marker的点击事件来达到想要的效果
        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                //关联信息框布局item_map_addoverlay_radarnearby_admin，绘制信息框
                viewOverlayItem = View.inflate(getContext(), R.layout.item_map_addoverlay_radarnearby_admin, null);
                //以下是信息框里各个控件
                tvAddOverlayItemUserID = (TextView) viewOverlayItem.findViewById(R.id.tvAddOverlayItemUserID);
                imageViewAddOverlayItem = (ImageView) viewOverlayItem.findViewById(R.id.imageViewAddOverlayItem);
                tvAddOverlayGeoCoder = (TextView) viewOverlayItem.findViewById(R.id.tvAddOverlayGeoCoder);
                tvAddOverlayItemDistance = (TextView) viewOverlayItem.findViewById(R.id.tvAddOverlayItemDistance);
                tvAddOverlayItemLatlng = (TextView) viewOverlayItem.findViewById(R.id.tvAddOverlayItemLatlng);
                layoutAddOverlayRadarNearbyItem = (LinearLayout) viewOverlayItem.findViewById(R.id.layoutAddOverlayRadarNearbyItem);
                walkNav = (Button) viewOverlayItem.findViewById(R.id.publicChannel);
                bikeNav = (Button) viewOverlayItem.findViewById(R.id.privateChannel);
                //以下两行是公频与私频的监听事件
                walkNav.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent starter = new Intent(getContext(), BNaviMainActivity_1.class);
                        starter.putExtra("start",getMyLatitudeAndmyLongitude())
                                .putExtra("end",result[2]);
                        startActivity(starter);

                    }
                });
                bikeNav.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent starter = new Intent(getContext(), BNaviMainActivity.class);
                        starter.putExtra("start",getMyLatitudeAndmyLongitude())
                                .putExtra("end",result[2]);
                        startActivity(starter);
                    }
                });


//              显示对应不同点的信息框的内容
                Log.d("lml", "MapFragment:覆盖物被点击");
                if (marker != null) {
                    //点击的对应点的经纬度
                    LatLng latLngshow = marker.getPosition();

//                    result[3] = "张钦展";

                    //获取用户名
                    tvAddOverlayItemUserID.setText(result[3]);

                    //距离
                    int dis = (int)DistanceUtil. getDistance(latLngshow,ll);
                    tvAddOverlayItemDistance.setText("距离："+ String.valueOf(dis) + "m");

//                    result[1] = "13006781260";

                    //获取id
                    tvAddOverlayItemLatlng.setText("ID：" + result[1]);


                    //获取地址反编码信息成功即经纬度转换为地理名字
                    reverseGeoCodeOption.location(latLngshow);
                    geoCoder.reverseGeoCode(reverseGeoCodeOption);
                    tvAddOverlayGeoCoder.setText("正在获取详细位置");//当对应点有详细地址名时，会在下面 onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult)方面显示地址名

                    //显示信息
                    // －130表示的是y轴的便宜量
                    InfoWindow infoWindow =new InfoWindow(viewOverlayItem,marker.getPosition(),-130);
                    //通过百度地图来显示view
                    baiduMap.showInfoWindow(infoWindow);
                }//   if (marker != null)

                return true;
            }//    public boolean onMarkerClick(Marker marker)
        });//  baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener()



    }//public void marketAndInfomation(BDLocation location)



    //地址正编码信息即即地理名字转换为经纬度，本次不需要，所以没有内容
    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

    }


    //获取地址反编码信息成功即经纬度转换为地理名字
    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
        Log.d("lml", "获取地址反编码信息成功");
        tvAddOverlayGeoCoder.setText(reverseGeoCodeResult.getAddress());
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mLocationClient.stop();//停止定位
        mapView.onDestroy();
//        Log.d("9", "onDestroyView:9 ");
        baiduMap.setMyLocationEnabled(false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        Log.d("2", "onViewCreated:2 ");
        mLocationClient.start();
        mapView.onCreate(getActivity(),savedInstanceState);
        baiduMap.setMyLocationEnabled(true);
    }
}
