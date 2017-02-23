package com.example.coolweather.ui;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.coolweather.R;
import com.example.coolweather.app.MyApplication;
import com.example.coolweather.base.MyBaseActivity;
import com.example.coolweather.lisenter.MyLocationListener;
import com.example.coolweather.utils.NetworkUtils;
import com.example.coolweather.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends MyBaseActivity {
    /**
     * 管理权限的集合
     */
    private List<String>permisonList=new ArrayList<>();
    private LocationClient mClient;
    private BDLocationListener mLisenter=new MyLocationListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //配置位置相关参数
        getLocationInfos();
        //检查权限
        checkPermison(Manifest.permission.ACCESS_FINE_LOCATION);

        if(permisonList.size()>0){
            //需要申请权限
            String[] permisons=permisonList.toArray(new String[permisonList.size()]);
            ActivityCompat.requestPermissions(this,permisons,1);
        }else{
            //检查网络
            if(!NetworkUtils.isAvalibale()){
                ToastUtils.showToast("网络异常，定位失败...");
            }else {
                mClient.start();
            }
        }
        SharedPreferences prs = PreferenceManager.getDefaultSharedPreferences(this);
        if(prs.getString("weather",null)!=null){
            Intent intent=new Intent(this,WeatherActivity.class);
            startActivity(intent);
            finish();
        }
    }

    /**
     * 获取位置信息
     */
    private void getLocationInfos() {
        mClient=new LocationClient(MyApplication.getContext());
        mClient.registerLocationListener(mLisenter);
        initLocationOptions();
    }

    /**
     * LocationClient配置信息
     */
    private void initLocationOptions() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备

        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系

        int span=0;
        option.setScanSpan(span);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的

        option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要

        option.setOpenGps(true);
        //可选，默认false,设置是否使用gps

        option.setLocationNotify(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果

        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”

        option.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到

        option.setIgnoreKillProcess(false);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死

        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集

        option.setEnableSimulateGps(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要

        mClient.setLocOption(option);
    }

    /**
     * 检查权限
     * @param permisson
     */
    private void checkPermison(String permisson){
        if(ContextCompat.checkSelfPermission(this,permisson)!= PackageManager.PERMISSION_GRANTED){
            permisonList.add(permisson);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                if(grantResults.length>0){
                    for(int i:grantResults){
                        if(i==PackageManager.PERMISSION_GRANTED){
                            //定位
                            //检查网络
                            if(!NetworkUtils.isAvalibale()){
                                ToastUtils.showToast("网络异常，定位失败...");
                                return;
                            }
                            mClient.start();
                        }else{
                            //提示用户拒绝权限无法使用程序
                            ToastUtils.showToast(R.string.refuse_permission);
                        }
                    }
                }
                break;

        }

    }
}
