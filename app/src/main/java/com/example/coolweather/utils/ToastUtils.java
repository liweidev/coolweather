package com.example.coolweather.utils;

import android.widget.Toast;

import com.example.coolweather.app.MyApplication;

/**
 * Created by liwei on 2017/2/21.
 */

public class ToastUtils {

    public static void showToast(String msg){
        Toast.makeText(MyApplication.getContext(),msg,Toast.LENGTH_SHORT).show();
    }

    public static void showToast(int resId){
        Toast.makeText(MyApplication.getContext(),resId,Toast.LENGTH_SHORT).show();
    }

}
