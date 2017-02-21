package com.example.coolweather.utils;

import android.util.Log;

/**
 * Created by liwei on 2017/2/21.
 */

public class LogUtils {
    public static final int VERBOSE=0;
    public static final int DEBUG=1;
    public static final int INFO=2;
    public static final int WARM=3;
    public static final int ERROR=4;
    public static final int NOTHING=5;
    private static int level=1;

    public static void v(String tag,String msg){
        if(level<=VERBOSE){
            Log.v(tag,msg);
        }
    }

    public static void d(String tag,String msg){
        if(level<=DEBUG){
            Log.d(tag,msg);
        }
    }

    public static void i(String tag,String msg){
        if(level<=INFO){
            Log.i(tag,msg);
        }
    }

    public static void w(String tag,String msg){
        if(level<=WARM){
            Log.w(tag,msg);
        }
    }

    public static void e(String tag,String msg){
        if(level<=ERROR){
            Log.e(tag,msg);
        }
    }

}
