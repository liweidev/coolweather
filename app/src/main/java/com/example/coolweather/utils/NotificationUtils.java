package com.example.coolweather.utils;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.example.coolweather.R;
import com.example.coolweather.app.MyApplication;
import com.example.coolweather.lisenter.ImagePathLisenter;
import com.example.coolweather.ui.WeatherActivity;

import java.io.File;
import java.util.concurrent.ExecutionException;

/**
 * Created by liwei on 2017/2/23.
 */

public class NotificationUtils {

    /**
     * 前台服务Notification
     * @param contentTitle
     * @param contentText
     * @return
     */
    public static  Notification createNotification(String contentTitle,String contentText,String  imagePath){
        NotificationCompat.Builder builder
                =new NotificationCompat.Builder(MyApplication.getContext());
        Intent intent=new Intent(MyApplication.getContext(), WeatherActivity.class);
        PendingIntent pi=PendingIntent.getActivity(MyApplication.getContext(),0,intent,0);
        builder.setContentTitle(contentTitle);
        builder.setContentIntent(pi);
        builder.setContentText(contentText);
        builder.setSmallIcon(R.drawable.w);
        builder.setWhen(System.currentTimeMillis());
        builder.setLargeIcon(BitmapFactory.decodeFile(imagePath));
        return builder.build();
    }

    /**
     * 获取图片缓存地址
     * @param imageCodeUrl
     */
    public static void  getImagePathFromCatch(String imageCodeUrl, ImagePathLisenter lisenter) {
        new Thread(()->{
            FutureTarget<File> future = Glide.with(MyApplication.getContext()).load(imageCodeUrl)
                    .downloadOnly(100, 100);
            try {
                File file = future.get();
                String path=file.getAbsolutePath();
                lisenter.sendImagePath(path);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }).start();
    }

}
