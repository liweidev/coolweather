package com.example.coolweather.app;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePal;

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
