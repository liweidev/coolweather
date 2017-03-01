package com.example.coolweather.app;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePal;

import cn.smssdk.SMSSDK;

/**
 * Created by liwei on 2017/2/21.
 */

public class MyApplication extends Application{

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext=getApplicationContext();
        LitePal.initialize(mContext);
        SMSSDK.initSDK(this, "1bb63ed9d8400", "71e61ef9186c11261c0898663667bd58");
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
