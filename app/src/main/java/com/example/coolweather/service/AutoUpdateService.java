package com.example.coolweather.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.liwei.coolweather.R;
import com.example.coolweather.gson.Weather;
import com.example.coolweather.utils.HttpUtils;
import com.example.coolweather.utils.JsonUtils;
import com.example.coolweather.utils.NotificationUtils;
import com.example.coolweather.utils.ToastUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 后台自动更新天气
 */
public class AutoUpdateService extends Service {
    private String cityName;
    private String weatherInfo;
    private String degree;
    private String windLevel;
    private String imagePath;
    public static final int UPDATE_WEATHER_ERROR=0;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case UPDATE_WEATHER_ERROR:
                    ToastUtils.showToast(R.string.auto_update_weather_error);
                    break;
            }
        }
    };
    public AutoUpdateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        cityName=intent.getStringExtra("cityName");
        weatherInfo=intent.getStringExtra("weatherInfo");
        degree=intent.getStringExtra("degree");
        windLevel=intent.getStringExtra("windLevel");
        imagePath=intent.getStringExtra("imageCodeUrl");
        //前台显示天气
        if(!TextUtils.isEmpty(cityName)&&
                !TextUtils.isEmpty(degree)&&
                !TextUtils.isEmpty(windLevel)&&
                !TextUtils.isEmpty(weatherInfo)){
            Notification nf = NotificationUtils.createNotification(cityName, degree + "    " + weatherInfo + "    " + windLevel, imagePath);
            //stopForeground(true);
            startForeground(1,nf);

        }
        updateWeather();
        updateBingPic();
        AlarmManager manager= (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        int eightHour=8*60*60*1000;
        long targetAtTime= SystemClock.elapsedRealtime()+eightHour;
        Intent i=new Intent(this,AutoUpdateService.class);
        PendingIntent pi=PendingIntent.getService(this,0,i,0);
        manager.cancel(pi);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,targetAtTime,pi);
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 从服务器更新必应图片
     */
    private void updateBingPic() {
        String requestBingPic="http://guolin.tech/api/bing_pic";
        HttpUtils.sendHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                editor.putString("bing_pic",bingPic);
                editor.apply();
            }
        });
    }

    /**
     * 从服务器更新天气
     */
    private void updateWeather() {
        SharedPreferences prs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = prs.getString("weather", null);
        if(weatherString!=null){
            Weather weather = JsonUtils.handlerWeatherResponse(weatherString);
            if(weather!=null){
                String weatherId = weather.basic.weatherId;
                String weatherUrl="http://guolin.tech/api/weather?cityid="+weatherId+"&key=666db77849ad443fb81f94ac03b7ad42";
                HttpUtils.sendHttpRequest(weatherUrl, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                        editor.putBoolean("status",false);
                        editor.apply();
                        handler.sendEmptyMessage(UPDATE_WEATHER_ERROR);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String responseText = response.body().string();
                        Weather weather=JsonUtils.handlerWeatherResponse(responseText);
                        if(weather!=null&&weather.status.equals("ok")){
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                            editor.putString("weather",responseText);
                            editor.putBoolean("status",true);
                            editor.apply();

                        }

                    }
                });
            }
        }
    }


}
