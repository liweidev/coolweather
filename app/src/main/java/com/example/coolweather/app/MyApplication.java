package com.example.coolweather.app;

import android.app.Application;
import android.content.Context;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.coolweather.lisenter.MyLocationListener;
import com.example.coolweather.utils.NetworkUtils;

import org.litepal.LitePal;

/**
 * Created by liwei on 2017/2/21.
 */

public class MyApplication extends Application{

    private static Context mContext;
    private LocationClient mClient;
    private BDLocationListener mLisenter=new MyLocationListener();
    @Override
    public void onCreate() {
        super.onCreate();
        mContext=getApplicationContext();
        getLocationInfos();
        LitePal.initialize(mContext);
    }

    /**
     * 获取位置信息
     */
    private void getLocationInfos() {
        //检查网络
        if(!NetworkUtils.isAvalibale()){
            //ToastUtils.showToast("网络异常，定位失败...");
            return;
        }
        mClient=new LocationClient(mContext);
        mClient.registerLocationListener(mLisenter);
        initLocationOptions();
        mClient.start();
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
     * 获取全局Context
     * @return
     */
    public static synchronized Context getContext(){
        if(mContext!=null){
            return mContext;
        }
        return null;
    }



}
