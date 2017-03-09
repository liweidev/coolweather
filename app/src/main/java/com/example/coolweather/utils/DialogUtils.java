package com.example.coolweather.utils;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by liwei on 2017/3/9.
 * 进度对话
 */

public class DialogUtils {
    private static ProgressDialog dialog;

    public static void  showDialog(String message, Context context){
        dialog=getInstance(context);
        dialog.setMessage(message);
        dialog.setTitle("请稍后");
        dialog.setCancelable(false);
        dialog.show();
    }

    private static  ProgressDialog getInstance(Context context){
        if(dialog==null){
            dialog=new ProgressDialog(context);
        }
        return dialog;
    }

    public static void dissmissDialog(){
        if(dialog!=null){
            dialog.dismiss();
            dialog=null;
        }
    }

}
