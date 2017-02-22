package dragonzonee.com.strengthen.SecondPart_Locate;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.graphics.Color;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.PolylineOptions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import dragonzonee.com.strengthen.R;

public class ContentActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,LocationSource,AMapLocationListener {


    private MapView mMapView;
    private AMap mAMap;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;
    private PolylineOptions mPolyoptions;
    private Snackbar mSnackbar;
    private float speed;
    private LatLng SecLng;
    private LatLng TematLng;
    float distance;
    //0代表没有开始
    private int ispause=0;
    private int isstart=0;
    private int isstop=1;//没有点击时一直是stop的状态

    private LatLng mylocation;
    private long mExitTime;
    private int layout_num;
    private PathRecord record;
    private DbAdapter DbHepler;
    private long mStartTime;
    private long mEndTime;
    private RelativeLayout mRelativeLayout1;
    private RelativeLayout mRelativeLayout2;
    private RelativeLayout mRelativeLayout3;
    private TextView mRM1_tv_averagespeed;
    private TextView mRM1_tv_current_distance;
    private TextView mNaviBar_username;
    private ImageView mMAP_BackIcon;    private ImageView mRM1_map_clicker; private ImageView mRM2_map_clicker; private ImageView mRM3_map_clicker;
    private ImageView mRM1_go;private ImageView mRM2_go;private ImageView mRM3_go;
    private ImageView mRM1_pause;private ImageView mRM2_pause;private ImageView mRM3_pause;
    private ImageView mRM1_stop;private ImageView mRM2_stop;private ImageView mRM3_stop;
    private String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        //requestExternalPermission();
        mRelativeLayout1=(RelativeLayout)findViewById(R.id.relativeLO_RM1);
        mRelativeLayout2=(RelativeLayout)findViewById(R.id.relativeLO_RM2);
        mRelativeLayout3=(RelativeLayout)findViewById(R.id.relativeLO_RM3);
        mMAP_BackIcon=(ImageView)findViewById(R.id.map_back_icon);
        mRM1_map_clicker=(ImageView)findViewById(R.id.RM1_map_clicker);
        mRM2_map_clicker=(ImageView)findViewById(R.id.RM2_map_clicker);
        mRM3_map_clicker=(ImageView)findViewById(R.id.RM3_map_clicker);
        mRM1_go=(ImageView)findViewById(R.id.RM1_go);mRM2_go=(ImageView)findViewById(R.id.RM2_go);mRM3_go=(ImageView)findViewById(R.id.RM3_go);
        mRM1_pause=(ImageView)findViewById(R.id.RM1_pause);mRM2_pause=(ImageView)findViewById(R.id.RM2_pause);mRM3_pause=(ImageView)findViewById(R.id.RM3_pause);
        mRM1_stop=(ImageView)findViewById(R.id.RM1_stop);mRM2_stop=(ImageView)findViewById(R.id.RM2_stop);mRM3_stop=(ImageView)findViewById(R.id.RM3_stop);
        mRM1_tv_averagespeed=(TextView)findViewById(R.id.RM1_tv_average_speed);
        mRM1_tv_current_distance=(TextView)findViewById(R.id.RM1_tv_current_distance);
        mNaviBar_username=(TextView)findViewById(R.id.tv_bar_username);
        //获取地图控件引用
        mMapView = (MapView)findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        Initial_Icon();
        init();
        initpolyline();
        username=getusername();
        //Toast.makeText(MapActivity.this,"OnCreate function is finished",Toast.LENGTH_SHORT).show();


        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
///*                Intent intent=new Intent();
//                intent.setClass(ContentActivity.this, MapActivity.class);
//                startActivity(intent);*/
//                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();*/
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        /*ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();*/

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        imageViewOnClick(mRM1_map_clicker);
        imageViewOnClick(mRM2_map_clicker);
        imageViewOnClick(mRM3_map_clicker);
        RM_OnclickerListener(mRM1_go,mRM1_pause,mRM1_stop);RM_OnclickerListener(mRM2_go,mRM2_pause,mRM2_stop);RM_OnclickerListener(mRM3_go,mRM3_pause,mRM3_stop);
        map_backiconOnClicklistener(mMAP_BackIcon);

    }

    //连按两次推出
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(isstop==1) {
                if ((System.currentTimeMillis() - mExitTime) > 2000) {
                    Object mHelperUtils;
                    Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    mExitTime = System.currentTimeMillis();

                } else {
                    System.exit(0);
                }
                return true;
            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(ContentActivity.this);
                builder.setTitle("Warning");
                builder.setMessage("当前有活动中的运动训练，请先结束训练再退出");
                builder.create().show();
            }
        }
        return super.onKeyDown(keyCode, event);
    }




    //imageview隐藏的Onclick方法
    public void imageViewOnClick(final ImageView imageView){
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_num=hidlayout(imageView);
                System.out.println("layout_mun: "+ layout_num);
            }
        });

    }




    //这里是设置点击地图图标时显示地图界面
    public int hidlayout(ImageView imageView){
        int layout_Num;
        switch (imageView.getId()) {
            case R.id.RM1_map_clicker:
                layout_Num=1;
                mRelativeLayout3.setVisibility(View.INVISIBLE);
                mRelativeLayout2.setVisibility(View.INVISIBLE);
                mRelativeLayout1.startAnimation(setFadeInAnimation("FadeOut"));
                mRelativeLayout1.setVisibility(View.INVISIBLE);
                if(ispause==1){
                    mAMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_ROTATE);
                }
                break;
            case R.id.RM2_map_clicker:
                layout_Num=2;
                mRelativeLayout1.setVisibility(View.INVISIBLE);
                mRelativeLayout3.setVisibility(View.INVISIBLE);
                mRelativeLayout2.startAnimation(setFadeInAnimation("FadeOut"));
                mRelativeLayout2.setVisibility(View.INVISIBLE);
                if(ispause==1){
                    mAMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_ROTATE);
                }
                break;
            case R.id.RM3_map_clicker:
                layout_Num=3;
                mRelativeLayout2.setVisibility(View.INVISIBLE);
                mRelativeLayout1.setVisibility(View.INVISIBLE);
                mRelativeLayout3.startAnimation(setFadeInAnimation("FadeOut"));
                mRelativeLayout3.setVisibility(View.INVISIBLE);
                if(ispause==1){
                    mAMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_ROTATE);
                }
                break;
            default:
                layout_Num=0;
                System.out.println("hid layout fail!!!!!!!!!!!!!!!!");
                break;
        }
        return layout_Num;
    }


    //这里是在地图界面点击返回按钮时显示跑步方法界面
    public void map_backiconOnClicklistener(ImageView imageView){
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("!!!!!!!!!!!!!!!!!!!!!");
                System.out.println(layout_num+"------------------>");
                switch(layout_num){
                    case 1:
                        System.out.println("11111111111111111");
                        mRelativeLayout1.startAnimation(setFadeInAnimation("FadeIn"));
                        mRelativeLayout1.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        mRelativeLayout2.startAnimation(setFadeInAnimation("FadeIn"));
                        mRelativeLayout2.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        mRelativeLayout3.startAnimation(setFadeInAnimation("FadeIn"));
                        mRelativeLayout3.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
    }


    //drawer layout的后退点击响应
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.content, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    //这里是侧边栏选中的事件响应
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_persdata) {
            // Handle the camera action
        } else if (id == R.id.nav_running_method1) {
            if(isstop==1) {
                mRelativeLayout1.setVisibility(View.VISIBLE);
                mRelativeLayout2.setVisibility(View.INVISIBLE);
                mRelativeLayout3.setVisibility(View.INVISIBLE);
            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(ContentActivity.this);
                builder.setTitle("Warning");
                builder.setMessage("当前训练正在进行中，不能转换标签页");
                builder.create().show();
            }

        } else if (id == R.id.nav_running_method2) {
            if(isstop==1) {
                mRelativeLayout2.setVisibility(View.VISIBLE);
                mRelativeLayout1.setVisibility(View.INVISIBLE);
                mRelativeLayout3.setVisibility(View.INVISIBLE);
            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(ContentActivity.this);
                builder.setTitle("Warning");
                builder.setMessage("当前训练正在进行中，不能转换标签页");
                builder.create().show();
            }

        } else if (id == R.id.nav_running_method3) {
            if(isstop==1) {
                mRelativeLayout3.setVisibility(View.VISIBLE);
                mRelativeLayout2.setVisibility(View.INVISIBLE);
                mRelativeLayout1.setVisibility(View.INVISIBLE);
            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(ContentActivity.this);
                builder.setTitle("Warning");
                builder.setMessage("当前训练正在进行中，不能转换标签页");
                builder.create().show();
            }

        } else if (id == R.id.nav_RM1_history) {
            Intent intent=new Intent();
            intent.setClass(ContentActivity.this,RestoreActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_RM2_history) {

        }else if (id == R.id.nav_RM3_history) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    ///以下放的是地图的配置文件

    /*  * 初始化AMap对象*/
    private void init() {
        if (mAMap == null) {
            mAMap = mMapView.getMap();
            setUpMap();
        }

    }


    /* * 设置一些amap的属性*/
    private void setUpMap() {
        mAMap.setLocationSource(this);// 设置定位监听，在这里调用了activate方法
        mAMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        mAMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        mAMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
        etupLocationStyle();
    }

    private void etupLocationStyle(){
        // 自定义系统定位蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        // 自定义定位蓝点图标s
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.
                fromResource(R.drawable.arrow_bluesmall));
        // 自定义精度范围的圆形边框颜色
        myLocationStyle.strokeColor(getResources().getColor(R.color.mycolor_gray));
        //自定义精度范围的圆形边框宽度
        myLocationStyle.strokeWidth(5);
        // 设置圆形的填充颜色p
        myLocationStyle.radiusFillColor(getTitleColor());
        // 将自定义的 myLocationStyle 对象添加到地图上
        mAMap.setMyLocationStyle(myLocationStyle);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，实现地图生命周期管理
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void activate(OnLocationChangedListener listener) {
        //Toast.makeText(ContentActivity.this,"activate function is running",Toast.LENGTH_SHORT).show();
        mListener = listener;
        startlocation();

    }


    @Override
    public void deactivate() {
        mListener = null;
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();

        }
        mLocationClient = null;
    }


    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        //Toast.makeText(ContentActivity.this,"Onchanged function is running",Toast.LENGTH_SHORT).show();
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null && amapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
                mylocation = new LatLng(amapLocation.getLatitude(),
                        amapLocation.getLongitude());
                mAMap.moveCamera(CameraUpdateFactory.changeLatLng(mylocation));
                mAMap.moveCamera(CameraUpdateFactory.zoomTo(18));
                /*mPolyoptions.add(mylocation);*/


                if(ispause==1){
                    record.addpoint(amapLocation);
                    mPolyoptions.add(mylocation);

                    speed = amapLocation.getSpeed();//
                    mRM1_tv_averagespeed.setText(String.valueOf(speed));//
                    SecLng=TematLng;
                    TematLng=mylocation;
                    double betweenDis = AMapUtils.calculateLineDistance(SecLng,
                            TematLng);
                    distance= (float) (distance + betweenDis);
                    mRM1_tv_current_distance.setText(String.valueOf(distance));

                    redrawline();
                    System.out.println("success to draw lines");
                }else
                {
                    System.out.println("fail to draw lines");
                }
                //record.addpoint(amapLocation);


            } else {
                String errText = "定位失败," + amapLocation.getErrorCode() + ": "
                        + amapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }
    }


    private void startlocation() {
        //Toast.makeText(ContentActivity.this,"Startloction function is running",Toast.LENGTH_SHORT).show();
        if (mLocationClient == null) {
            mLocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            // 设置定位监听
            mLocationClient.setLocationListener(this);
            // 设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);

            mLocationOption.setInterval(2000);
            // 设置定位参数
            mLocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mLocationClient.startLocation();

        }
    }


    private void initpolyline() {
        mPolyoptions = new PolylineOptions();
        mPolyoptions.width(10f);
        mPolyoptions.color(Color.BLUE);
    }


    private void redrawline() {
        if (mPolyoptions.getPoints().size() > 0) {
            mAMap.clear(true);
            mAMap.addPolyline(mPolyoptions);
        }
    }

    //这里是设置点击的开始与停止按钮的响应事件
    public void RM_OnclickerListener(final ImageView imageView1, final ImageView imageView2, final ImageView imageView3){
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                isstop=0;
                imageView1.startAnimation(setFadeInAnimation("FadeOut"));
                imageView2.startAnimation(setFadeInAnimation("FadeIn"));
                imageView3.startAnimation(setFadeInAnimation("FadeIn"));
                imageView1.setVisibility(View.INVISIBLE);
                imageView2.setVisibility(View.VISIBLE);
                imageView3.setVisibility(View.VISIBLE);

                if(ispause==0){

                    mRM1_tv_current_distance.setText("");
                    mRM1_tv_averagespeed.setText("");
                    mAMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_ROTATE);
                    Snackbar.make(v, "Training starts in 3 seconds!", Snackbar.LENGTH_SHORT).setAction("Action", null).show();

                    new Handler().postDelayed(new Runnable(){
                        public void run() {
                            Snackbar snackbar= Snackbar.make(v, "Run!!!!!!!!!!!!!", Snackbar.LENGTH_SHORT).setAction("Action", null);
                            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.mycolor_red));
                            snackbar.show();
                        }
                    }, 3000);

                    //尝试计时
/*
                    Handler timehandler = new Handler() {
                        public void handleMessage(Message msg) {
                            mRM1_tv_averagespeed.setText((String)msg.obj);
                        }
                    };
                    new Thread().start();*/


                    ispause=1;
                    if (isstop==0) {
                        Log.i("MY", "isChecked");

                        mAMap.clear(true);//从地图上删除所有的Marker，Overlay，Polyline 等覆盖物。
                        if (record != null) {
                            record = null;//record是pathrecord类的对象
                        }
                        record = new PathRecord();
                        System.out.println("开始检测");
                        mStartTime = System.currentTimeMillis();//长整形
                        record.setDate(getcueDate(mStartTime));//开始之后调用pathrecord的setDate方法存储时间
                    }



                }else{
                    Snackbar snackbar=Snackbar.make(v, "Training resumes...", Snackbar.LENGTH_LONG).setAction("Action", null);
                    snackbar.getView().setBackgroundColor(getResources().getColor(R.color.mycolor_RM1_BG_light));
                    snackbar.show();
                }

            }
        });

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                ispause=1;
                imageView2.startAnimation(setFadeInAnimation("FadeOut"));
                imageView3.startAnimation(setFadeInAnimation("FadeOut"));
                imageView1.startAnimation(setFadeInAnimation("FadeIn"));
                imageView2.setVisibility(View.INVISIBLE);
                imageView3.setVisibility(View.INVISIBLE);
                imageView1.setVisibility(View.VISIBLE);
                Snackbar snackbar=Snackbar.make(v, "Training is paused, click 'GO' to continue", Snackbar.LENGTH_LONG).setAction("Action", null);
                snackbar.getView().setBackgroundColor(getResources().getColor(R.color.lightsalmon));
                snackbar.show();
            }
        });

        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ContentActivity.this);
                builder.setTitle("Warning");
                builder.setMessage("确认要结束本次训练吗?");
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Snackbar snackbar=Snackbar.make(v, "Training resumes...", Snackbar.LENGTH_LONG).setAction("Action", null);
                        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.mycolor_RM1_BG_light));
                        snackbar.show();
                    }
                });
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Snackbar.make(v, "Training records have been saved...", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        imageView2.startAnimation(setFadeInAnimation("FadeOut"));
                        imageView3.startAnimation(setFadeInAnimation("FadeOut"));
                        imageView1.startAnimation(setFadeInAnimation("FadeIn"));
                        imageView3.setVisibility(View.INVISIBLE);
                        imageView2.setVisibility(View.INVISIBLE);
                        imageView1.setVisibility(View.VISIBLE);
                        ispause=0;
                        isstop=1;
                        if(isstop==1) {
                            System.out.println("停止");
                            mEndTime = System.currentTimeMillis();
                            saveRecord(record.getPathline(), record.getDate());//use the method saveRecord to save the pathline
					/*use the method getpathline(), record is the object from Pathrecord*/
                        }
                    }
                });
                builder.create().show();


            }
        });
    }



    //初始化图标
    public void Initial_Icon(){
        mRM1_go.setVisibility(View.VISIBLE);
        mRM1_pause.setVisibility(View.INVISIBLE);
        mRM1_stop.setVisibility(View.INVISIBLE);

        mRM2_go.setVisibility(View.VISIBLE);
        mRM2_pause.setVisibility(View.INVISIBLE);
        mRM2_stop.setVisibility(View.INVISIBLE);

        mRM3_go.setVisibility(View.VISIBLE);
        mRM3_pause.setVisibility(View.INVISIBLE);
        mRM3_stop.setVisibility(View.INVISIBLE);

    }

    //set animation action
    public AnimationSet setFadeInAnimation(String type){
        switch (type){

            case "FadeIn":
                AnimationSet animationSet = new AnimationSet(true);
                AlphaAnimation alphaAnimation=new AlphaAnimation(0,1);
                alphaAnimation.setDuration(500);
                animationSet.addAnimation(alphaAnimation);
                return animationSet;

            case "FadeOut":
                AnimationSet animationSet2 = new AnimationSet(true);
                AlphaAnimation alphaAnimation2=new AlphaAnimation(1,0);
                alphaAnimation2.setDuration(300);
                animationSet2.addAnimation(alphaAnimation2);
                return animationSet2;

            default:
                System.out.println("animation not define in this method!!!!!!!!!");
                AnimationSet animationSet3 = new AnimationSet(true);
                AlphaAnimation alphaAnimation3=new AlphaAnimation(1,1);
                alphaAnimation3.setDuration(500);
                animationSet3.addAnimation(alphaAnimation3);
                return animationSet3;

        }

    }



    //统一化时间格式，SimpleDateFormat类定义格式， Date类传入时间
    @SuppressLint("SimpleDateFormat")
    private String getcueDate(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat(
                "yyyy-MM-dd  HH:mm:ss ");
        Date curDate = new Date(time);
        String date = formatter.format(curDate);
        return date;
    }


    protected void saveRecord(List<AMapLocation> list, String time) {
        if (list != null && list.size() > 0) {
            DbHepler = new DbAdapter(this);
            System.out.println("在saveRecord 方法中调用了DbAdapter方法");
            DbHepler.open();//at this point the system open the database
            String duration = getDuration();// getDuration method is used to calculate the time interval
            float distance = getDistance(list);// list is the data from the record.getpathline()
            String average = getAverage(distance);// average is the average speed
            String pathlineSring = getPathLineString(list);
            AMapLocation firstLocaiton = list.get(0);//the first point of list is the forst location
            AMapLocation lastLocaiton = list.get(list.size() - 1);
            String stratpoint = amapLocationToString(firstLocaiton);
            String endpoint = amapLocationToString(lastLocaiton);
            //在这里将读取到的数据存储到DbAdapter类中, createrecord method is using the the insert method from SQL
            DbHepler.createrecord(String.valueOf(distance), duration, average,
                    pathlineSring, stratpoint, endpoint, time);
            DbHepler.close();
        } else {
            Toast.makeText(ContentActivity.this, "没有记录到路径", Toast.LENGTH_SHORT)
                    .show();
        }
    }


    private String getDuration() {
        return String.valueOf((mEndTime - mStartTime) / 1000f);
    }

    private String getAverage(float distance) {
        return String.valueOf(distance / (float) (mEndTime - mStartTime));
    }

    private float getDistance(List<AMapLocation> list) {
        float distance = 0;
        if (list == null || list.size() == 0) {
            return distance;
        }
        for (int i = 0; i < list.size() - 1; i++) {
            AMapLocation firstpoint = list.get(i);
            AMapLocation secondpoint = list.get(i + 1);//距离的计算是利用自带的数据格式，即LatLng
            LatLng firstLatLng = new LatLng(firstpoint.getLatitude(),/*计算方法采用的是得到亮点的经纬度后利用AMapUtils.calculateLineDistance*/
                    firstpoint.getLongitude());
            LatLng secondLatLng = new LatLng(secondpoint.getLatitude(),
                    secondpoint.getLongitude());
            double betweenDis = AMapUtils.calculateLineDistance(firstLatLng,
                    secondLatLng);
            distance = (float) (distance + betweenDis);
        }
        return distance;
    }

    private String getPathLineString(List<AMapLocation> list) {
        if (list == null || list.size() == 0) {
            return "";
        }
        StringBuffer pathline = new StringBuffer();
        for (int i = 0; i < list.size(); i++) {
            AMapLocation location = list.get(i);
            String locString = amapLocationToString(location);
            pathline.append(locString).append(";");
        }
        String pathLineString = pathline.toString();
        pathLineString = pathLineString.substring(0,
                pathLineString.length() - 1);
        return pathLineString;//将每隔地址的信息转化成字符串传回，调用amapLocationToString
    }

    //把AmapLocation对象所包含的实际数值转化为字符串
    private String amapLocationToString(AMapLocation location) {
        StringBuffer locString = new StringBuffer();
        locString.append(location.getLatitude()).append(",");
        locString.append(location.getLongitude()).append(",");
        locString.append(location.getProvider()).append(",");
        locString.append(location.getTime()).append(",");
        locString.append(location.getSpeed()).append(",");
        locString.append(location.getBearing());
        return locString.toString();
    }

    //得到intent传输过来的用户名并将其表示在naviBar中
    private String getusername(){
        Intent intent= getIntent();
        return(intent.getStringExtra("username"));
    }



   /* private static final int REQUEST_PERMISSION_EXTERNAL_STORAGE_CODE = 1;
    private void requestExternalPermission() {
        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_EXTERNAL_STORAGE_CODE);
    }*/


}
