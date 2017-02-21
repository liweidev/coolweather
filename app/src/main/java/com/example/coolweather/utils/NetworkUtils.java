package com.example.coolweather.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.coolweather.app.MyApplication;

/**
 * Created by liwei on 2017/2/21.
 */

public class NetworkUtils {
    private static ConnectivityManager manager=
            (ConnectivityManager) MyApplication
            .getContext()
            .getSystemService(Context.CONNECTIVITY_SERVICE);


    /**
     * 获取网络状态类型
     * @return “无网络、WIFI、MOBILE三种”
     */
    public static String getNetworkType(){
        String typeName="";
        if(!isAvalibale()){
            typeName= "当前网络异常";
            return typeName;
        }
        if(manager!=null){
            NetworkInfo netInfo = manager.getActiveNetworkInfo();
            if(netInfo!=null&&netInfo.isConnected()){
                int type=netInfo.getType();
                switch (type){
                    case ConnectivityManager.TYPE_WIFI:
                        typeName="WIFI";
                        break;
                    case ConnectivityManager.TYPE_MOBILE:
                        typeName="MOBILE";
                        break;
                    default:

                        break;
                }
                return typeName;
            }
        }

        return typeName;
    }

    /**
     * 判断当前网络是否可用
     * @return
     * true -- 可用
     * false -- 不可用
     */
    public static boolean isAvalibale(){
        if(manager!=null){
            NetworkInfo netInfo = manager.getActiveNetworkInfo();
            if(netInfo!=null&&netInfo.isConnected()){
                return true;
            }
        }
        return false;
    }


}
